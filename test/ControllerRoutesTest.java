import controllers.routes;
import controllers.MainController;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;
import play.test.WithBrowser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.*;

public class ControllerRoutesTest extends WithBrowser {

    private Application application;

    @Override
    protected Application provideApplication() {
        application = new GuiceApplicationBuilder().build();
        return super.provideApplication();
    }

    @Test
    public void testBasReverseRoutes() {

        Helpers.running(fakeApplication(), () -> {
            Http.RequestBuilder requestBuilder = Helpers.fakeRequest(routes.MainController.index());
            Result result = Helpers.route(application, requestBuilder);
            assertEquals(200, result.status());
        });

        Helpers.running(fakeApplication(), () -> {
            Http.RequestBuilder requestBuilder = Helpers.fakeRequest(routes.MainController.search());
            Result result = Helpers.route(application, requestBuilder);
            assertEquals(400, result.status());
        });

        Helpers.running(fakeApplication(), () -> {
            Http.RequestBuilder requestBuilder = Helpers.fakeRequest(routes.MainController.searchHashTags("#"));
            Result result = Helpers.route(application, requestBuilder);
            assertEquals(400, result.status());
        });

        Helpers.running(fakeApplication(), () -> {
            Http.RequestBuilder requestBuilder = Helpers.fakeRequest(routes.MainController.searchHashTags("#"));
            Result result = Helpers.route(application, requestBuilder);
            assertEquals(400, result.status());
        });

        Helpers.running(fakeApplication(), () -> {
            Http.RequestBuilder requestBuilder = Helpers.fakeRequest(routes.MainController.userProfile(1L));
            Result result = Helpers.route(application, requestBuilder);
            assertEquals(400, result.status());
        });

    }

    @Test
    public void testInBrowser() {
        Helpers.running(testServer(3333), HTMLUNIT, browser -> {
            browser.goTo("/");
            assertEquals("TwitterLytics", browser.el("title").text());

        });
    }
}
