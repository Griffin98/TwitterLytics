package controllers;

import actors.HashtagActor;
import actors.Message;
import actors.TwitterActor;
import actors.UserActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import akka.util.Timeout;

import factory.TweetLyticsFactory;
import factory.UserFactory;
import model.*;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.libs.F;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.oauth.OAuth;
import play.libs.streams.ActorFlow;
import play.mvc.*;
import views.html.*;

import javax.inject.Inject;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;
import static play.libs.Scala.asScala;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class MainController extends Controller {

	play.Logger.ALogger logger = play.Logger.of(getClass());

	private ActorSystem actorSystem;
	@Inject private Materializer materializer;
	@Inject private HttpExecutionContext httpExecutionContext;
	private final Timeout timeout = new Timeout(2, TimeUnit.SECONDS);

	static final OAuth.ConsumerKey KEY = new OAuth.ConsumerKey(TweetLyticsFactory.API_KEY, TweetLyticsFactory.API_KEY_SECRET);

	private static int SESSION_ID = 0;

	private static final OAuth.ServiceInfo SERVICE_INFO =
			new OAuth.ServiceInfo(
					"https://api.twitter.com/oauth/request_token",
					"https://api.twitter.com/oauth/access_token",
					"https://api.twitter.com/oauth/authorize",
					KEY);
	private static final OAuth TWITTER = new OAuth(SERVICE_INFO);
	private final AssetsFinder assetsFinder;
	private final Form<SearchData> form;
	private final MessagesApi messagesApi;
	SearchResultsMap searchResultsMap=SearchResultsMap.getInstance();
	private TweetLyticsFactory tweetLyticsFactory;

	private ActorRef twitterActor;


	@Inject
	public MainController(ActorSystem system, AssetsFinder assetsFinder, FormFactory formFactory, MessagesApi messagesApi) {
		this.actorSystem = system;
		this.assetsFinder = assetsFinder;
		this.form = formFactory.form(SearchData.class);
		this.messagesApi = messagesApi;

		OAuth.RequestToken tempToken = new OAuth.RequestToken("804992947015929857-DhDb94zyLSUkZLkXBprs48w9diAMPy9",
				"t9LOxQbuAuAN0R4rw2Xq7KKZpXw54cECifvxDzAXBD0EM");
		twitterActor = actorSystem.actorOf(TwitterActor.getProps(tempToken), "twitterActor");

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
	 * @param request a http request
	 * @return search results
	 */
	public CompletableFuture<Result> main(Http.Request request) {
		Optional<OAuth.RequestToken> sessionTokenPair = getSessionTokenPair(request);
		Optional<String> sessionId = request.session().get("session_id");
		if(!sessionTokenPair.isPresent() || !sessionId.isPresent()){
			return CompletableFuture.supplyAsync(()->badRequest(views.html.errorView.render("Invalid session",assetsFinder)));
		}
		return searchResultsMap.getSearchResultsMap().get(sessionId.get()).thenApplyAsync(results ->
				ok(views.html.home.render(form, asScala(results), assetsFinder,request, messagesApi.preferred(request))
				));
	}

	/**
	 * method for search
	 * @param request http request
	 * @return
	 */
	public CompletionStage<Result> search(Http.Request request) {
		Optional<OAuth.RequestToken> sessionTokenPair = getSessionTokenPair(request);
		Optional<String> sessionId = request.session().get("session_id");
		if(!sessionTokenPair.isPresent() || !sessionId.isPresent()){
			return CompletableFuture.supplyAsync(()->badRequest(views.html.errorView.render("Invalid session",assetsFinder)));
		}
		if(searchResultsMap.getListSearchResultsCount(sessionId.get())>=10) {
			return CompletableFuture.completedFuture(redirect(routes.MainController.main()));
		}
		tweetLyticsFactory = TweetLyticsFactory.getInstance(sessionTokenPair.get());
		final Form<SearchData> boundForm = form.bindFromRequest(request);
		final SearchData data = boundForm.get();
		String searchKey = data.getSearchKey();

		CompletableFuture<List<SearchResults>> oldResult = searchResultsMap.getSearchResultsMap().get(sessionId.get());

		try {
			Message.Keyword keyword = new Message.Keyword(searchKey,sessionId.get(), Message.TYPE.KEYWORD);
			CompletionStage<Object> rs = ask(twitterActor, keyword, timeout);
			CompletableFuture<CompletableFuture<List<SearchResults>>> tmp = rs
					.thenApply(objects -> (CompletableFuture<List<SearchResults>>)objects)
					.toCompletableFuture();

			CompletableFuture<List<SearchResults>> newResult = tmp.get();

			searchResultsMap.addSearchResultsMap(sessionId.get(), newResult.thenCombine(oldResult, (op1,op2) -> {
				op1.addAll(op2);
				return op1;
			}));
			return CompletableFuture.completedFuture(redirect(routes.MainController.main()));

		} catch (Exception ex) {
			logger.error(ex.getLocalizedMessage());
			return CompletableFuture.completedFuture(redirect(routes.MainController.main()));
		}

	}

	/**
	 * method for TweetWords
	 * @param index  index
	 * @param request   http request
	 * @return
	 */
	public CompletionStage<Result> tweetWords(Integer index,Http.Request request) {
		Optional<OAuth.RequestToken> sessionTokenPair = getSessionTokenPair(request);
		Optional<String> sessionId = request.session().get("session_id");
		if(!sessionTokenPair.isPresent() || !sessionId.isPresent()) {
			return CompletableFuture.supplyAsync(() -> badRequest(views.html.errorView.render("Invalid session", assetsFinder)));
		}
		if(index>=searchResultsMap.getListSearchResultsCount(sessionId.get()) || index<0){
			return CompletableFuture.supplyAsync(()->badRequest(views.html.errorView.render("Please provide a valid value",assetsFinder)));
		}
		Message.FindStatistics findStatistics = new Message.FindStatistics(sessionId.get(),index);
		CompletionStage<Object> rs = ask(twitterActor, findStatistics, timeout);
		CompletableFuture<Map<String, Long>> futureStatistics = rs
				.thenCompose(objects -> (CompletableFuture<Map<String, Long>>) objects)
				.toCompletableFuture();
//		tweetLyticsFactory = TweetLyticsFactory.getInstance(sessionTokenPair.get());
//		CompletableFuture<Map<String, Long>> futureStatistics= tweetLyticsFactory.findStatistics(searchResultsMap.getSearchResultsMap().get(sessionId.get()),index);
		return futureStatistics.thenApplyAsync(statistics->ok(views.html.tweetWords.render(statistics,assetsFinder)));
	}

	/**
	 * Search hashTag method
	 * @param tag      tag
	 * @param request  http request
	 * @return
	 */
	public CompletionStage<Result> searchHashTags(String tag, Http.Request request) {
		Optional<OAuth.RequestToken> sessionTokenPair = getSessionTokenPair(request);
		Optional<String> sessionId = request.session().get("session_id");
		if(!sessionTokenPair.isPresent() || !sessionId.isPresent()){
			return CompletableFuture.supplyAsync(()->badRequest(views.html.errorView.render("Invalid session",assetsFinder)));
		}
		Message.Keyword keyword = new Message.Keyword(tag,sessionId.get(), Message.TYPE.HASHTAG);
		CompletionStage<Object> rs = ask(twitterActor, keyword, timeout);
		CompletableFuture<CompletableFuture<List<SearchResults>>> tmp = rs
				.thenApply(objects -> (CompletableFuture<List<SearchResults>>)objects)
				.toCompletableFuture();

		CompletableFuture<List<SearchResults>> results = null;
		try {
			results = tmp.get();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}

		return results.thenApplyAsync(result ->
				ok(views.html.hashTags.render(asScala(result), assetsFinder,request, messagesApi.preferred(request))
				));
	}

	/**
	 * UserProfile Method
	 *
	 * @param userID    userId
	 * @param request   http request
	 * @return
	 */
	public CompletionStage<Result> userProfile(Long userID, Http.Request request) {
		Optional<OAuth.RequestToken> sessionTokenPair = getSessionTokenPair(request);
		if(!sessionTokenPair.isPresent()){
			return CompletableFuture.supplyAsync(()->badRequest(views.html.errorView.render("Invalid session",assetsFinder)));
		}
		tweetLyticsFactory = TweetLyticsFactory.getInstance(sessionTokenPair.get());
		CompletableFuture<List<Tweet>> futureUserHomeLine= tweetLyticsFactory.getUserListTweets(userID);
		User user = UserFactory.getInstance().getUserById(userID);
		return futureUserHomeLine.thenApplyAsync(userHomeLine ->ok(views.html.userProfile.render(user, asScala(userHomeLine),assetsFinder)));
	}
	/**
	 * method for authentication
	 * @param request  http request
	 * @return
	 */
	public Result auth(Http.Request request) {
		Optional<String> verifier = request.queryString("oauth_verifier");
		Result result =
				verifier
						.filter(s -> !s.isEmpty())
						.map(
								s-> {
									OAuth.RequestToken requestToken = getSessionTokenPair(request).get();
									OAuth.RequestToken accessToken = TWITTER.retrieveAccessToken(requestToken, s);
									SESSION_ID++;
									String id = "Session:"+SESSION_ID;
									twitterActor.tell(new Message.Session(id), ActorRef.noSender());
									searchResultsMap.addSearchResultsMap(id, CompletableFuture.supplyAsync(() -> new ArrayList<SearchResults>()));
									return redirect(routes.MainController.main())
											.addingToSession(request, "session_id", id)
											.addingToSession(request, "token", accessToken.token)
											.addingToSession(request, "secret", accessToken.secret);
								})
						.orElseGet(
								() -> {
									String url = routes.MainController.auth().absoluteURL(request);
									OAuth.RequestToken requestToken = TWITTER.retrieveRequestToken(url);
									return redirect(TWITTER.redirectUrl(requestToken.token))
											.addingToSession(request, "token", requestToken.token)
											.addingToSession(request, "secret", requestToken.secret);
								});

		return result;
	}
	/**
	 * optional getSessionTokenPair method
	 * @param request   http request
	 * @return
	 */
	public Optional<OAuth.RequestToken> getSessionTokenPair(Http.Request request) {
		return request
				.session()
				.get("token")
				.map(token -> new OAuth.RequestToken(token, request.session().get("secret").get()));
	}

//	public WebSocket ws(String type,Http.Request req) {
public WebSocket ws(String type) {
//		String sessionId=session().get("session_id");
		if(type.equals("hashtag")) {
			return WebSocket.Json.accept(request ->{
				Optional<String> session_id = request.session().get("session_id");
				return ActorFlow.actorRef(wsOut->UserActor.props(wsOut,session_id.get()), actorSystem, materializer);
			});
		}
		else {
			return WebSocket.Json.accept(request ->{
				Optional<String> session_id = request.session().get("session_id");
//				Optional<OAuth.RequestToken> sessionTokenPair = getSessionTokenPair(req);
//				Optional<String> sessionId = req.session().get("session_id");
				return ActorFlow.actorRef(wsOut->UserActor.props(wsOut,session_id.get()),actorSystem, materializer );
			});
		}
	}

}
