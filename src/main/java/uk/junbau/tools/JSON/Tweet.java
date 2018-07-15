package uk.junbau.tools.JSON;

import java.time.Instant;

public class Tweet implements ITweet {
    private final String msg;
    private final Instant scheduledTime;
    Tweet (String msg, Instant scheduledTime) {
        this.msg = msg;
        this.scheduledTime = scheduledTime;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public Instant getScheduledTime() {
        return scheduledTime;
    }
}