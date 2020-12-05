package actors;

import model.SearchResults;
import model.Tweet;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

public class Message {

    public static final class Register{

        private TYPE type;
        private final String sessionId;
        public Register(TYPE type,String sessionId) {
            this.sessionId=sessionId;
            this.type = type;
        }
        public TYPE getRegistrationType() {return type;}
        public String getSessionId() {
            return sessionId;
        }
    }

    public static final class Tick{

    }

    public static final class Keyword{
        private String keyword;
        private String sessionId;

        private TYPE messageType;
        /**
         *
         * @param
         */
        public Keyword(String keyword,String sessionId, TYPE t){
            this.keyword = requireNonNull(keyword);
            this.sessionId=sessionId;
            this.messageType = t;

        }
        public String getSessionId() {
            return sessionId;
        }
        public String getKeyword(){
            return keyword;
        }

        public TYPE getMessageType() {
            return messageType;
        }


    }

    /**
     * This class is to pass a update message.
     */
    public static class Update {

        final private CompletableFuture<List<SearchResults>> tweets;

        /**
         * Constructor
         * @param tweets A list of tweets
         */
        public Update(CompletableFuture<List<SearchResults>> tweets){
            this.tweets = requireNonNull(tweets);
        }


        /**
         * Get the list of tweets
         * @return The list of tweets.
         */
        public CompletableFuture<List<SearchResults>> getTweets(){
            return this.tweets;
        }
    }
    public static class FindStatistics{
        final private String sessionId;
        final private Integer index;
        public FindStatistics(String sessionId,Integer index) {
            this.sessionId = sessionId;
            this.index=index;
        }
        public String getSessionId() {
            return sessionId;
        }
        public Integer getIndex() {
            return index;
        }
    }

    /**
     *
     */
    public static class Session {

        final private String sessionId;

        public Session(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getSessionId() {
            return sessionId;
        }
    }

    public static final class GetSingleTweetResult {

        public List<Tweet> getTweet() {
            return tweet;
        }

        private final List<Tweet> tweet;

        public GetSingleTweetResult(List<Tweet> tweet) {
            this.tweet = tweet;
        }

    }

    public static final class GetOverallTweetResult {

        private final List<String> overallResult;

        public List<String> getOverallResult() {
            return overallResult;
        }

        public GetOverallTweetResult(List<String> overallResult) {
            this.overallResult = overallResult;
        }

    }

    public enum TYPE { HASHTAG, KEYWORD };


}
