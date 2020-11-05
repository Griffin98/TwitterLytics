package factory;

import model.SearchResults;
import model.Tweet;
import play.libs.oauth.OAuth.RequestToken;
import services.TwitterAPIService;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Common Factory class to retrieve tweets from Twitter. This class abstracts inner call to TwitterAPI service.
 * This class implements asynchronous call to TwitterAPIService using {link #Futures}.
 *
 * Singleton Pattern
 */
public class TweetLyticsFactory {

    // Instance of TweetLytics Factory class.
    private static TweetLyticsFactory INSTANCE = null;

    // Twitter Consumer Key.
    public static final String API_KEY = "M9Gp7QQDVS3hwXsblhi2baSvn";

    // Twitter Consumer Key Secret.
    public static final String API_KEY_SECRET = "2JR5ZlrSFwZX2PHXdccnAoAuLES6KKQvAJOWYWO54Bma7AcImh";

    // Number of Tweet feeds to fetch.
    private static final int MAX_SEARCH_LIMIT = 250;

    // Number of Tweets to be displayed.
    private static final int DISPLAY_LIMIT = 10;

    // Instance of TwitterAPI Service. Asynchronous.
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
     * Method to get single instance of TweetLyticsFactory.
     *
     * @param accessToken Twitter API AccessToken.
     * @return instance of TweetLyticsFactory.
     */
    public static TweetLyticsFactory getInstance(RequestToken accessToken) {

        if (INSTANCE == null) {
            INSTANCE = new TweetLyticsFactory(accessToken);
        }

        return INSTANCE;
    }

    /**
     * Methods returns list of {link #SearchResults} asynchronously using {link #Futures}.
     * @param keyword Keyword for which tweets need to be fetched.
     * @return List of {link #SearchResults}.
     */
    public CompletableFuture<List<SearchResults>> getTweetsByKeyword(String keyword) {

        CompletableFuture<List<SearchResults>> results = twitterAPIService.thenApply(twitter -> {
            List<SearchResults> listOfSearchResults = new ArrayList<SearchResults>();
            List<Tweet> allTweets = twitter.getTweets(keyword, MAX_SEARCH_LIMIT);

            // From 250 tweets fetched we wish to display only 10 tweets. So take a subset from the whole list.
            List<Tweet> tweets = allTweets.subList(0, DISPLAY_LIMIT);

            SearchResults res = new SearchResults(keyword, tweets);
            listOfSearchResults.add(res);
            return listOfSearchResults;
        });

        return results;
    }

}