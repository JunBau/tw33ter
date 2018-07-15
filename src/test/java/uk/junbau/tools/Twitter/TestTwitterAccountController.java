package uk.junbau.tools.Twitter;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.mockito.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import uk.junbau.tools.JSON.ITwitterCredentials;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

public class TestTwitterAccountController {
    @Mock
    TwitterFactory twitterFactory;
    @Mock
    ITwitterCredentials credentials;
    @Mock
    Twitter twitter;
    @Mock
    Status status;

    @BeforeMethod
    private void configure() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConfigurationBuilderIsCorrectlySetup() {
        //Given a mock that will return our own values.
        final String consumerKey = "08wqp4nfofno";
        final String consumerSec = "80234ngfio[qf";
        final String accessToken = "02389hfjiopqewk";
        final String accessSecrt = "89w4pghuioq;42w";
        when(twitterFactory.getInstance(any())).thenReturn(twitter);
        when(credentials.getConsumerKey()).thenReturn(consumerKey);
        when(credentials.getConsumerSecret()).thenReturn(consumerSec);
        when(credentials.getUserToken()).thenReturn(accessToken);
        when(credentials.getSecretKey()).thenReturn(accessSecrt);

        //When we construct the TwitterAccountController and capture the arguments
        TwitterAccountController t = new TwitterAccountController(twitterFactory, credentials);
        ArgumentCaptor<Configuration> conf = ArgumentCaptor.forClass(Configuration.class);
        verify(twitterFactory).getInstance(conf.capture());

        //Then they should match what we set.
        Configuration confArgs = conf.getValue();
        assertTrue(confArgs.isDebugEnabled());
        assertEquals(confArgs.getOAuthConsumerKey(), consumerKey);
        assertEquals(confArgs.getOAuthConsumerSecret(), consumerSec);
        assertEquals(confArgs.getOAuthAccessToken(), accessToken);
        assertEquals(confArgs.getOAuthAccessTokenSecret(), accessSecrt);
    }

    @Test
    public void testOAuthAccessTokenIsSetOnTwitterInstance() {
        //Given mocks for instantiating a twitter instance.
        final String accessToken = "923480yt0qhuiogjwel";
        final String accessSecrt = "iuwagjk34gpol";
        when(credentials.getUserToken()).thenReturn(accessToken);
        when(credentials.getSecretKey()).thenReturn(accessSecrt);
        when(twitterFactory.getInstance(any())).thenReturn(twitter);

        //When we construct the TwitterAccountController and capture the AccessToken arguments
        TwitterAccountController t = new TwitterAccountController(twitterFactory, credentials);
        ArgumentCaptor<AccessToken> info = ArgumentCaptor.forClass(AccessToken.class);
        verify(twitter).setOAuthAccessToken(info.capture());

        //Then it should have set the AccessToken
        assertEquals(info.getValue().getToken(), accessToken);
        assertEquals(info.getValue().getTokenSecret(), accessSecrt);
    }

    @Test
    public void testTweetIsSentToAPIForDeliveryAndReturnsValidStatus() throws TwitterException {
        //Given a message we want to tweet and a mock for the Twitter call
        final String msg = "hi, this is a test tweet that we want to send";
        final String accessToken = "923480yt0qhuiogjwel";
        final String accessSecrt = "iuwagjk34gpol";
        when(credentials.getUserToken()).thenReturn(accessToken);
        when(credentials.getSecretKey()).thenReturn(accessSecrt);
        when(twitterFactory.getInstance(any())).thenReturn(twitter);
        when(twitter.updateStatus(any(String.class))).thenReturn(status);

        //When we construct the TwitterAccountManager and call the method.
        TwitterAccountController t = new TwitterAccountController(twitterFactory, credentials);
        Status result = t.makeTweet(msg);

        //Then is should have returned our status and the message should be ours too.
        assertEquals(result, status);
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        verify(twitter).updateStatus(arg.capture());
        assertEquals(arg.getValue(), msg);
    }
}