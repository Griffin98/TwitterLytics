package actors;

import akka.actor.AbstractActorWithTimers;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.pattern.Patterns;
import factory.TweetLyticsFactory;
import model.SearchResults;
import model.Tweet;
import play.libs.oauth.OAuth.RequestToken;
import scala.compat.java8.FutureConverters;
import scala.concurrent.duration.Duration;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TwitterActor extends AbstractActorWithTimers {

    //private final Set<ActorRef> userActors;
    //private final HashSet<String> keywords;
    private final HashMap<String, List<Tweet>> history;

    private String currentSession = null;
    private ArrayList<String> listOfSession;
    private HashMap<String, ArrayList<String>> sessionMapKeyword;
    private HashMap<String, List<SearchResults>> sessionMapSearchResults;
    private HashMap<String, Set<ActorRef>> sessionMapActorRef;
    private ActorRef sentimentAnalyzerActor;
    private final TweetLyticsFactory tweetLyticsFactory;

    play.Logger.ALogger logger = play.Logger.of(getClass());

    public TwitterActor(RequestToken token) {
        //this.userActors = new HashSet<>();
        this.tweetLyticsFactory = TweetLyticsFactory.getInstance(token);

        //this.keywords = new HashSet<>();
        this.history = new HashMap<>();
        this.listOfSession = new ArrayList<>();
        this.sessionMapActorRef = new HashMap<>();
        this.sessionMapKeyword = new HashMap<>();
        this.sessionMapSearchResults=new HashMap<>();
        this.sentimentAnalyzerActor = this.getContext().getSystem().actorOf(SentimentAnalyzerActor.getProps());
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

                    Set<ActorRef> actorRefSet = sessionMapActorRef.get(currentSession);
                    actorRefSet.add(sender());
                    sessionMapActorRef.put(currentSession, actorRefSet);
                    //userActors.add(sender());
                })
                .match(Message.FindStatistics.class, msg -> {
                    logger.error("Finding statistics for index:",msg.getIndex());
                    List<SearchResults> searchResults1 = sessionMapSearchResults.get(msg.getSessionId());
                    CompletableFuture<List<SearchResults>> searchResultsListComp = CompletableFuture.supplyAsync(() -> searchResults1);
                    CompletableFuture<List<Tweet>> listCompletableFutureTweets = searchResultsListComp.thenApply(searchResults -> searchResults.get(msg.getIndex()).getTweets());
                    CompletableFuture<LinkedHashMap<String, Long>> mapCompletableFuture = (CompletableFuture<LinkedHashMap<String, Long>>) listCompletableFutureTweets.thenApply(tweets -> tweets.stream()
                            .map(Tweet::getText)
                            .flatMap(text -> Arrays.stream(text.split(" ")))
                            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                            .entrySet().stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new)));
                    sender().tell(mapCompletableFuture,self());
                })
                .match(Message.Keyword.class, msg -> {
                    logger.error("Received message");
                    logger.error(msg.getKeyword());

                    ArrayList<String> key = sessionMapKeyword.get(currentSession);
//                    ArrayList<String> key = sessionMapKeyword.get(msg.getSessionId());
                    key.add(msg.getKeyword());
                    sessionMapKeyword.put(currentSession, key);
//                    sessionMapKeyword.put(msg.getSessionId(), key);
                    //keywords.add(msg.getKeyword());

                    CompletableFuture<List<SearchResults>>  results = tweetLyticsFactory.getTweetsByKeyword(msg.getKeyword())
                            .thenApply(res -> {
                                List<Tweet> t = res.get(0).getTweets();
                                history.put(msg.getKeyword(), t);
                                sessionMapSearchResults.put(currentSession,res);
//                                sessionMapSearchResults.put(msg.getSessionId(),res);
                                return res;
                            });

                    sender().tell(results, self());
                })
                .match(Message.Session.class ,msg -> {
                    logger.error("Session started : ", msg.getSessionId());
                    currentSession = msg.getSessionId();
                    listOfSession.add(currentSession);
                    sessionMapKeyword.put(currentSession, new ArrayList<>());
                    sessionMapActorRef.put(currentSession, new HashSet<>());
                })
                .match(Message.Tick.class, tick -> {
                    notifyUsers();
                })
                .build();
    }

    private void notifyUsers() {


        for(String session: listOfSession) {

            ArrayList<String> keywords = sessionMapKeyword.get(session);
            Set<ActorRef> userActors = sessionMapActorRef.get(session);

            CompletableFuture<List<SearchResults>> result = CompletableFuture.supplyAsync(() -> {
                List<SearchResults> results = new ArrayList<>();

                for(String key : keywords) {

                    tweetLyticsFactory.getTweetsByKeyword(key)
                            .thenAccept(res -> {
                                List<Tweet> now = res.get(0).getTweets();
                                List<Tweet> before = history.get(key);

                                List<Tweet> diff = getUpdate(now, before);
                                history.put(key, diff);

                                List<String> singleTweetResult =  FutureConverters.toJava(Patterns.ask(sentimentAnalyzerActor, new SentimentAnalyzerActor.GetSingleTweetResult(diff), 5000))
                                        .thenApply(o -> (List<String>) o).toCompletableFuture().join();

                                String allResult =  FutureConverters.toJava(Patterns.ask(sentimentAnalyzerActor, new SentimentAnalyzerActor.GetOverallTweetResult(singleTweetResult), 5000))
                                        .thenApply(o -> (String) o)
                                        .toCompletableFuture().join();
//                                SentimentAnalyzerFactory sentimentAnalyzerFactory = new SentimentAnalyzerFactory();
//                                List<String> resultsIndividualTweets = sentimentAnalyzerFactory.getEmotionOfTweet(diff);
//                                String overallResult = sentimentAnalyzerFactory.getResultOfAllTweet(resultsIndividualTweets);
                                diff.forEach(u -> u.setTweetSentiment(singleTweetResult));

                                SearchResults searchResults = new SearchResults(key, diff, allResult);
                                results.add(searchResults);

                            });
                }
                Collections.reverse(results);

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
