package controller;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;

import com.google.common.collect.ImmutableMap;
import model.SearchResults;
import model.SearchResultsMap;
import model.Tweet;
import model.User;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class MainControllerTest extends WithApplication {

    private Application application;
    @Before
    public void init() {
        application = new GuiceApplicationBuilder().build();
    }

    @Test
    public void testIndex() {
        Helpers.running(Helpers.fakeApplication(), () -> {
            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method("GET")
                    .uri("/");

            Result result = Helpers.route(application, request);
            assertEquals(OK, result.status());

        });
    }
    @Test
    public void testMainBadRequest() {
        Helpers.running(Helpers.fakeApplication(), () -> {
            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method("GET")
                    .uri("/home");
            Result result = Helpers.route(application, request);
            assertEquals(400, result.status());
        });
    }
    @Test
    public void testMainOK() {
        List<SearchResults> listSearchResults=new ArrayList<>();
//        User user = new User(100, "test", "test", "test", "test");
//        List<Tweet> listTweets = new ArrayList<>();
//        ArrayList<String> hashTags=new ArrayList<>();
//        hashTags.add("#good");
//        hashTags.add("#bad");
//        Tweet hello_world_tweet = new Tweet(user, "hello world", new Date(), hashTags);
//        listTweets.add(hello_world_tweet);
//        listSearchResults.add(new SearchResults("test search",listTweets,":-|"));
        SearchResultsMap searchResultsMap = SearchResultsMap.getInstance();
        searchResultsMap.addSearchResultsMap("session_id_test",CompletableFuture.completedFuture(listSearchResults));
        Helpers.running(Helpers.fakeApplication(), () -> {
            Map<String,String> sessionMap=new HashMap<>();
            sessionMap.put("token","test");
            sessionMap.put("secret","test");
            sessionMap.put("session_id","session_id_test");
            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method("GET")
                    .session(sessionMap)
                    .uri("/home");
            Result result = Helpers.route(application, request);
            assertEquals(OK, result.status());
        });
        SearchResultsMap instance = SearchResultsMap.getInstance();
        instance=null;
    }
    @Test
    public void TweetWordsWithoutSession() {
        Helpers.running(Helpers.fakeApplication(), () -> {
            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method("GET")
                    .uri("/tweetWords");
            Result result = Helpers.route(application, request);
            assertEquals(400, result.status());
        });
    }
    @Test
    public void TweetWordsIndexOutOfBound() {
        Helpers.running(Helpers.fakeApplication(), () -> {
            Map<String,String> sessionMap=new HashMap<>();
            sessionMap.put("token","test");
            sessionMap.put("secret","test");
            sessionMap.put("session_id","test");
            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method("GET")
                    .session(sessionMap)
                    .uri("/tweetWords?index=1");
            Result result = Helpers.route(application, request);
            assertEquals(400, result.status());
        });
    }
    @Test
    public void TweetWordsOK() {
        List<SearchResults> listSearchResults=new ArrayList<>();
        User user = new User(100, "test", "test", "test", "test");
        List<Tweet> listTweets = new ArrayList<>();
        ArrayList<String> hashTags=new ArrayList<>();
        hashTags.add("#good");
        Tweet hello_world_tweet = new Tweet(user, "hello world", new Date(), hashTags);
        listTweets.add(hello_world_tweet);
        listSearchResults.add(new SearchResults("test search",listTweets,":-)"));
        SearchResultsMap searchResultsMap = SearchResultsMap.getInstance();
        searchResultsMap.addSearchResultsMap("session_id_test",CompletableFuture.completedFuture(listSearchResults));
        Map<String, Object> mapConfigure=new HashMap<>();
        mapConfigure.put("searchCounter",1);
        mapConfigure.put("searchResultsList",CompletableFuture.supplyAsync(()->{
            List<SearchResults> searchResults = new ArrayList<>();
            SearchResults searchResult = new SearchResults("test", new ArrayList<Tweet>(), ";-|");
            searchResults.add(searchResult);
            return searchResults;
        }));
        Helpers.running(Helpers.fakeApplication(), () -> {
            Map<String,String> sessionMap=new HashMap<>();
            sessionMap.put("token","test");
            sessionMap.put("secret","test");
            sessionMap.put("session_id","session_id_test");
            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method("GET")
                    .session(sessionMap)
                    .uri("/tweetWords?index=0");
            Result result = Helpers.route(application, request);
            assertEquals(OK, result.status());
        });
        SearchResultsMap instance = SearchResultsMap.getInstance();
        instance=null;
    }
    @Test
    public void testSearchFails() {
        Helpers.running(Helpers.fakeApplication(), () -> {
            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method("GET")
                    .uri("/search");
            Result result = Helpers.route(application, request);
            assertEquals(400, result.status());
        });
    }
    @Test
    public void testSearchPasses() {
        List<SearchResults> listSearchResults=new ArrayList<>();
        User user = new User(100, "test", "test", "test", "test");
        List<Tweet> listTweets = new ArrayList<>();
        ArrayList<String> hashTags=new ArrayList<>();
        hashTags.add("#good");
        Tweet hello_world_tweet = new Tweet(user, "hello world", new Date(), hashTags);
        listTweets.add(hello_world_tweet);
        listSearchResults.add(new SearchResults("test search",listTweets,":-|"));
        SearchResultsMap searchResultsMap = SearchResultsMap.getInstance();
        searchResultsMap.addSearchResultsMap("session_id_test",CompletableFuture.completedFuture(listSearchResults));
        Helpers.running(Helpers.fakeApplication(), () -> {
            Map<String,String> sessionMap=new HashMap<>();
            sessionMap.put("token","test");
            sessionMap.put("secret","test");
            sessionMap.put("session_id","session_id_test");
            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method("GET")
                    .bodyForm(ImmutableMap.of("searchKey", "SearchedSomething"))
                    .session(sessionMap)
                    .uri("/search");
            Result result = Helpers.route(application, request);
            assertEquals(303, result.status());
        });
        SearchResultsMap instance = SearchResultsMap.getInstance();
        instance=null;
    }
    @Test
    public void testSearchHashTagsFail() {
        Helpers.running(Helpers.fakeApplication(), () -> {
            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method("GET")
                    .uri("/searchHashTags");
            Result result = Helpers.route(application, request);
            assertEquals(400, result.status());

        });
    }
    @Test
    public void testSearchHashTagsPass() {
//        Map<String,String> sessionMap=new HashMap<>();
//        sessionMap.put("token","test");
//        sessionMap.put("secret","test");
//        sessionMap.put("session_id","session_id_test");
//        Helpers.running(Helpers.fakeApplication(), () -> {
//            Http.RequestBuilder request = new Http.RequestBuilder()
//                    .method("GET")
//                    .session(sessionMap)
//                    .uri("/searchHashTags");
//
//            Result result = Helpers.route(application, request);
//            assertEquals(OK, result.status());
//
//        });
    }
    @Test
    public void testUserProfileBadRequest() {
        Helpers.running(Helpers.fakeApplication(), () -> {
            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method("GET")
                    .uri("/userProfile");
            Result result = Helpers.route(application, request);
            assertEquals(400, result.status());

        });
    }
    @Test
    public void testUserProfileOK() {
//        Map<String,String> sessionMap=new HashMap<>();
//        sessionMap.put("token","test");
//        sessionMap.put("secret","test");
//        sessionMap.put("session_id","session_id_test");
//        Helpers.running(Helpers.fakeApplication(), () -> {
//            Http.RequestBuilder request = new Http.RequestBuilder()
//                    .method("GET")
//                    .session(sessionMap)
//                    .uri("/userProfile");
//            Result result = Helpers.route(application, request);
//            assertEquals(OK, result.status());
//        });
    }
}