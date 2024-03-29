package model;
import factory.TweetLyticsFactory;
import model.SearchResults;
import model.Tweet;
import model.User;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.oauth.OAuth.RequestToken;
import play.test.WithApplication;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * @author Manoj
 * To test TweetWords class
 */
public class TweetWordsTest extends WithApplication {

    /**
     * To test the map(storing word:frequency) returned by TestFindStatistics() method
     */
    @Test
    public void TestFindStatistics(){
        RequestToken requestToken = new RequestToken("","");
        TweetLyticsFactory tweetLyticsFactory = TweetLyticsFactory.getInstance(requestToken);
        CompletableFuture<List<SearchResults>> arrayListCompletableFuture = CompletableFuture.supplyAsync(() ->{
            ArrayList<SearchResults> searchResultsArrayList = new ArrayList<>();
            // TODO: implement test cases;
            ArrayList<String> hashTags = new ArrayList<>();
            User user = new User(111, "test", "test","test","test");
            Tweet tweet=new Tweet(user,"test the application",new Date(), hashTags);
            User user1 = new User(110, "test1", "test1","test1","test1");
            Tweet tweet1=new Tweet(user1,"test the application with this testcase test it well",new Date(), hashTags);
            List<Tweet> listTweet=new ArrayList<>();
            listTweet.add(tweet);
            listTweet.add(tweet1);
            searchResultsArrayList.add(new SearchResults("random search",listTweet, ":-|"));
            return searchResultsArrayList;
        } );
        Map<String, Long> expectedMap1=new HashMap<>();
        expectedMap1.put("test",3L);
        expectedMap1.put("the",2L);
        expectedMap1.put("application",2L);
        expectedMap1.put("with",1L);
        expectedMap1.put("this",1L);
        expectedMap1.put("testcase",1L);
        expectedMap1.put("it",1L);
        expectedMap1.put("well",1L);
        try {
            assertEquals(expectedMap1,tweetLyticsFactory.findStatistics(arrayListCompletableFuture,0).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
