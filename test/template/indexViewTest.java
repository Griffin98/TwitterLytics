package template;
import controllers.AssetsFinder;
import org.junit.Test;
import play.twirl.api.Content;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.contentAsString;

/**
 * @author Manoj
 * To test the view, tweetWords
 */
public class indexViewTest {
    @Inject
    AssetsFinder assetsFinder;

    /**
     * Testing if the view is rendered with estimated text for given input parameters
     */
    @Test
    public void renderTemplate() {
        Content html = views.html.index.render(assetsFinder);
        assertEquals("text/html", html.contentType());
        assertTrue(contentAsString(html).contains("TwitterLytics"));
        assertTrue(contentAsString(html).contains("Welcome to,"));
        assertTrue(contentAsString(html).contains("Login to continue.."));
    }
}
