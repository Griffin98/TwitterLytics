package model;
/*
  @author Darshan on 01-11-2020
 * @project TweetLytics
 */

import java.util.List;
import java.util.Objects;

/**
 * Model class for maintaining search result for specified keyword.
 */
public class SearchResults {

    private String keyword;
    private List<Tweet> tweets;

    /**
     * Instantiates a new Search results.
     *
     * @param keyword         the keyword
     * @param tweets          the tweets
     */
    public SearchResults(String keyword, List<Tweet> tweets) {
        this.keyword = keyword;
        this.tweets = tweets;
    }

    /**
     * Gets keyword.
     *
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Sets keyword.
     *
     * @param keyword the keyword
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Gets tweets.
     *
     * @return the tweets
     */
    public List<Tweet> getTweets() {
        return tweets;
    }

    /**
     * Sets tweets.
     *
     * @param tweets the tweets
     */
    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyword, tweets);
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return  true;

        if(! (o instanceof SearchResults)) return false;

        SearchResults results = (SearchResults) o;
        return (keyword.equals(results.keyword) && tweets.equals(results.tweets));
    }
}
