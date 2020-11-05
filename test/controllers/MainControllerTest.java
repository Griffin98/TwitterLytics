//package controllers;
//
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import play.Application;
//import play.inject.guice.GuiceApplicationBuilder;
//import play.libs.oauth.OAuth;
//import play.mvc.Http;
//import play.mvc.Result;
//import play.test.Helpers;
//import play.test.WithApplication;
//
//import javax.inject.Inject;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.Assert.assertEquals;
//import static play.mvc.Http.Status.OK;
//import static play.mvc.Http.Status.SEE_OTHER;
//import static play.mvc.Results.ok;
//import static play.test.Helpers.GET;
//import static play.test.Helpers.route;
//
//public class MainControllerTest extends WithApplication {
//    @Inject MainController mainController;
//    @Override
//    protected Application provideApplication() {
//        return new GuiceApplicationBuilder().build();
//    }
//    @Test
//    public void testIndex() {
//        Http.RequestBuilder request = Helpers.fakeRequest()
//                .method(GET)
//                .uri("/");
//        Result result = route(app, request);
//        assertEquals(OK, result.status());
//    }
////    @Test
////    public void testSearchFails(){
////        Http.RequestBuilder request = Helpers.fakeRequest(routes.MainController.search());
////        Result result = route(app, request);
////        assertEquals(400, result.status());
////    }
////    @Test
////    public void testMainFails(){
////        Http.RequestBuilder request = Helpers.fakeRequest(routes.MainController.main());
////        Result result = route(app, request);
////        assertEquals(400, result.status());
////    }
////    @Test
////    public void testMainPasses(){
////        HashMap<String,String> sessionMap = new HashMap<>();
////        sessionMap.put("token","1118808460987117573-bra8aLIYFmQkQz7vQC098HIDdhDu1q");
////        sessionMap.put("secret","s9WrtAp1qiDUVSSli1jTRaY0PNpohQPFHKGq6vpBTR99e");
////        Http.RequestBuilder request = Helpers.fakeRequest()
////                .method(GET)
////                .uri("/").session(sessionMap);
////        Result result = route(app, request);
////        assertEquals(OK, result.status());
////    }
////    @Test
////    public void testTweetWordsFail(){
////        Http.RequestBuilder request = Helpers.fakeRequest(routes.MainController.tweetWords(0));
////        Result result = route(app, request);
////        assertEquals(400, result.status());
////    }
////    @Test
////    public void testTweetWordsPasses(){
////        Http.RequestBuilder request = Helpers.fakeRequest(routes.MainController.main());
//////        Http.RequestBuilder session = Helpers.fakeRequest().session("token", "lsdlds");
////        Map<String,String> sessionMap=new HashMap<>();
////        sessionMap.put("token","1118808460987117573-bra8aLIYFmQkQz7vQC098HIDdhDu1q");
////        sessionMap.put("secret","s9WrtAp1qiDUVSSli1jTRaY0PNpohQPFHKGq6vpBTR99e");
//////        Http.RequestBuilder request1 = request.session(sessionMap);
////        Result result = route(app, request).addingToSession(request.build(),sessionMap);
////        assertEquals(OK, result.status());
////    }
//}
