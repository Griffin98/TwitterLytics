package factory;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import model.SearchResults;
import model.Tweet;
import org.junit.Test;
import play.libs.oauth.OAuth.RequestToken;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * This class performs testing for {@link TweetLyticsFactory}.
 */
public class TweetLyticsFactoryTest {

    // Real token for testing purpose;
    private static final String OAUTH_TOKEN = "804992947015929857-DhDb94zyLSUkZLkXBprs48w9diAMPy9";
    private static final String OAUTH_TOKEN_SECRET = "t9LOxQbuAuAN0R4rw2Xq7KKZpXw54cECifvxDzAXBD0EM";

    /**
     * Test that we don't accept null request token.
     */
    @Test(expected = NullPointerException.class)
    public void testBadSingleton() {
        TweetLyticsFactory instance = TweetLyticsFactory.getInstance(null);
    }

    /**
     * Test that we correctly implement singleton pattern for {@link TweetLyticsFactory}.
     */
    @Test
    public void testSingleton() {
        RequestToken requestToken = new RequestToken(OAUTH_TOKEN, OAUTH_TOKEN_SECRET);
        TweetLyticsFactory instance = TweetLyticsFactory.getInstance(requestToken);

        // Check if we get correct instance
        assertTrue(instance instanceof TweetLyticsFactory);

        // Check if we get same instance, singleton pattern.
        TweetLyticsFactory instance2 = TweetLyticsFactory.getInstance(requestToken);
        assertEquals(instance, instance2);
    }

    /**
     * Test the getTweetByKeyword() method of {@link TweetLyticsFactory}.
     */
    @Test
    public void testGetTweetsByKeyword() {
        RequestToken requestToken = new RequestToken(OAUTH_TOKEN, OAUTH_TOKEN_SECRET);
        TweetLyticsFactory instance = TweetLyticsFactory.getInstance(requestToken);

        // For valid input.
        CompletableFuture<List<SearchResults>> result = instance.getTweetsByKeyword("COVID-19");
        result.thenRun(() -> {
            try {
                List<SearchResults> rs = result.get();
                assertTrue(rs.size() > 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        // For invalid input.
        CompletableFuture<List<SearchResults>> result2 = instance.getTweetsByKeyword("h8~13/3;249d)931#4$");
        result2.thenRun(() -> {
            try {
                List<SearchResults> rs = result2.get();
                assertFalse(rs.size() > 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * Test the findStatistics() method of {@link TweetLyticsFactory}.
     */
    @Test
    public void testFindStatistics() {

        RequestToken requestToken = new RequestToken(OAUTH_TOKEN, OAUTH_TOKEN_SECRET);
        TweetLyticsFactory instance = TweetLyticsFactory.getInstance(requestToken);

        // For valid input. Test for index 0 only.
        CompletableFuture<List<SearchResults>> rs = instance.getTweetsByKeyword("COVID-19");
        CompletableFuture<Map<String, Long>> result = instance.findStatistics(rs, 0);
        result.thenRun(() -> {
            try {
                Map<String, Long> map = result.get();
                assertTrue(map.size() > 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        // For invalid input. Test for index 0 only.
        CompletableFuture<List<SearchResults>> rs2 = instance.getTweetsByKeyword("h8~13/3;249d)931#4$");
        CompletableFuture<Map<String, Long>> result2 = instance.findStatistics(rs2, 0);
        result.thenRun(() -> {
            try {
                Map<String, Long> map = result2.get();
                assertFalse(map.size() > 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * Test the getUserTweetList() method of {@link TweetLyticsFactory}.
     */
    @Test
    public void testGetUserTweetList() {
        // Cocordia university user handle;
        long user_id = 18173399;

        RequestToken requestToken = new RequestToken(OAUTH_TOKEN, OAUTH_TOKEN_SECRET);
        TweetLyticsFactory instance = TweetLyticsFactory.getInstance(requestToken);

        // For valid input.
        CompletableFuture<List<Tweet>> result = instance.getUserListTweets(user_id);
        result.thenRun(() -> {
            try {
                List<Tweet> rs = result.get();
                assertTrue(rs.size() > 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

    }

}