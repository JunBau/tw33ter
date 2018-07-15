package uk.junbau.tools.Functions;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAPI extends AuthAPI {


    public static void Tweet (String userInput) {
        try {
            if (userInput.equals(null)) {
                System.out.println("Nothing to tweet.");
            } else {
                Status status = getTwitter().updateStatus(userInput);
                System.out.println("Successfully updated the status to [" + status.getText() + "].\n");
            }
        } catch (TwitterException except) {
            except.printStackTrace();
        }
    }

    private static Twitter getTwitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(getConsumerKey())
                .setOAuthConsumerSecret(getConsumerSecret())
                .setOAuthAccessToken(getUserToken())
                .setOAuthAccessTokenSecret(getSecretKey());
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        twitter.setOAuthAccessToken(new AccessToken(getUserToken(),getSecretKey()));

        return twitter;
    }

}
