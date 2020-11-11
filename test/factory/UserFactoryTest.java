package factory;

import model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;


public class UserFactoryTest {

    public UserFactory userFactory;
    public HashMap<Long, User> userHashMapTest = new HashMap<>();

    @Before
    public void setUp(){
        long userId = 987;
        String userName = "bcd";
        String userScreenName = "qwerty";
        String userProfileImage = "image97";
        String userProfileLink = "link12";
        User user = new User(userId, userName, userScreenName, userProfileImage, userProfileLink);
    }

    @Test
    public void testGetInstance() {
    }

    /**
     * test singleton
     */
    @Test
    public void TestSingleton() {
        UserFactory f1 = UserFactory.getInstance();
        UserFactory f2 = UserFactory.getInstance();
        assertEquals(f1, f2);
    }
    
    @Test
    public void testGetOrCreateUser() {

    }



}