package factory;

import model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;


public class UserFactoryTest<user> {

    public UserFactory userFactory;
    public HashMap<Long, user> userHashMapTest = new HashMap<>();

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
//        User u1 = userFactory.getOrCreateUser(123, "hello123", "hello123");
//        User u4 = userFactory.getOrCreateUser(456, "hello456", "hello456");
//        User u7 = userFactory.getOrCreateUser(789, "hello789", "hello789");
//
//        User u11 = userFactory.getOrCreateUser(123, "hello123", "hello123");
//        User u44 = userFactory.getOrCreateUser(456, "hello456", "hello456");
//        User u77 = userFactory.getOrCreateUser(789, "hello789", "hello789");
//
//        assertEquals(u1, u11);
//        assertEquals(u4, u44);
//        assertEquals(u7, u77);
    }
    @Test
    public void testGetUserById(long userId1) {
//        User u1 = userFactory.getOrCreateUser(123, "hello123", "hello123");
//        User u11 = userFactory.getUserById(123);
//
//        User u_invalid = userFactory.getUserById(123456789);
//
//        assertEquals(u1, u11);
//        assertNull(u_invalid);



    }
}