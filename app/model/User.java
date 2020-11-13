package model;
import java.util.Objects;

/**
 * @author Sai Preetham
 * Model to store user information.
 */

public class User {

    private long userId;
    private String userName;
    private String userScreenName;
    private String userProfileImage;
    private String userProfileLink;

    /**
     * constructor for {@link User}
     * @param userId            user ID
     * @param userName          user Name
     * @param userScreenName    user Screen Name
     * @param userProfileImage  user Profile Image
     * @param userProfileLink   user Profile Link
     */
    public User(long userId, String userName, String userScreenName, String userProfileImage, String userProfileLink) {
        this.userId = userId;
        this.userName = userName;
        this.userScreenName = userScreenName;
        this.userProfileImage = userProfileImage;
        this.userProfileLink = "https://www.twitter.com/" + userProfileLink;
    }

    /**
     * method to get the value of user id
     * @return returns userId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * method to set the user id
     * @param userId  assigns value to the parameter userId
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * method to get the value of user name
     * @return returns userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * method to set the user name
     * @param userName assigns name to parameter userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * method to get the value of user Profile Image
     * @return returns userProfileImage
     */
    public String getUserProfileImage() {
        return userProfileImage;
    }

    /**
     * method to set user Profile Image
     * @param userProfileImage assigns profileImage to the parameter userProfileImage
     */
    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    /**
     * method to get the value of user profile link
     * @return returns userProfileLink
     */
    public String getUserProfileLink() {
        return userProfileLink;
    }

    /**
     * method to set profile link
     * @param userProfileLink  assigns Profile link to the parameter userProfileLink
     */
    public void setUserProfileLink(String userProfileLink) {
        this.userProfileLink = userProfileLink;
    }

    /**
     * equals method
     * @param o Object
     * @return returns userId, userName, userProfileImage, userProfileLink
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if(o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return ( this.userId == user.userId && this.userName == user.userName &&
                this.userProfileImage == user.userProfileImage &&
                this.userProfileLink == user.userProfileLink);
    }

    /**
     * Hashcode method
     * @return returns hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, userProfileImage, userProfileLink);
    }

    /**
     * method to get the value of user screen name
     * @return returns userScreenName
     */
    public String getUserScreenName() {
        return userScreenName;
    }

    /**
     * method to set Screen name
     * @param userScreenName  assigns screenName to the parameter userScreenName
     */
    public void setUserScreenName(String userScreenName) {
        this.userScreenName = userScreenName;
    }
}
