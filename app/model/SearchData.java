package model;
/*
  @author Darshan on 01-11-2020
 * @project TweetLytics
 */

import play.data.validation.Constraints;

/**
 * Model to store input search key by the user.
 */
public class SearchData {
    @Constraints.Required
    private String searchKey;

    /**
     * Instantiates a new Search data.
     */
    public SearchData() {
    }

    /**
     * Instantiates a new Search data.
     *
     * @param searchKey the search key
     */
    public SearchData(String searchKey) {
        this.searchKey = searchKey;
    }

    /**
     * Gets search key.
     *
     * @return the search key
     */
    public String getSearchKey() {
        return searchKey;
    }

    /**
     * Sets search key.
     *
     * @param searchKey the search key
     */
    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
