package model;

import factory.TweetLyticsFactory;
import org.junit.Test;
import play.libs.oauth.OAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SearchResultsMapTest {
    @Test
    public void testSingleton() {
        SearchResultsMap instance1 = SearchResultsMap.getInstance();

        // Check if we get correct instance
        assertTrue(instance1 instanceof SearchResultsMap);

        // Check if we get same instance, singleton pattern.
        SearchResultsMap instance2 = SearchResultsMap.getInstance();
        assertEquals(instance1, instance2);
        instance1=null;
        instance2=null;
    }
    @Test
    public void testGetSearchResultsMap(){
        SearchResultsMap instance = SearchResultsMap.getInstance();
        assertEquals(instance.getSearchResultsMap(), new HashMap<String, CompletableFuture<List<SearchResults>>>());
    }
    @Test
    public void testSetSearchResultsMap(){
        SearchResultsMap instance = SearchResultsMap.getInstance();
        Map<String, CompletableFuture<List<SearchResults>>> searchResultsMap=new HashMap<>();
        searchResultsMap.put("test_session",CompletableFuture.supplyAsync(()->new ArrayList<>()));
        instance.setSearchResultsMap(searchResultsMap);
        assertEquals(instance.getSearchResultsMap(), searchResultsMap);
    }
    @Test
    public void testAddSearchResultsMap(){
        SearchResultsMap instance = SearchResultsMap.getInstance();
        Map<String, CompletableFuture<List<SearchResults>>> searchResultsMap=new HashMap<>();
        CompletableFuture<List<SearchResults>> arrayListCompletableFuture = CompletableFuture.supplyAsync(() -> new ArrayList<>());
        searchResultsMap.put("test_session",arrayListCompletableFuture);
        instance.addSearchResultsMap("test_session",arrayListCompletableFuture);
        assertEquals(instance.getSearchResultsMap(), searchResultsMap);
    }
    @Test
    public void testGetListSearchResultsCount(){
        SearchResultsMap instance = SearchResultsMap.getInstance();
        Map<String, CompletableFuture<List<SearchResults>>> searchResultsMap=new HashMap<>();
        searchResultsMap.put("test_session",CompletableFuture.supplyAsync(()->new ArrayList<>()));
        instance.setSearchResultsMap(searchResultsMap);
        Integer res=0;
        assertEquals(instance.getListSearchResultsCount("test_session"), res);
    }
}
