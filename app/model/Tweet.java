package model;
/*
  @author Darshan on 01-11-2020
 * @project TweetLytics
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * Model class to store tweets.
 */
public class Tweet {

    private User user;
    private String text;
    private ArrayList<String> hashTags;
    private Date creationTime;

    /**
     * Instantiates a new Tweet.
     *
     * @param user         the user
     * @param text         the text
     * @param creationTime the creation time
     */
    public Tweet(User user, String text, Date creationTime, ArrayList<String> hashTags) {
        this.user = user;
        this.text = text;
        this.creationTime = creationTime;
        this.hashTags = hashTags;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets text.
     *
     * @param text the text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets creation time.
     *
     * @return the creation time
     */
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * Sets creation time.
     *
     * @param creationTime the creation time
     */
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if(o == null || o.getClass() != getClass()) return false;
        Tweet tweet = (Tweet) o;
        return (tweet.user.equals(this.user) && tweet.text.equals(this.text) &&
                tweet.creationTime.equals(this.creationTime));
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, text, creationTime);
    }

    public ArrayList<String> getHashTags() {
        return hashTags;
    }

    public void setHashTags(ArrayList<String> hashTags) {
        this.hashTags = hashTags;
    }
}
