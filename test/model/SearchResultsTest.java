package model;
/**
 * @author Darshan on 03-11-2020
 * @project TweetLytics
 */

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * The type Search results test.
 */
public class SearchResultsTest {
    private SearchResults searchResults;
    private SearchResults test;
    private List<Tweet> tweetList;
    private List<Tweet> tweetList2;
    private Tweet tweet;
    private List<String> sentiments;

    /**
     * Set up.
     */
    @Before
    public void setUp(){
        User user = new User(1, "User", "screenName", "userprofile", "useProfile");
        Date date = new Date();
        ArrayList<String> hashTags = new ArrayList<>();
        sentiments = new ArrayList<>();
        sentiments.add(":-)");
        // TODO: implement test cases
        tweet = new Tweet(user, "text", date, hashTags);
        tweet.setTweetSentiment(sentiments);
        tweetList = new ArrayList<>();
        tweetList2 = new ArrayList<>();
        tweetList.add(tweet);
        tweetList2.add(tweet);
        tweetList2.add(new Tweet(user, "text2", date, hashTags));
        searchResults = new SearchResults("today", tweetList, ":-)");
        test = new SearchResults("today", tweetList2, ":-)");
    }

    /**
     * Test get keyword.
     */
    @Test
    public void testGetKeyword(){
        assertEquals("today" ,  searchResults.getKeyword());
        assertEquals(searchResults, searchResults);
        assertNotEquals("1", searchResults);
        assertNotEquals(searchResults, test);
    }

    /**
     * Test get tweets.
     */
    @Test
    public void testGetTweets(){
        assertEquals( tweetList , searchResults.getTweets());
    }

    /**
     * Test set keyword.
     */
    @Test
    public void testSetKeyword(){
        String searchKeyword = "Hello";
        searchResults.setKeyword(searchKeyword);
        assertEquals(searchResults.getKeyword(),searchKeyword);
    }

    /**
     * Test set tweets.
     */
    @Test
    public void testSetTweets(){
        List<Tweet> dummyTweet = new ArrayList<>();
        dummyTweet.add(tweet);
        searchResults.setTweets(dummyTweet);
        assertEquals(dummyTweet, searchResults.getTweets());
    }

    /**
     * Test get overall results.
     */
    @Test
    public void testGetOverallResults(){
        String str = String.join("", tweet.getTweetSentiment());
        assertEquals(searchResults.getOverallResult(), str);
    }

    /**
     * Test set overall results.
     */
    @Test
    public void testSetOverallResults(){
        List<String> sentiment = new ArrayList<>();
        sentiment.add(":-)");
        String str = String.join("", sentiment);
        searchResults.setOverallResult(":-)");
        assertEquals(str, searchResults.getOverallResult());
    }
    /**
     * Test hash code.
     */
    @Test
    public void testHashCode(){
        assertEquals(searchResults.hashCode(),Objects.hash(searchResults.getKeyword(), tweetList));
    }
    @Test
    public void testEquals(){
        SearchResults searchResults3=searchResults;
        assertEquals(searchResults3.equals(searchResults),true);
        assertEquals(searchResults3.equals(new User(1L,"test","test","test","test")),false);
        SearchResults searchResults4 = new SearchResults("today", tweetList, ":-)");
        assertEquals(searchResults4.equals(searchResults),true);
        SearchResults searchResults5 = new SearchResults("today1", tweetList, ":-)");
        assertEquals(searchResults5.equals(searchResults),false);
    }
}
