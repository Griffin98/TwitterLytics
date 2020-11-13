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
 * To test the view, errorView
 */
public class errorViewTest {
    @Inject
    AssetsFinder assetsFinder;

    /**
     * Testing if the view is rendered with estimated text for given input parameters
     */
    @Test
    public void renderTemplate() {
        Content html = views.html.errorView.render("error test",assetsFinder);
        assertEquals("text/html", html.contentType());
        assertTrue(contentAsString(html).contains("error test"));
        assertTrue(contentAsString(html).contains("You can only access data which is present"));
    }
}
