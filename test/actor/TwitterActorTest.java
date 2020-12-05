package actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import model.SearchResults;
import org.junit.*;
import play.libs.oauth.OAuth;
import actors.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public class TwitterActorTest {
    static ActorSystem actorSystem;
    private ActorRef twitterActor;
    @Before
    public void setup() {
        actorSystem = ActorSystem.create();
    }
    @After
    public  void teardown() {
        TestKit.shutdownActorSystem(actorSystem);
        actorSystem = null;
    }
    @Test
    public void testTwitterActor() {
        OAuth.RequestToken tempToken = new OAuth.RequestToken("804992947015929857-DhDb94zyLSUkZLkXBprs48w9diAMPy9",
                "t9LOxQbuAuAN0R4rw2Xq7KKZpXw54cECifvxDzAXBD0EM");
        final TestKit testProbe = new TestKit(actorSystem);
        twitterActor = actorSystem.actorOf(TwitterActor.getProps(tempToken), "twitterActor");
        twitterActor.tell(new Message.Session("dummysession1"),testProbe.getRef());
        testProbe.expectNoMsg();
        twitterActor.tell(new Message.Session("dummysession2"),testProbe.getRef());
        testProbe.expectNoMsg();
        final TestKit userTestProbe1 = new TestKit(actorSystem);
        twitterActor.tell(new Message.Register(Message.TYPE.KEYWORD,"dummysession1"), userTestProbe1.getRef());
        testProbe.expectNoMsg();
        twitterActor.tell(new Message.Keyword("hello","dummysession1",Message.TYPE.KEYWORD), testProbe.getRef());
        CompletableFuture<Object> future = testProbe.expectMsgClass(CompletableFuture.class);
        List<SearchResults> recvMsg = future.thenApply(object -> (List<SearchResults>) object).join();
//        testProbe.expectMsgClass(CompletableFuture.class);
        twitterActor.tell(new Message.Keyword("covid","dummysession1",Message.TYPE.KEYWORD), testProbe.getRef());
        twitterActor.tell(new Message.Keyword("america","dummysession1",Message.TYPE.KEYWORD), testProbe.getRef());
        twitterActor.tell(new Message.Tick(), testProbe.getRef());
        userTestProbe1.expectMsgClass(Message.Update.class);
        twitterActor.tell(new Message.FindStatistics("dummysession1",0), testProbe.getRef());
        testProbe.expectMsgClass(CompletableFuture.class);
    }

}
