package factory;

import model.SearchResultsMap;
import model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * @author Sai Preetham
 * This class performs testing for {@link UserFactory}
 */
public class UserFactoryTest {

    public UserFactory userFactory;
    public UserFactory instance = null;
    public HashMap<Long, User> userHashMapTest = new HashMap<>();

    /**
     * this method is to setup UserFactory parameters
     */
    @Before
    public void setUp(){
        long userId = 987;
        String userName = "bcd";
        String userScreenName = "qwerty";
        String userProfileImage = "image97";
        String userProfileLink = "link12";
        User user = new User(userId, userName, userScreenName, userProfileImage, userProfileLink);
    }

    /**
     * test for getInstance()
     */
    @Test
    public void testGetInstance() {
        if (instance==null) {
            instance = new UserFactory();
        }

        assertEquals(instance, UserFactory.getInstance());
    }


    /**
     * Test that we correctly implement singleton pattern for {@link UserFactory}.
     */
    @Test
    public void TestSingleton() {
        UserFactory instance1 = UserFactory.getInstance();

        // Check if we get correct instance
        assertTrue(instance1 instanceof UserFactory);

        // Check if we get same instance, singleton pattern.
        UserFactory instance2 = UserFactory.getInstance();
        assertEquals(instance1, instance2);
        instance1=null;
        instance2=null;
    }

    /**
     * test for getOrCreateUser()
     */
    @Test
    public void testGetOrCreateUser() {

    }

    /**
     * test for getUserById
     */
    @Test
    public void testGetUserById(){
       long userId = 1234;
       User user = userHashMapTest.get(userId);
       assertEquals(user,userHashMapTest.get(userId));
    }

}