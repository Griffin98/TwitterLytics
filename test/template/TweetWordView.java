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
public class TweetWordView {
    @Inject
    AssetsFinder assetsFinder;

    /**
     * Testing if the vies is rendered with estimated text for given input parameters
     */
    @Test
    public void renderTemplate() {
        Map<String, Long> frequencyMap = new HashMap<>();
        frequencyMap.put("Manoj",1L);
        Content html = views.html.tweetWords.render(frequencyMap,assetsFinder);
        assertEquals("text/html", html.contentType());
        assertTrue(contentAsString(html).contains("Tweet Words Statistics"));
        assertTrue(contentAsString(html).contains("Manoj"));
    }
}
