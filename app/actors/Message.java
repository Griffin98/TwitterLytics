package actors;

import model.SearchResults;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

public class Message {

    public static final class Register{

    }

    public static final class Tick{

    }

    public static final class Keyword{
        private String keyword;

        /**
         *
         * @param
         */
        public Keyword(String keyword){
            this.keyword = requireNonNull(keyword);
        }

        public String getKeyword(){
            return keyword;
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


}
