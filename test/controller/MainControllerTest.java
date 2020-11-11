package controller;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;

import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;

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
}
