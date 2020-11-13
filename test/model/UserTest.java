package model;

import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.AssertTrue;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static org.junit.Assert.*;
import static play.test.Helpers.*;
//import static org.fest.assertions.Assertions.*;

/**
 * @author Sai Preetham
 * This class performs testing for {@link User}
 */

public class UserTest {
    User user;
    @Before
    public void setup(){
        long userId = 1234;
        String userName = "hello";
        String userScreenName = "xyz";
        String userProfileImage = "img";
        String userProfileLink = "link";

        user = new User(userId, userName, userScreenName, userProfileImage, userProfileLink);
    }

    /**
     * Test for getUserId
     */
    @Test
    public void testGetUserId(){
        assertEquals(1234, user.getUserId());
    }

    /**
     * test for setUserId
     */
    @Test
    public void testSetUserId(){
        long userId = 123;
        user.setUserId(userId);
        assertEquals(123,user.getUserId());
    }

    /**
     * test for getUserName
     */
    @Test
    public void testGetUserName(){
        assertEquals("hello", user.getUserName());
    }

    /**
     * test for setUserName
     */
    @Test
    public void testSetUserName(){
        String userName = "abc";
        user.setUserName(userName);
        assertEquals("abc",user.getUserName());
    }

    /**
     * test for getUserScreenName
     */
    @Test
    public void testGetUserScreenName(){
        assertEquals("xyz", user.getUserScreenName());
    }

    /**
     * test for setUserScreenName
     */
    @Test
    public void testSetUserScreenName(){
        String userScreenName = "name";
        user.setUserScreenName(userScreenName);
        assertEquals("name",user.getUserScreenName());
    }

    /**
     * test for getUserProfileImage
     */
    @Test
    public void testGetUserProfileImage(){
        assertEquals("img",user.getUserProfileImage());
    }

    /**
     * test for setUserProfileImage
     */
    @Test
    public void testSetUserProfileImage(){
        String userProfileImage = "image";
        user.setUserProfileImage(userProfileImage);
        assertEquals("image",user.getUserProfileImage());
    }

    /**
     * test for getUserProfileLink
     */
    @Test
    public void testGetUserProfileLink(){
        assertEquals("https://www.twitter.com/" +"link",user.getUserProfileLink());
    }

    /**
     * test for setUserProfileLink
     */
    @Test
    public void testSetUserProfileLink(){
        String userProfileLink = "link1";
        user.setUserProfileLink(userProfileLink);
        assertEquals("link1",user.getUserProfileLink());
    }

    /**
     * test for Equals
     */
    @Test
    public void testEquals(){
        User user=new User(123,"hello","xyz","img","link");
        User user2=user;
        assertTrue(user.equals(user2));
        Tweet tweet=new Tweet(user,"text",new Date(),new ArrayList<>());
        assertFalse(user.equals(tweet));
        User user1=new User(123,"hello","xyz","img","link");
        User user3=new User(123,"hello","xyz","img","link");
        assertEquals(user1.equals(user3),false);
        User user4=new User(123,"hello_test","xyz","img","link");
        assertFalse(user.equals(user4));
    }

    /**
     * test for Hash code
     */
    @Test
    public void testHashCode(){
        assertEquals(Objects.hash(user.getUserId(),user.getUserName(),user.getUserProfileImage(),user.getUserProfileLink()),user.hashCode());
    }
}