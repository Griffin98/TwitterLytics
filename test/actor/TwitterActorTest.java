package actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.*;
import play.libs.oauth.OAuth;
import actors.*;

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
    public void testGreeterActorSendingOfGreeting() {
        OAuth.RequestToken tempToken = new OAuth.RequestToken("804992947015929857-DhDb94zyLSUkZLkXBprs48w9diAMPy9",
                "t9LOxQbuAuAN0R4rw2Xq7KKZpXw54cECifvxDzAXBD0EM");
        final TestKit testProbe = new TestKit(actorSystem);
        twitterActor = actorSystem.actorOf(TwitterActor.getProps(tempToken), "twitterActor");
        twitterActor.tell(new Message.Session("dummysession"),testProbe.getRef());
        testProbe.expectNoMsg();
        twitterActor.tell(new Message.Register(Message.TYPE.KEYWORD,"dummysession"), testProbe.getRef());
        testProbe.expectNoMsg();
        twitterActor.tell(new Message.Keyword("hello","testSessionId",Message.TYPE.KEYWORD), testProbe.getRef());
        testProbe.expectMsgClass(CompletableFuture.class);
        twitterActor.tell(new Message.Tick(), testProbe.getRef());
        testProbe.expectMsgClass(Message.Update.class);
    }

}
