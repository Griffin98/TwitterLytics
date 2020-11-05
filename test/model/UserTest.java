package model;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.*;
//import static org.fest.assertions.Assertions.*;


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
    @Test
    public void testGetUserId(){
        assertEquals(1234, user.getUserId());
    }
    @Test
    public void testSetUserId(){
        long userId = 123;
        user.setUserId(userId);
        assertEquals(123,user.getUserId());
    }
    @Test
    public void testGetUserName(){
        assertEquals("hello", user.getUserName());
    }
    @Test
    public void testSetUserName(){
        String userName = "abc";
        user.setUserName(userName);
        assertEquals("abc",user.getUserName());
    }
    @Test
    public void testGetUserScreenName(){
        assertEquals("xyz", user.getUserScreenName());
    }
    @Test
    public void testSetUserScreenName(){
        String userScreenName = "name";
        user.setUserScreenName(userScreenName);
        assertEquals("name",user.getUserScreenName());
    }
    @Test
    public void testGetUserProfileImage(){
        assertEquals("img",user.getUserProfileImage());
    }
    @Test
    public void testSetUserProfileImage(){
        String userProfileImage = "image";
        user.setUserProfileImage(userProfileImage);
        assertEquals("image",user.getUserProfileImage());
    }
    @Test
    public void testGetUserProfileLink(){
        assertEquals("https://www.twitter.com/" +"link",user.getUserProfileLink());
    }
    @Test
    public void testSetUserProfileLink(){
        String userProfileLink = "link1";
        user.setUserProfileLink(userProfileLink);
        assertEquals("link1",user.getUserProfileLink());
    }
    @Test
    public void testEquals(){

    }
//    @Test
//    public int testHashCode(){
//        return Objects.hash(user.getUserId(),user.getUserName(),user.getUserProfileImage(),user.getUserProfileLink());
//    }


}