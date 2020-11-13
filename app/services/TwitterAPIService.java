package services;

import java.util.ArrayList;
import java.util.List;

import factory.UserFactory;
import model.Tweet;
import model.User;
import twitter4j.*;

/**
 * @author Dhyey Patel
 * Core class which perform API call to Twitter using Twitter4j. This class
 * encapsulates twitter4j responses into {@link Tweet}.
 */
public class TwitterAPIService {

    // Instance of Twitter object (see: twitter4j).
    private Twitter twitter;

    /**
     * Constructor to obtain instance of {@link TwitterAPIService} class.
     * @param twitter instance of {@link Twitter}
     */
    public TwitterAPIService(Twitter twitter) {
        if(twitter == null && ! (twitter instanceof Twitter))
            throw new NullPointerException("");
        this.twitter = twitter;
    }

    /**
     * Method to fetch list of {@link Tweet} from Twitter.
     * @param keyword keyword for which tweets need to be fetched.
     * @param limit number of tweets to fetch.
     * @return list of {@link Tweet}
     */
    public List<Tweet> getTweets(String keyword, int limit) {

        ArrayList<Tweet> result = new ArrayList<>();

        // Set the query with limit of tweet to fetch.
        Query query  = new Query(keyword);
        query.setCount(limit);
        try {
            QueryResult queryResult = twitter.search(query);
            List<Status> statuses = queryResult.getTweets();

            int i=0;

            // Encapsulates result into Tweet.
            for(Status status : statuses) {

                // Obtain hashtags from tweet too.
                ArrayList<String> hashTags = new ArrayList<>();
                HashtagEntity[] tags = status.getHashtagEntities();
                if(tags != null  && tags.length >0){

                    for(HashtagEntity tag : tags) {
                        hashTags.add("#" + tag.getText());
                    }
                }

                User user = UserFactory.getInstance().getOrCreateUser(status.getUser().getId(), status.getUser().getName(),
                        status.getUser().getScreenName(), status.getUser().getMiniProfileImageURL(), status.getUser().getName());

                Tweet tweet = new Tweet(user, status.getText(), status.getCreatedAt(), hashTags);
                result.add(tweet);

                if(++i == limit)
                    break;
            }

        } catch (TwitterException e) {
            return null;
        }

        return result;
    }

    /**
     * Method provides list of tweet by the user.
     * @param user_id twitter user id of the user.
     * @return list of {@link Tweet}
     */
    public List<Tweet> getHomeLineById(long user_id) {
        ArrayList<Tweet> results = new ArrayList<>();

        try {
            List<Status> statues = twitter.getUserTimeline(user_id);
            for (Status status : statues) {
                ArrayList<String> hashTags = new ArrayList<>();
                HashtagEntity[] tags = status.getHashtagEntities();
                if(tags != null  && tags.length >0){

                    for(HashtagEntity tag : tags) {
                        hashTags.add("#" + tag.getText());
                    }
                }
                User user = UserFactory.getInstance().getOrCreateUser(status.getUser().getId(), status.getUser().getName(), status.getUser().getScreenName(),status.getUser().getMiniProfileImageURL(), status.getUser().getName());
                Tweet result = new Tweet(user, status.getText(), status.getCreatedAt(),hashTags);
                results.add(result);
            }
        } catch (TwitterException e) {
            return null;
        }

        return results;
    }
}
