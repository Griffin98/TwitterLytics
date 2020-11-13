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
     * constructor for {@link UserFactory}
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
     * @param userId            UserId
     * @param userName          UserName
     * @param userScreenName    UserScreenName
     * @param userProfileImage  UserProfileImage
     * @param userProfileLink   UserProfileLink
     * @return  returns user object
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

    /**
     * method to get user by userId
     * @param userId    userId
     * @return returns user object based on userId
     */
    public User getUserById(long userId) {
        User user = userHashMap.get(userId);
        return  user;
    }

}
