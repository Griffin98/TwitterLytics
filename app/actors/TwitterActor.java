package actors;

import akka.actor.AbstractActorWithTimers;
import akka.actor.ActorRef;
import akka.actor.Props;
import factory.SentimentAnalyzerFactory;
import factory.TweetLyticsFactory;
import model.SearchData;
import model.SearchResults;
import model.Tweet;
import play.libs.oauth.OAuth.RequestToken;
import scala.concurrent.duration.Duration;
import services.TwitterAPIService;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TwitterActor extends AbstractActorWithTimers {

    private final Set<ActorRef> userActors;
    private final HashSet<String> keywords;
    private final HashMap<String, List<Tweet>> history;
    private List<SearchResults> indexedResult;

    private final TweetLyticsFactory tweetLyticsFactory;

    play.Logger.ALogger logger = play.Logger.of(getClass());

    public TwitterActor(RequestToken token) {
        this.userActors = new HashSet<>();
        this.tweetLyticsFactory = TweetLyticsFactory.getInstance(token);

        this.keywords = new HashSet<>();
        this.history = new HashMap<>();
        this.indexedResult = new ArrayList<>();
    }


    /**
     * get TwitterActor
     * @return Create TwitterActor class
     */
    public static Props getProps( RequestToken requestToken){
        return Props.create(TwitterActor.class, () -> new TwitterActor(requestToken));
    }

    /**
     * configuration and set timer
     */
    @Override
    public void preStart(){
        getTimers().startPeriodicTimer("Timer", new Message.Tick(), Duration.create(5, TimeUnit.SECONDS));
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.Register.class, msg -> {
                    logger.error("Received new user");
                    userActors.add(sender());
                })
                .match(Message.Keyword.class, msg -> {
                    logger.error("Received message");
                    logger.error(msg.getKeyword());

                    keywords.add(msg.getKeyword());

                    CompletableFuture<List<SearchResults>>  results = tweetLyticsFactory.getTweetsByKeyword(msg.getKeyword())
                            .thenApply(res -> {

                                List<Tweet> t = res.get(0).getTweets();
                                history.put(msg.getKeyword(), t);

                                return res;
                            });

                    sender().tell(results, self());
                })
                .match(Message.Tick.class, tick -> {
                    notifyUsers();
                })
                .build();
    }

    private void notifyUsers() {

        CompletableFuture<List<SearchResults>> result = CompletableFuture.supplyAsync(() -> {
            List<SearchResults> results = new ArrayList<>();

            for(String key : keywords) {

                tweetLyticsFactory.getTweetsByKeyword(key)
                        .thenAccept(res -> {
                            List<Tweet> now = res.get(0).getTweets();
                            List<Tweet> before = history.get(key);

                            List<Tweet> diff = getUpdate(now, before);
                            history.put(key, diff);

                            SentimentAnalyzerFactory sentimentAnalyzerFactory = new SentimentAnalyzerFactory();
                            List<String> resultsIndividualTweets = sentimentAnalyzerFactory.getEmotionOfTweet(diff);
                            String overallResult = sentimentAnalyzerFactory.getResultOfAllTweet(resultsIndividualTweets);
                            diff.forEach(u -> u.setTweetSentiment(resultsIndividualTweets));

                            SearchResults searchResults = new SearchResults(key, diff, overallResult);
                            results.add(searchResults);
                            
                        });
            }
            Collections.reverse(results);

            indexedResult = results;

            return results;
        });

        for(ActorRef user: userActors) {
            try {
                user.tell(new Message.Update(result), self());
            }catch (Exception e) {
                return;
            }
        }

    }

    /**
     * update the old history Tweet
     * @param now new Tweet list
     * @param history old Tweet list
     * @return new list of Tweet
     */
    private List<Tweet> getUpdate(List<Tweet> now, List<Tweet> history) {
        Stream<Tweet> combination = Stream.concat(now.stream(), history.stream());

        List<Tweet> update = combination
                .distinct()
                .sorted((op1, op2) -> op1.getCreationTime().before(op2.getCreationTime())? 1 : -1 )
                .limit(10)
                .collect(Collectors.toList());


        return update;
    }


}
