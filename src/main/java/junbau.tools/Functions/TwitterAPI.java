package junbau.tools.Functions;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAPI extends AuthAPI {

    public void Tweet (String userInput, TwitterFactory tf) {
        try {
            Twitter twitter = tf.getInstance();
            Status status = twitter.updateStatus(userInput);
            System.out.println("Successfully updated the status to [" + status.getText() + "].\n");
        } catch (TwitterException except) {
            except.printStackTrace();
        }
    }

    public void settingKeys(ConfigurationBuilder cb) {
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(getConsumerKey())
                .setOAuthConsumerSecret(getConsumerSecret())
                .setOAuthAccessToken(getUserToken())
                .setOAuthAccessTokenSecret(getSecretKey());
    }

}
