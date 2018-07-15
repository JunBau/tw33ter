package uk.junbau.tools.JSON;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;

import static org.testng.Assert.*;

public class TestScheduledTweets {
    @Test
    public void testScheduledTweetsSerializesCorrectly() {
        //Given a ScheduledTweets object containing some tweets.
        ScheduledTweets tweets = new ScheduledTweets();
        //And a couple of tweets in the future.
        Instant now = Clock.systemUTC().instant();
        Instant nowPlus1K = now.plusSeconds(1000);
        Instant nowPlus2K = now.plusSeconds(2000);
        tweets.getScheduledTweets().add(new Tweet("Test one", nowPlus1K));
        tweets.getScheduledTweets().add(new Tweet("Test two", nowPlus2K));
        //And a tweet in the past which should be excluded from serialization.
        tweets.getScheduledTweets().add(new Tweet("Test omitme", now.minusSeconds(200)));

        //When we serialize it into a string.
        String out = tweets.serialize();

        //Then the string should contain the correct values.
        String json = "[{\"scheduledTime\":" + nowPlus1K.getEpochSecond() + ",\"msg\":\"Test one\"}" +
                      ",{\"scheduledTime\":" + nowPlus2K.getEpochSecond() + ",\"msg\":\"Test two\"}" +
                      "]";
        assertEquals(out, json);
    }

    @Test
    public void testJsonDeserializesCorrectlyToScheduledTime() throws UnsupportedEncodingException, IOException {
        //Given a piece of JSON with some objects in the future and in the past.
        Instant now = Clock.systemUTC().instant();
        Instant nowPlus1K = now.plusSeconds(1000);
        Instant nowPlus2K = now.plusSeconds(2000);
        String json = "[{\"scheduledTime\":" + nowPlus1K.getEpochSecond() + ",\"msg\":\"Test one\"}" +
                ",{\"scheduledTime\":" + nowPlus2K.getEpochSecond() + ",\"msg\":\"Test two\"}" +
                ",{\"scheduledTime\":" + now.minusSeconds(10).getEpochSecond() + ",\"msg\":\"Test omit\"}" +
                "]";

        //When we deserialize it into an Object.
        ScheduledTweets tweets = null;
        try (InputStreamReader in = new InputStreamReader(new ByteArrayInputStream(json.getBytes("UTF-8")))) {
            tweets = ScheduledTweets.deserialize(in);
        }

        //Then the object should contain what we expect.
        assertNotNull(tweets);
        assertNotNull(tweets.getScheduledTweets());
        assertEquals(tweets.getScheduledTweets().size(), 2);
        assertEquals(tweets.getScheduledTweets().get(0).getMsg(), "Test one");
        assertEquals(tweets.getScheduledTweets().get(1).getMsg(), "Test two");
        assertEquals(nowPlus1K.getEpochSecond(), tweets.getScheduledTweets().get(0).getScheduledTime().getEpochSecond());
        assertEquals(nowPlus2K.getEpochSecond(), tweets.getScheduledTweets().get(1).getScheduledTime().getEpochSecond());
    }
}