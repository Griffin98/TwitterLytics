package model;
import java.util.Objects;

/**
 * Model to store user information.
 */

public class User {

    private long userId;
    private String userName;
    private String userScreenName;
    private String userProfileImage;
    private String userProfileLink;

    public User(long userId, String userName, String userScreenName, String userProfileImage, String userProfileLink) {
        this.userId = userId;
        this.userName = userName;
        this.userScreenName = userScreenName;
        this.userProfileImage = userProfileImage;
        this.userProfileLink = "https://www.twitter.com/" + userProfileLink;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public String getUserProfileLink() {
        return userProfileLink;
    }

    public void setUserProfileLink(String userProfileLink) {
        this.userProfileLink = userProfileLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if(o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return ( this.userId == user.userId && this.userName == user.userName &&
                this.userProfileImage == user.userProfileImage &&
                this.userProfileLink == user.userProfileLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, userProfileImage, userProfileLink);
    }

    public String getUserScreenName() {
        return userScreenName;
    }

    public void setUserScreenName(String userScreenName) {
        this.userScreenName = userScreenName;
    }
}
