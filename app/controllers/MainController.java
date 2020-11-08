package controllers;

import factory.TweetLyticsFactory;
import factory.UserFactory;
import model.SearchData;
import model.SearchResults;
import model.Tweet;
import model.User;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.libs.oauth.OAuth;
import play.libs.oauth.OAuth.*;
import static play.libs.Scala.asScala;
import play.mvc.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class MainController extends Controller {

    static final ConsumerKey KEY = new ConsumerKey(TweetLyticsFactory.API_KEY, TweetLyticsFactory.API_KEY_SECRET);

    private static final ServiceInfo SERVICE_INFO =
            new ServiceInfo(
                    "https://api.twitter.com/oauth/request_token",
                    "https://api.twitter.com/oauth/access_token",
                    "https://api.twitter.com/oauth/authorize",
                    KEY);

    private static final OAuth TWITTER = new OAuth(SERVICE_INFO);

    private final AssetsFinder assetsFinder;
    private final Form<SearchData> form;
    private final MessagesApi messagesApi;
    private Integer searchCounter;

    private TweetLyticsFactory tweetLyticsFactory;

    private CompletableFuture<List<SearchResults>> searchResultsList;
    @Inject
    public MainController(AssetsFinder assetsFinder, FormFactory formFactory, MessagesApi messagesApi) {
        this.assetsFinder = assetsFinder;
        this.form = formFactory.form(SearchData.class);
        this.messagesApi = messagesApi;
        this.searchResultsList = CompletableFuture.supplyAsync(() -> new ArrayList<SearchResults>());
        searchCounter=0;
    }

    /**
     * An action that renders an HTML page with a home message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public CompletableFuture<Result> index() {
        return CompletableFuture.supplyAsync(()->ok(views.html.index.render(assetsFinder)));
    }

    /**
     *
     * @param request
     * @return
     */

    public CompletableFuture<Result> main(Http.Request request) {
        Optional<RequestToken> sessionTokenPair = getSessionTokenPair(request);
        if(!sessionTokenPair.isPresent()){
            return CompletableFuture.supplyAsync(()->badRequest(views.html.errorView.render("Invalid session",assetsFinder)));
        }
        return searchResultsList.thenApplyAsync(results ->
                ok(views.html.home.render(form, asScala(results), assetsFinder,request, messagesApi.preferred(request))
        ));

    }

    public CompletionStage<Result> search(Http.Request request) {

        if(searchCounter>=10){
            return CompletableFuture.completedFuture(redirect(routes.MainController.main()));
        }

        Optional<RequestToken> sessionTokenPair = getSessionTokenPair(request);
        if(!sessionTokenPair.isPresent()){
            return CompletableFuture.supplyAsync(()->badRequest(views.html.errorView.render("Invalid session",assetsFinder)));
        }

        tweetLyticsFactory = TweetLyticsFactory.getInstance(sessionTokenPair.get());

        final Form<SearchData> boundForm = form.bindFromRequest(request);
        final SearchData data = boundForm.get();
        String searchKey = data.getSearchKey();

        CompletableFuture<List<SearchResults>> result = tweetLyticsFactory.getTweetsByKeyword(searchKey);
        searchResultsList = result.thenCombine(searchResultsList, (op1,op2) -> {
           op1.addAll(op2);
           return op1;
        });
        searchCounter+=1;
        return CompletableFuture.completedFuture(redirect(routes.MainController.main()));
    }
    public CompletionStage<Result> tweetWords(Integer index,Http.Request request) {
        Optional<RequestToken> sessionTokenPair = getSessionTokenPair(request);
        if(!sessionTokenPair.isPresent()){
            return CompletableFuture.supplyAsync(()->badRequest(views.html.errorView.render("Invalid session",assetsFinder)));
        }

        tweetLyticsFactory = TweetLyticsFactory.getInstance(sessionTokenPair.get());

        if(index>searchCounter){
            return CompletableFuture.supplyAsync(()->badRequest(views.html.errorView.render("Please provide a valid value",assetsFinder)));
        }
        CompletableFuture<Map<String, Long>> futureStatistics= tweetLyticsFactory.findStatistics(searchResultsList,index);
        return futureStatistics.thenApplyAsync(statistics->ok(views.html.tweetWords.render(statistics,assetsFinder)));
    }

    public CompletionStage<Result> searchHashTags(String tag, Http.Request request) {
        Optional<RequestToken> sessionTokenPair = getSessionTokenPair(request);

        if(!sessionTokenPair.isPresent()){
            return CompletableFuture.supplyAsync(()->badRequest(views.html.errorView.render("Invalid session",assetsFinder)));
        }

        tweetLyticsFactory = TweetLyticsFactory.getInstance(sessionTokenPair.get());

        CompletableFuture<List<SearchResults>> results = tweetLyticsFactory.getTweetsByKeyword(tag);
        return results.thenApplyAsync(result ->

                ok(views.html.hashTags.render(asScala(result), assetsFinder,request, messagesApi.preferred(request))
                ));
    }


    public CompletionStage<Result> userProfile(Long userID, Http.Request request) {

        Optional<RequestToken> sessionTokenPair = getSessionTokenPair(request);

        if(!sessionTokenPair.isPresent()){
            return CompletableFuture.supplyAsync(()->badRequest(views.html.errorView.render("Invalid session",assetsFinder)));
        }

        tweetLyticsFactory = TweetLyticsFactory.getInstance(sessionTokenPair.get());

        CompletableFuture<List<Tweet>> futureUserHomeLine= tweetLyticsFactory.getUserListTweets(userID);
        User user = UserFactory.getInstance().getUserById(userID);
        return futureUserHomeLine.thenApplyAsync(userHomeLine ->ok(views.html.userProfile.render(user, asScala(userHomeLine),assetsFinder)));
    }

    /**
     *
     * @param request
     * @return
     */
    public Result auth(Http.Request request) {

        Optional<String> verifier = request.queryString("oauth_verifier");
        Result result =
                verifier
                .filter(s -> !s.isEmpty())
                .map(
                        s-> {
                            RequestToken requestToken = getSessionTokenPair(request).get();
                            RequestToken accessToken = TWITTER.retrieveAccessToken(requestToken, s);
                            return redirect(routes.MainController.main())
                                    .addingToSession(request, "token", accessToken.token)
                                    .addingToSession(request, "secret", accessToken.secret);
                        })
                .orElseGet(
                        () -> {
                            String url = routes.MainController.auth().absoluteURL(request);
                            RequestToken requestToken = TWITTER.retrieveRequestToken(url);
                            return redirect(TWITTER.redirectUrl(requestToken.token))
                                    .addingToSession(request, "token", requestToken.token)
                                    .addingToSession(request, "secret", requestToken.secret);
                        });

        return result;
    }


    /**
     *
     * @param request
     * @return
     */
    public Optional<RequestToken> getSessionTokenPair(Http.Request request) {
        return request
                .session()
                .get("token")
                .map(token -> new RequestToken(token, request.session().get("secret").get()));
    }

}
