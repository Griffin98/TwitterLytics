package model;/*
  @author Darshan on 03-11-2020
 * @project TweetLytics
 */

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    /**
     * Set up.
     */
    @Before
    public void setUp(){
        User user = new User(1, "User", "screenName", "userprofile", "useProfile");
        Date date = new Date();
        ArrayList<String> hashTags = new ArrayList<>();
        // TODO: implement test cases
        tweet = new Tweet(user, "text", date, hashTags);
        tweetList = new ArrayList<>();
        tweetList2 = new ArrayList<>();
        tweetList.add(tweet);
        tweetList2.add(tweet);
        tweetList2.add(new Tweet(user, "text2", date, hashTags));
        searchResults = new SearchResults("today", tweetList);
        test = new SearchResults("today", tweetList2);
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
     * Test hash code.
     */
    @Test
    public void testHashCode(){
        assertEquals(searchResults.hashCode(),Objects.hash(searchResults.getKeyword(), tweetList));
    }
}
