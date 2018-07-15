package uk.junbau.tools;

import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;
import uk.junbau.tools.JSON.ITwitterCredentials;
import uk.junbau.tools.JSON.ScheduledTweets;
import uk.junbau.tools.JSON.TwitterCredentials;
import uk.junbau.tools.Schedule.TweetScheduler;
import uk.junbau.tools.Twitter.TwitterAccountController;
import uk.junbau.tools.Twitter.TwitterFactory;

import java.io.FileReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Tw33ter {
    public Tw33ter(String tweetDbFile, String credsFile) throws Throwable {
        ScheduledTweets tweets = ScheduledTweets.deserialize(new FileReader(tweetDbFile));
        ITwitterCredentials creds = TwitterCredentials.deserialize(new FileReader(credsFile));
        TwitterFactory tf = new TwitterFactory();
        TwitterAccountController tac = new TwitterAccountController(tf, creds);
        Scheduler sched = new StdSchedulerFactory().getScheduler();
        try (TweetScheduler ts = new TweetScheduler(sched, tac, tweets)) {
            CompletableFuture<Boolean> f = new CompletableFuture<>();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> f.complete(true)));
            f.get();
        }
    }
}
