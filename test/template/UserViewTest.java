package template;
import controllers.AssetsFinder;
import model.Tweet;
import model.User;
import org.junit.Test;
import play.twirl.api.Content;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.libs.Scala.asScala;
import static play.test.Helpers.contentAsString;

/**
 * @author Manoj
 * To test the view, userProfile
 */
public class UserViewTest {
    @Inject
    AssetsFinder assetsFinder;

    /**
     * Testing if the view is rendered with estimated text for given input parameters
     */
    @Test
    public void renderTemplate() {
        User user=new User(1118808460987117573L,"test","test","test","test");
        List<Tweet> tweetList=new ArrayList<>();
        Content html = views.html.userProfile.render(user,asScala(tweetList),assetsFinder);
        assertEquals("text/html", html.contentType());
        assertTrue(contentAsString(html).contains("Welcome to your profile"));
        assertTrue(contentAsString(html).contains("Welcome test"));
        assertTrue(contentAsString(html).contains("User ID: 1118808460987117573"));
    }
}
