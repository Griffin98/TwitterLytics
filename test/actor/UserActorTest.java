package actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import akka.testkit.TestProbe;
import akka.testkit.javadsl.TestKit;
import org.junit.*;
import play.libs.oauth.OAuth;
import actors.*;
import model.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class UserActorTest {
    static ActorSystem actorSystem;
    private ActorRef userActor;
    @Before
    public  void setup() {
        actorSystem = ActorSystem.create();
    }
    @After
    public void teardown() {
        TestKit.shutdownActorSystem(actorSystem);
        actorSystem = null;
    }
    @Test
    public void testGreeterActorSendingOfGreeting() {
        OAuth.RequestToken tempToken = new OAuth.RequestToken("804992947015929857-DhDb94zyLSUkZLkXBprs48w9diAMPy9",
                "t9LOxQbuAuAN0R4rw2Xq7KKZpXw54cECifvxDzAXBD0EM");
        final TestKit testProbe = new TestKit(actorSystem);
        userActor = actorSystem.actorOf(UserActor.props(testProbe.getRef()), "userActor");
        User user = new User(1, "User", "screenName", "userprofile", "useProfile");
        Date date = new Date();
        ArrayList<String> hashTags = new ArrayList<>();
        hashTags.add("#tag1");
        hashTags.add("#tag2");
        List<String> sentiments = new ArrayList<>();
        sentiments.add(":-)");
        Tweet tweet = new Tweet(user, "text", date, hashTags);
        tweet.setTweetSentiment(sentiments);
        List<Tweet> tweetList = new ArrayList<>();
        List<Tweet> tweetList2 = new ArrayList<>();
        tweetList.add(tweet);
        tweetList2.add(tweet);
        tweetList2.add(new Tweet(user, "text2", date, new ArrayList<>()));
        SearchResults searchResults1 = new SearchResults("today", tweetList, ":-)");
        SearchResults searchResults2 = new SearchResults("after", tweetList2, ":-)");
        List<SearchResults> listSearchResults=new ArrayList<>();
        listSearchResults.add(searchResults1);
        listSearchResults.add(searchResults2);
        CompletableFuture<List<SearchResults>> listCompletableFuture=CompletableFuture.supplyAsync(()->listSearchResults);
        userActor.tell(new Message.Update(listCompletableFuture),testProbe.getRef());
        testProbe.expectNoMsg();

    }

}
