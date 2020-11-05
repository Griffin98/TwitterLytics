package factory;

import model.User;

import java.util.HashMap;

/**
 * Model factory to avoid creating already existing user object (Save memory).
 *
 * Singleton Pattern
 * @author Sai Preetham
 */
public class UserFactory {

    private static UserFactory INSTANCE = null;

    private HashMap<Long, User> userHashMap;

    /**
     * this method is used to assign a hashmap list to userHashMap object
     */
    private UserFactory() {
        userHashMap = new HashMap<>();
    }

    /**
    * this method creates a new instance when there is no instance or else it returns an instancs
     */
    public static UserFactory getInstance() {

        if(INSTANCE == null) {
            INSTANCE = new UserFactory();
        }

        return INSTANCE;
    }

    /**
     * This method is used to map the entire user object to userHasMap using the userId
     * If user is null, a new user is created and then assigned it to userHashMap
     * @param userId
     * @param userName
     * @param userScreenName
     * @param userProfileImage
     * @param userProfileLink
     * @return
     */
    public User getOrCreateUser(long userId, String userName, String userScreenName, String userProfileImage,
                                String userProfileLink)  {

        User user = userHashMap.get(userId);

        if(user != null)
            return user;

        user = new User(userId, userName, userScreenName, userProfileImage, userProfileLink);
        userHashMap.put(userId, user);

        return user;
    }

    public User getUserById(long userId) {
        User user = userHashMap.get(userId);
        return  user;
    }

}
