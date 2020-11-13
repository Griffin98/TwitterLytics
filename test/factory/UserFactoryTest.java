package factory;

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

    private UserFactory instance;
    private User user1, user2, user3;

    @Before
    public void setUp() {
        instance = UserFactory.getInstance();
        user1 = instance.getOrCreateUser(999, "abc", "qwerty", "image97", "link12");
        user2 = instance.getOrCreateUser(999, "abc", "qwerty", "image97", "link12");
        user3 = instance.getOrCreateUser(888, "def", "qwerty", "image98", "link15");
    }

    /**
     * Test that we correctly implement singleton pattern for {@link UserFactory}.
     */
    @Test
    public void testSingleton() {
        UserFactory instance1 = UserFactory.getInstance();

        // Check if we get correct instance
        assertTrue(instance1 instanceof UserFactory);

        // Check if we get same instance, singleton pattern.
        UserFactory instance2 = UserFactory.getInstance();
        assertEquals(instance1, instance2);
    }

    /**
     * Test that we correctly implement getOrCreateUser() method for {@link UserFactory}.
     */
    @Test
    public void testGetOrCreateUser() {

        // For new user;
        assertTrue(user1 != null);

        // For already existing user, we need to same user object;
        assertTrue(user2 != null);
        assertEquals(user1, user2);

        // Assert third object;
        assertTrue(user3 != null);
        assertNotEquals(user1, user3);
        assertNotEquals(user2, user3);
    }

    /**
     * Test that we correctly implement getUserById() method for {@link UserFactory}
     */
    @Test
    public void testGetUserById() {
        User user = instance.getUserById(999);
        assertEquals(user1, user);
        assertEquals(user2, user);

        User nuser = instance.getUserById(888);
        assertEquals(user3, nuser);
    }
}