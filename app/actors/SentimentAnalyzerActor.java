package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import factory.SentimentAnalyzerFactory;
import model.Tweet;
import play.libs.oauth.OAuth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Darshan on 03-12-2020 17:34
 * @project TwitterLytics-v2
 */
public class SentimentAnalyzerActor extends AbstractActor {
    private final SentimentAnalyzerFactory sentimentAnalyzerFactory = new SentimentAnalyzerFactory();

    private SentimentAnalyzerActor() {

    }

    public static Props getProps(){
        return Props.create(SentimentAnalyzerActor.class, SentimentAnalyzerActor::new);
    }

    public static final class GetSingleTweetResult {

        private List<Tweet> tweet;

        public GetSingleTweetResult(List<Tweet> tweet) {
            this.tweet = tweet;
        }

    }

    public static final class GetOverallTweetResult {

        private List<String> overallResult;

        public GetOverallTweetResult(List<String> overallResult) {
            this.overallResult = overallResult;
        }

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(GetSingleTweetResult.class, this::onGetSingleTweetResult)
                .match(GetOverallTweetResult.class, this::onGetOverallTweetResult)
                .build();
    }

    private void onGetSingleTweetResult(GetSingleTweetResult singleResult) {
        List<String> resultsIndividualTweets = sentimentAnalyzerFactory.getEmotionOfTweet(singleResult.tweet);
        getSender().tell(resultsIndividualTweets, getSelf());
    }

    private void onGetOverallTweetResult(GetOverallTweetResult overallTweetResult){
        String overallResult = sentimentAnalyzerFactory.getResultOfAllTweet(overallTweetResult.overallResult);
        getSender().tell(overallResult, getSelf());
    }


}

