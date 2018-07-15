package uk.junbau.tools.Twitter;

import twitter4j.Twitter;
import twitter4j.conf.Configuration;

public class TwitterFactory {
    public Twitter getInstance(Configuration arg) {
        return new twitter4j.TwitterFactory(arg).getInstance();
    }
}
