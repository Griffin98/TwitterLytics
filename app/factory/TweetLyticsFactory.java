package factory;

import model.SearchResults;
import model.Tweet;
import play.libs.oauth.OAuth.RequestToken;
import services.TwitterAPIService;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Centralized class to work with twitter.
 *
 * Singleton Pattern
 */
public class TweetLyticsFactory {

    private static TweetLyticsFactory INSTANCE = null;

    public static final String API_KEY = "M9Gp7QQDVS3hwXsblhi2baSvn";
    public static final String API_KEY_SECRET = "2JR5ZlrSFwZX2PHXdccnAoAuLES6KKQvAJOWYWO54Bma7AcImh";

    private static final int MAX_SEARCH_LIMIT = 250;
    private static final int DISPLAY_LIMIT = 10;
    private final CompletableFuture<TwitterAPIService> twitterAPIService;

    private TweetLyticsFactory(RequestToken accessToken) {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(API_KEY)
                .setOAuthConsumerSecret(API_KEY_SECRET)
                .setOAuthAccessToken(accessToken.token)
                .setOAuthAccessTokenSecret(accessToken.secret);

        TwitterFactory twitterFactory = new TwitterFactory(cb.build());
        this.twitterAPIService = CompletableFuture.supplyAsync(() -> new TwitterAPIService(twitterFactory.getInstance()));

    }

    /**
     *
     * @param accessToken
     * @return
     */
    public static TweetLyticsFactory getInstance(RequestToken accessToken) {

        if (INSTANCE == null) {
            INSTANCE = new TweetLyticsFactory(accessToken);
        }

        return INSTANCE;
    }

    /**
     *
     * @return instance of TweetLyticsFactory(Using it in TweetWords)
     */
    public static TweetLyticsFactory getInstance(){
        return INSTANCE;
    }
//    public List<SearchResults> getListTweetSearchResults() {
//        return listTweetSearchResults;
//    }
//    public void setListTweetSearchResults(List<SearchResults> listTweetSearchResults) {
//        this.listTweetSearchResults=listTweetSearchResults;
//    }
    /**
     *
     * @param keyword
     * @return
     */
    public CompletableFuture<List<SearchResults>> getTweetsByKeyword(String keyword) {

        CompletableFuture<List<SearchResults>> results = twitterAPIService.thenApply(twitter -> {
            List<SearchResults> listOfSearchResults = new ArrayList<SearchResults>();
            List<Tweet> allTweets = twitter.getTweets(keyword, MAX_SEARCH_LIMIT);

            //
            List<Tweet> tweets = allTweets.subList(0, DISPLAY_LIMIT);

            SearchResults res = new SearchResults(keyword, tweets);
            listOfSearchResults.add(res);
//            if(res!=null) {
//                listTweetSearchResults.add(0,new SearchResults(keyword, allTweets));
//            }
            return listOfSearchResults;
        });

        return results;
    }


    /**
     * Used to get a CompletableFuture map which stores word:frequency pairs for 250 tweets of selected search key
     * @param searchResultsList CompletableFuture list of search results
     * @param index index of the key
     * @return CompletableFuture map to get word:frequency
     */
    public static CompletableFuture<Map<String, Long>> findStatistics(CompletableFuture<List<SearchResults>> searchResultsList, Integer index){
        CompletableFuture<List<Tweet>> listCompletableFutureTweets = searchResultsList.thenApply(searchResults -> searchResults.get(index).getTweets());
        return listCompletableFutureTweets.thenApply(tweets -> tweets.stream()
                .map(Tweet::getText)
                .flatMap(text -> Arrays.stream(text.split(" ")))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new)));
    }

}