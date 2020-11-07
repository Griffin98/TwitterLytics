package model;/*
  @author Darshan on 03-11-2020
 * @project TweetLytics
 */
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

/**
 * The type Tweet test.
 */
public class TweetTest {
    /**
     * The Tweet.
     */
    Tweet tweet;
    /**
     * The Date.
     */
    Date date;
    /**
     * The Text tweet.
     */
    String textTweet;

    ArrayList<String> hashTags;

    /**
     * Set up.
     */
    @Before
    public void setUp(){
        date = new Date();
        // TODO: implement test cases;
        hashTags = new ArrayList<>();
        textTweet = "My name is Darshan. This is my first tweet";
        tweet = new Tweet(new User(1, "User", "screenName", "userprofile", "useProfile"), textTweet, date, hashTags);
    }

    /**
     * Test get user.
     */
    @Test
    public void testGetUser(){
        assertEquals("User", tweet.getUser().getUserName());
    }

    /**
     * Test set user.
     */
    @Test
    public void testSetUser(){
        User newUser = new User(10, "ABC","screenName", "userprofile", "useProfile");
        tweet.setUser(newUser);
        assertEquals(newUser, tweet.getUser());
    }

    /**
     * Test get text.
     */
    @Test
    public void testGetText(){
        assertEquals(textTweet, tweet.getText());
    }

    /**
     * Test set text.
     */
    @Test
    public void testSetText(){
        textTweet = "Modified Tweet";
        tweet.setText(textTweet);
        assertEquals(textTweet, tweet.getText());
    }

    /**
     * Test get creation time.
     */
    @Test
    public void testGetCreationTime(){
        assertEquals(date, tweet.getCreationTime());
    }

    /**
     * Test set creation time.
     */
    @Test
    public void testSetCreationTime(){
        Date modDate = new Date();
        tweet.setCreationTime(modDate);
        assertEquals(modDate, tweet.getCreationTime());
    }

    @Test
    public void testGetHashTag(){
        assertEquals(hashTags, tweet.getHashTags());
    }
    @Test
    public void testSetHashTag(){
        ArrayList<String> dummyHashTag = new ArrayList<>();
        tweet.setHashTags(dummyHashTag);
        assertEquals(hashTags, tweet.getHashTags());
    }
    /**
     * Test hash code.
     */
    @Test
    public void testHashCode(){
        assertEquals(tweet.hashCode(),Objects.hash(tweet.getUser(), tweet.getText(), tweet.getCreationTime()));
    }

}

