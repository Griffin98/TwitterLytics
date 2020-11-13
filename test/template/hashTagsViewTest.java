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

import static play.libs.Scala.asScala;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.contentAsString;

/**
 * @author Dhyey
 */

public class hashTagsViewTest extends WithApplication {

    @Inject
    AssetsFinder assetsFinder;


    @Test
    public void add() {
        List<SearchResults> searchResultsList=new ArrayList<>();
        searchResultsList.add(new SearchResults("test",new ArrayList<>(),":-)"));
        searchResultsList.add(new SearchResults("test1",new ArrayList<>(),":-)"));
        Http.Request request = new Http.RequestBuilder().build();
        MessagesApi messagesApi = play.test.Helpers.stubMessagesApi();
        Content html = views.html.hashTags.render(asScala(searchResultsList),assetsFinder,request, messagesApi.preferred(request));
        assertEquals("text/html", html.contentType());
        assertTrue(contentAsString(html).contains("test"));
        assertTrue(contentAsString(html).contains("test1"));
    }
}
