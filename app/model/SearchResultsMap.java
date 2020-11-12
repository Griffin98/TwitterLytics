package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Manoj
 * This class is used to store data for {@link controllers.MainController}
 * To store cache data of search data for every session using {@link CompletableFuture}
 */
public class SearchResultsMap {
    private static SearchResultsMap INSTANCE = null;
    private Map<String, CompletableFuture<List<SearchResults>>> searchResultsMap;
    private Map<String,Integer> searchResultsCount;

    /**
     * Constructor for {@link SearchResultsMap}
     */
    private SearchResultsMap(){
        searchResultsMap=new HashMap<>();
        searchResultsCount=new HashMap<>();
    }

    /**
     * Method to get single instance of class {@link SearchResultsMap}
     * @return Singleton instance of {@link SearchResultsMap}
     */
    public static SearchResultsMap getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SearchResultsMap();
        }
        return INSTANCE;
    }

    /**
     * Method to get map of session_id and CompletableFuture List of {@link SearchResults}
     * @return map of session_id and CompletableFuture List of {@link SearchResults}
     */
    public Map<String, CompletableFuture<List<SearchResults>>> getSearchResultsMap() {
        return searchResultsMap;
    }

    /**
     * Method to set map of session_id and CompletableFuture List of {@link SearchResults}
     * @param searchResultsMap map of session_id and CompletableFuture List of {@link SearchResults}
     */
    public void setSearchResultsMap(Map<String, CompletableFuture<List<SearchResults>>> searchResultsMap) {
        this.searchResultsMap = searchResultsMap;
    }

    /**
     * Method to add a session_id and CompletableFuture List of {@link SearchResults}
     * @param session_id Session Id that is to be stored
     * @param listSearchResults CompletableFuture List of {@link SearchResults}
     */
    public void addSearchResultsMap(String session_id,CompletableFuture<List<SearchResults>> listSearchResults){
        searchResultsMap.put(session_id,listSearchResults);
        if(!searchResultsCount.containsKey(session_id)){
            searchResultsCount.put(session_id,0);
        }
        else {
            searchResultsCount.put(session_id, searchResultsCount.get(session_id) + 1);
        }
    }

    /**
     * To get number of searched done in a session
     * @param session_id session id for which the count is to be found
     * @return number of searches made
     */
    public Integer getListSearchResultsCount(String session_id){
        if(!searchResultsCount.containsKey(session_id)){
            return 0;
        }
        return searchResultsCount.get(session_id);
    }
}
