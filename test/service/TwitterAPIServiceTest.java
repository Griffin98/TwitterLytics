package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import model.Tweet;
import org.junit.Before;
import org.junit.Test;
import services.TwitterAPIService;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;

/**
 * This class perform JUnit testing for TwitterAPIService class.
 */
public class TwitterAPIServiceTest {

    Twitter twitter;

    /**
     * Setup method to initialize {@link #twitter}.
     */
    @Before
    public void setUp() {
        // Real tokens and api key to simulate twitter api call.
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("M9Gp7QQDVS3hwXsblhi2baSvn")
                .setOAuthConsumerSecret("2JR5ZlrSFwZX2PHXdccnAoAuLES6KKQvAJOWYWO54Bma7AcImh")
                .setOAuthAccessToken("804992947015929857-DhDb94zyLSUkZLkXBprs48w9diAMPy9")
                .setOAuthAccessTokenSecret("t9LOxQbuAuAN0R4rw2Xq7KKZpXw54cECifvxDzAXBD0EM");

        TwitterFactory twitterFactory = new TwitterFactory(cb.build());
        twitter = twitterFactory.getInstance();
    }

    /**
     * Test that we don't accept null value for constructor.
     */
    @Test(expected=NullPointerException.class)
    public void testBadConstructor() {
      TwitterAPIService api =   new TwitterAPIService(null);
    }

    /**
     * Test Construtor of {@link TwitterAPIService}.
     */
    @Test
    public void testConstructor() {
        TwitterAPIService api = new TwitterAPIService(twitter);
    }

    /**
     * Test the getTweets() method of {@link TwitterAPIService}.
     */
    @Test
    public void testGetTweets() {
        TwitterAPIService api = new TwitterAPIService(twitter);

        // for correct search expect same size as result
        int searchLimit = 100;
        List<Tweet> tweets;
        tweets = api.getTweets("health", searchLimit);
        assertEquals(searchLimit, tweets.size());

        searchLimit = 5;
        tweets = api.getTweets("health", searchLimit);
        assertEquals(searchLimit, tweets.size());

        // for incorrect search
        tweets = api.getTweets("124!@`*12n29342niwcwefwegWEGWG", searchLimit);
        assertEquals(0, tweets.size());
    }

    /**
     * Test the getUserProfile() method of {@link TwitterAPIService}.
     */
    @Test
    public void testGetUserHometimeline() {
        // Cocordia university user handle;
        long user_id = 18173399;
        TwitterAPIService api = new TwitterAPIService(twitter);

        List<Tweet> tweets;
        // for correct user id;
        tweets = api.getHomeLineById(user_id);
        assertTrue(tweets.size() > 0);

        // for incorrect user id;
        tweets = api.getHomeLineById(00);
        assertEquals(null,tweets);
    }

}