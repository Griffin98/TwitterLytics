package factory;

import model.SearchResults;
import model.Tweet;
import play.libs.oauth.OAuth.RequestToken;
import services.TwitterAPIService;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Dhyey Patel
 * This factory class provide unified way to perform asynchronous function call to
 * {@link TwitterAPIService} using {@link CompletableFuture}.
 * 
 */
public class TweetLyticsFactory {

    private static TweetLyticsFactory INSTANCE = null;

    // Twitter Consumer Key.
    public static final String API_KEY = "M9Gp7QQDVS3hwXsblhi2baSvn";

    // Twitter Consumer Key Secret.
    public static final String API_KEY_SECRET = "2JR5ZlrSFwZX2PHXdccnAoAuLES6KKQvAJOWYWO54Bma7AcImh";

    // Numbers of tweets to fetch. Max is 100.
    private static final int MAX_SEARCH_LIMIT = 100;

    private final CompletableFuture<TwitterAPIService> twitterAPIService;

    /**
     * constructor for {@link TweetLyticsFactory}
     * @param accessToken  access token
     */
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
     * Method to get single instance of class
     * @param accessToken OAUTH access toke from the twitter.
     * @return instance of {@link TweetLyticsFactory}
     */
    public static TweetLyticsFactory getInstance(RequestToken accessToken) {

        if(accessToken == null && !(accessToken instanceof RequestToken))
            throw new NullPointerException("Invalid token specified");

        if (INSTANCE == null) {
            INSTANCE = new TweetLyticsFactory(accessToken);
        }

        return INSTANCE;
    }

    /**
     * Method to get list of {@link SearchResults} asynchronously using {@link CompletableFuture}.
     * @param keyword keyword for which tweets need to be searched.
     * @return completable future list of {@link SearchResults}.
     */
    public CompletableFuture<List<SearchResults>> getTweetsByKeyword(String keyword) {

        CompletableFuture<List<SearchResults>> results = twitterAPIService.thenApply(twitter -> {
            List<SearchResults> listOfSearchResults = new ArrayList<SearchResults>();
            List<Tweet> allTweets = twitter.getTweets(keyword, MAX_SEARCH_LIMIT);

            SentimentAnalyzerFactory sentimentAnalyzerFactory = new SentimentAnalyzerFactory();
            List<String> resultsIndividualTweets = sentimentAnalyzerFactory.getEmotionOfTweet(allTweets);
            String overallResult = sentimentAnalyzerFactory.getResultOfAllTweet(resultsIndividualTweets);
            allTweets.forEach(u -> u.setTweetSentiment(resultsIndividualTweets));

            SearchResults res = new SearchResults(keyword, allTweets, overallResult);
            listOfSearchResults.add(res);
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
    public CompletableFuture<Map<String, Long>> findStatistics(CompletableFuture<List<SearchResults>> searchResultsList, Integer index){
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

    /**
     * Method provides the list of tweets by the specified user.
     * @param userID twitter id of the user.
     * @return completable future list of {@link Tweet}.
     */
    public CompletableFuture<List<Tweet>> getUserListTweets(Long userID){
        CompletableFuture<List<Tweet>> futureUserHomeLine=twitterAPIService.thenApply((twitterConnection)-> twitterConnection.getHomeLineById(userID));
        return futureUserHomeLine;
    }

}