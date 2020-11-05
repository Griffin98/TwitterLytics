package model;
/*
  @author Darshan on 03-11-2020
 * @project TweetLytics
 */

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * The type Search data test.
 */
public class SearchDataTest {
    private SearchData searchData;
    private String searchKey;

    /**
     * Set up.
     */
    @Before
    public void setUp(){
        searchKey = "Hello";
        searchData =new SearchData(searchKey);
    }

    /**
     * Test search data object created.
     */
    @Test
    public void testSearchDataObjectCreated(){
        SearchData i = new SearchData();
        assertNotNull(i);
    }

    /**
     * Test get search key.
     */
    @Test
    public void testGetSearchKey(){
        assertEquals("Hello", searchData.getSearchKey());
    }

    /**
     * Test set search key.
     */
    @Test
    public void testSetSearchKey(){
        String searchKey = "covid19";
        searchData.setSearchKey(searchKey);
        assertEquals(searchData.getSearchKey(), searchKey);
    }
}
