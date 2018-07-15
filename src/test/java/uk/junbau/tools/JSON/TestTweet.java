package uk.junbau.tools.JSON;

import org.testng.annotations.Test;

import java.time.Instant;

import static org.testng.Assert.*;

public class TestTweet {
    @Test
    public void testGetMsg() {
        Tweet tweet = new Tweet("someMessage", null);
        assertEquals(tweet.getMsg(), "someMessage");
    }

    @Test
    public void testGetScheduledTime() {
        Instant now = Instant.now();
        Tweet tweet = new Tweet(null, now);
        assertEquals(tweet.getScheduledTime(), now);
    }
}