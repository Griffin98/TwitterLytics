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

        long lastID = Long.MAX_VALUE;

        Query query  = new Query(keyword);

        while(result.size() < limit) {

            if(limit - result.size() > 100)
                query.setCount(100);
            else
                query.setCount(limit - result.size());

            try {
                QueryResult queryResult = twitter.search(query);
                List<Status> statuses = queryResult.getTweets();

                for(Status status : statuses) {

                    if(status.getId() < lastID)
                        lastID = status.getId();

                    User user = UserFactory.getInstance().getOrCreateUser(status.getUser().getId(), status.getUser().getName(),
                            status.getUser().getScreenName(), status.getUser().getMiniProfileImageURL(), status.getUser().getName());

                    Tweet tweet = new Tweet(user, status.getText(), status.getCreatedAt());
                    result.add(tweet);

                }

            } catch (TwitterException e) {
                e.printStackTrace();
                return null;
            }

            query.setMaxId(lastID - 1);

        }

        return result;
    }
}
