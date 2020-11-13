package template;

import controllers.AssetsFinder;
import factory.UserFactory;
import model.SearchData;
import model.SearchResults;
import model.Tweet;
import model.User;
import org.junit.Test;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.Http;
import play.test.WithApplication;
import play.twirl.api.Content;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static play.libs.Scala.asScala;

/**
 * @author Dhyey
 */

public class homeViewTest extends WithApplication {

    @Inject
    AssetsFinder assetsFinder;

    @Test
    public void renderTemplate() {
        FormFactory formFactory = app.injector().instanceOf(FormFactory.class);
        Form<SearchData> form = formFactory.form(SearchData.class);
        UserFactory instance  = UserFactory.getInstance();
        User user = instance.getOrCreateUser(999,
                "abc","ABC","image1", "link1");
        ArrayList<String> hastag = new ArrayList<>();
        hastag.add("#hello");
        Tweet tweet = new Tweet(user,"hello world", new Date(),hastag);
        ArrayList<Tweet> tweets = new ArrayList<>();
        tweets.add(tweet);
        tweets.add(tweet);
        tweets.add(tweet);
        SearchResults searchResults = new SearchResults("#hello", tweets, ":-)");
        ArrayList<SearchResults> results = new ArrayList<>();
        //results.add(searchResults);

        MessagesApi emptyMessagesApi = play.test.Helpers.stubMessagesApi();
        Http.Request request = new Http.RequestBuilder().build();

        Content html = views.html.home.render(form, asScala(results),
                assetsFinder, request, emptyMessagesApi.preferred(request));
        assertEquals("text/html", html.contentType());
    }
}
