package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 *
 */
public class SearchResultsMap {
    private static SearchResultsMap INSTANCE = null;
    private Map<String, CompletableFuture<List<SearchResults>>> searchResultsMap;
    private Map<String,Integer> searchResultsCount;
    private SearchResultsMap(){
        searchResultsMap=new HashMap<>();
        searchResultsCount=new HashMap<>();
    }
    public static SearchResultsMap getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SearchResultsMap();
        }
        return INSTANCE;
    }
    public Map<String, CompletableFuture<List<SearchResults>>> getSearchResultsMap() {
        return searchResultsMap;
    }
    public void setSearchResultsMap(Map<String, CompletableFuture<List<SearchResults>>> searchResultsMap) {
        this.searchResultsMap = searchResultsMap;
    }
    public void addSearchResultsMap(String session_id,CompletableFuture<List<SearchResults>> listSearchResults){
        searchResultsMap.put(session_id,listSearchResults);
        if(!searchResultsCount.containsKey(session_id)){
            searchResultsCount.put(session_id,0);
        }
        else {
            searchResultsCount.put(session_id, searchResultsCount.get(session_id) + 1);
        }
    }
    public Integer getListSearchResultsCount(String session_id){
        if(!searchResultsCount.containsKey(session_id)){
            return 0;
        }
        return searchResultsCount.get(session_id);
    }
}
