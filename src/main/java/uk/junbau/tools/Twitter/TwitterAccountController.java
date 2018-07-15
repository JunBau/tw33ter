package uk.junbau.tools.Twitter;

import com.google.inject.Inject;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import uk.junbau.tools.JSON.ITwitterCredentials;

public class TwitterAccountController {
    Twitter twitter;

    @Inject
    public TwitterAccountController(TwitterFactory factory, ITwitterCredentials creds) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(creds.getConsumerKey());
        cb.setOAuthConsumerSecret(creds.getConsumerSecret());
        cb.setOAuthAccessToken(creds.getUserToken());
        cb.setOAuthAccessTokenSecret(creds.getSecretKey());
        twitter = factory.getInstance(cb.build());
        twitter.setOAuthAccessToken(new AccessToken(creds.getUserToken(), creds.getSecretKey()));
    }

    public Status makeTweet(String msg) throws TwitterException {
        return twitter.updateStatus(msg);
    }
}
