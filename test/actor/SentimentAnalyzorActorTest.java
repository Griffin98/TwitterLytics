package actor;

import actors.Message;
import actors.SentimentAnalyzerActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import model.SearchResults;
import model.Tweet;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;

/**
 * @author Darshan on 04-12-2020 10:17
 * @project TwitterLytics-v2
 */
public class SentimentAnalyzorActorTest {
    static ActorSystem actorSystem;
    private ActorRef tweetSentimentActor;
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
    public void testGetSingleOverAllTweetResult() {
        final TestKit testProbe = new TestKit(actorSystem);
        tweetSentimentActor = actorSystem.actorOf(SentimentAnalyzerActor.getProps(), "sentimentAnalyzerActor");

        User user = new User(1, "User", "screenName", "userprofile", "useProfile");
        Date date = new Date();

        ArrayList<String> hashTags = new ArrayList<>();
        hashTags.add("#tag1");
        hashTags.add("#tag2");

        List<String> sentiments = new ArrayList<>();
        sentiments.add(":-|");

        Tweet tweet = new Tweet(user, "text", date, hashTags);
        tweet.setTweetSentiment(sentiments);

        List<Tweet> tweetList = new ArrayList<>();
        tweetList.add(tweet);

        tweetSentimentActor.tell(new Message.GetSingleTweetResult(tweetList), testProbe.getRef());
        testProbe.expectMsg(sentiments);

        tweetSentimentActor.tell(new Message.GetOverallTweetResult(sentiments), testProbe.getRef());
        testProbe.expectMsg(":-|");


    }
}
