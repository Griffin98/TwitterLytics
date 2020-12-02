import actors.*;
import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;
import com.google.inject.name.Names;

/**
 * This class extends from AkkaGuiceSupport.
 */
@SuppressWarnings("unused")
public class Module extends AbstractModule implements AkkaGuiceSupport {
	/**
	 * Override the configuration to combine witch the configuration for Twitter.
	 */
    @Override
    protected void configure() {
    	//bindActor(TwitterActor.class, "twitterActor");
        //bindActor(TwitterActor.class, "twitteraActor");
        //bindActorFactory(UserActor.class, UserActor.Factory.class);
    }
}