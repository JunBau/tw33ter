package uk.junbau.tools.Schedule;

import com.google.inject.Inject;
import org.quartz.*;
import twitter4j.TwitterException;
import uk.junbau.tools.JSON.ScheduledTweets;
import uk.junbau.tools.JSON.ITweet;
import uk.junbau.tools.Twitter.TwitterAccountController;

import java.util.Date;

public class TweetScheduler implements AutoCloseable {
    static final String MSG_KEY = "msg";
    static final String TAC_KEY = "tac";

    final Scheduler sched;

    private static class TweetJob implements Job {
        @Override
        public void execute(JobExecutionContext ctx) throws JobExecutionException {
            JobDataMap data = ctx.getJobDetail().getJobDataMap();
            String msg = data.getString(MSG_KEY);
            TwitterAccountController tac = (TwitterAccountController) data.get(TAC_KEY);
            try {
                tac.makeTweet(msg);
            } catch (TwitterException e) {
                throw new JobExecutionException(e.getErrorMessage(), false);
            }
        }
    }


    public TweetScheduler(Scheduler sched, TwitterAccountController tac, ScheduledTweets tweets) throws SchedulerException {
        this.sched = sched;
        for (ITweet tweet : tweets.getScheduledTweets()) {
            JobDataMap data = new JobDataMap();
            data.put(MSG_KEY, tweet.getMsg());
            data.put(TAC_KEY, tac);
            JobDetail job = JobBuilder.newJob(TweetJob.class).setJobData(data).build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .startAt(Date.from(tweet.getScheduledTime()))
                    .forJob(job)
                    .build();
            sched.scheduleJob(job, trigger);
        }
        sched.start();
    }

    @Override
    public void close() {
        try {
            sched.shutdown(false);
        } catch (SchedulerException e) {}
    }

}
