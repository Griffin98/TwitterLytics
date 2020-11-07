package services;

import java.util.ArrayList;
import java.util.List;

import factory.UserFactory;
import model.Tweet;
import model.User;
import twitter4j.*;

/**
 *
 */
public class TwitterAPIService {

    private Twitter twitter;

    public TwitterAPIService(Twitter twitter) {
        this.twitter = twitter;
    }

    /**
     *
     * @param keyword
     * @return
     */
    public List<Tweet> getTweets(String keyword, int limit) {

        ArrayList<Tweet> result = new ArrayList<>();

        Query query  = new Query(keyword);
        try {
            QueryResult queryResult = twitter.search(query);
            List<Status> statuses = queryResult.getTweets();

            int i=0;
            for(Status status : statuses) {
                User user = UserFactory.getInstance().getOrCreateUser(status.getUser().getId(), status.getUser().getName(),
                        status.getUser().getScreenName(), status.getUser().getMiniProfileImageURL(), status.getUser().getName());

                Tweet tweet = new Tweet(user, status.getText(), status.getCreatedAt());
                result.add(tweet);

                if(++i == limit)
                    break;
            }

        } catch (TwitterException e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }
}
