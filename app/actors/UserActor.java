package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.SearchResults;
import model.Tweet;
import play.libs.Json;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserActor  extends AbstractActor {

    private final ActorRef ws;
    play.Logger.ALogger logger = play.Logger.of(getClass());
    private final String sessionId;

    /**
     * Constructor
     * @param wsOut pass the reference of the UserActor.
     */
    public UserActor(final ActorRef wsOut,String sessionId){
        this.ws = wsOut;
        this.sessionId=sessionId;
    }

    /**
     * Props method, to create an ActorRef of UserActor.
     * @param wsOut ActorRef
     * @return An ActorRef of UserActor
     */
    public static Props props(final ActorRef wsOut,String sessionId){
        return Props.create(UserActor.class, ()->new UserActor(wsOut,sessionId));
    }


    /**
     * Register the UserActor to TwitterActor.
     */
    @Override
    public void preStart(){
        logger.error("User Actor Registered");
        context().actorSelection("/user/twitterActor")
                .tell(new Message.Register(Message.TYPE.KEYWORD,this.sessionId), self());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.Update.class, this::sendUpdate)
                .build();
    }

    private void sendUpdate(Message.Update msg) {

        logger.error("Sending Update");

        CompletableFuture<List<SearchResults>> searchResult = msg.getTweets();

        searchResult.thenApply(tweets -> {

            ObjectNode response = Json.newObject();
            ArrayNode arrayNode = response.putArray("updates");

            for(SearchResults result: tweets) {

                ObjectNode tweetNode = arrayNode.addObject();
                tweetNode.put("keyword", result.getKeyword());
                tweetNode.put("overall_sentiment", result.getOverallResult());

                ArrayNode tweetsForKey = tweetNode.putArray("tweets");
                int i=0;
                for(Tweet item: result.getTweets()) {

                    ObjectNode tweet = tweetsForKey.addObject();
                    tweet.put("text", item.getText());
                    tweet.put("sentiment", item.getTweetSentiment().get(i++));
                    tweet.put("user_name", item.getUser().getUserScreenName());
                    tweet.put("user_image", item.getUser().getUserProfileImage());
                    tweet.put("user_link", item.getUser().getUserId());

                    if(item.getHashTags().size() > 0) {
                        tweet.put("hashtag", item.getHashTags().get(0));
                    }
                }
            }

            return response;

        }).thenAccept(response -> ws.tell(response, self()));

    }
}
