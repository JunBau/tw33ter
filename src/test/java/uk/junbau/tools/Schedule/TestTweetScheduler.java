package uk.junbau.tools.Schedule;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import twitter4j.TwitterException;
import uk.junbau.tools.JSON.ITweet;
import uk.junbau.tools.JSON.ScheduledTweets;
import uk.junbau.tools.JSON.Tweet;
import uk.junbau.tools.Twitter.TwitterAccountController;

import java.lang.reflect.Constructor;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;
import static uk.junbau.tools.Schedule.TweetScheduler.MSG_KEY;
import static uk.junbau.tools.Schedule.TweetScheduler.TAC_KEY;

public class TestTweetScheduler {
    @Mock
    Scheduler sched;
    @Mock
    TwitterAccountController tac;
    @Mock
    ScheduledTweets tweets;

    @BeforeMethod
    private void configure() {
        MockitoAnnotations.initMocks(this);
    }

    private ITweet createMockTweet(String msg, Instant time) {
        ITweet ret = mock(ITweet.class);
        when(ret.getMsg()).thenReturn(msg);
        when(ret.getScheduledTime()).thenReturn(time);
        return ret;
    }

    private void verifyScheduledJob(List<JobDetail> jobs, List<Trigger> triggers, int elem, String msg, Instant time) {
        JobDetail jd = jobs.get(elem);
        assertNotNull(jd);
        assertEquals(jd.getJobDataMap().getString(MSG_KEY), msg);
        TwitterAccountController tacElem = (TwitterAccountController)jd.getJobDataMap().get(TAC_KEY);
        assertEquals(tacElem, tac);
        Trigger t = triggers.get(elem);
        assertTrue(Date.from(time).equals(t.getStartTime()));
    }

    @Test
    public void testSchedulerCorrectlySetsUpJobsToExecute() throws SchedulerException {
        //Given some mock calls to satisfy the preconditions.
        List<ITweet> tList = new ArrayList<>();
        Instant now = Instant.now();
        tList.add(createMockTweet("test1", now));
        tList.add(createMockTweet("test2", now.plusSeconds(300)));
        when(tweets.getScheduledTweets()).thenReturn(tList);

        //When we construct the scheduler.
        try (TweetScheduler ts = new TweetScheduler(sched, tac, tweets)) {}

        //Then it should have scheduled the jobs and started the scheduler.
        ArgumentCaptor<JobDetail> job = ArgumentCaptor.forClass(JobDetail.class);
        ArgumentCaptor<Trigger> trigger = ArgumentCaptor.forClass(Trigger.class);
        verify(sched, times(2)).scheduleJob(job.capture(), trigger.capture());
        assertEquals(job.getAllValues().size(), 2);
        verifyScheduledJob(job.getAllValues(), trigger.getAllValues(), 0, "test1", now);
        verifyScheduledJob(job.getAllValues(), trigger.getAllValues(), 1, "test2", now.plusSeconds(300));
        verify(sched).start();
        verify(sched).shutdown(false);
    }

    @Test
    public void testJobExecutorSuccessfullyPostsTweet() throws Exception {
        //Given some mock calls to satisfy the preconditions.
        List<ITweet> tList = new ArrayList<>();
        Instant now = Instant.now();
        final String msg = "test message here";
        tList.add(createMockTweet(msg, now));
        when(tweets.getScheduledTweets()).thenReturn(tList);
        JobExecutionContext mockCtx = mock(JobExecutionContext.class);
        JobDetail mockDetail = mock(JobDetail.class);
        JobDataMap mockMap = mock(JobDataMap.class);
        when(mockCtx.getJobDetail()).thenReturn(mockDetail);
        when(mockDetail.getJobDataMap()).thenReturn(mockMap);
        when(mockMap.getString(MSG_KEY)).thenReturn(msg);
        when(mockMap.get(TAC_KEY)).thenReturn(tac);

        //When we construct the scheduler.
        TweetScheduler ts = new TweetScheduler(sched, tac, tweets);
        ArgumentCaptor<JobDetail> job = ArgumentCaptor.forClass(JobDetail.class);
        ArgumentCaptor<Trigger> trigger = ArgumentCaptor.forClass(Trigger.class);
        verify(sched).scheduleJob(job.capture(), trigger.capture());

        //Then executing the job should post the tweet.
        assertEquals(job.getAllValues().size(), 1);
        Constructor c = job.getAllValues().get(0).getJobClass()
                .getDeclaredConstructor();
        c.setAccessible(true);
        Job runner = (Job)c.newInstance();
        runner.execute(mockCtx);
        verify(tac).makeTweet(msg);
    }

    @Test
    public void testJobExecutorThrowsExceptionIfTweetPostingFails() throws Exception {
        //Given some mock calls to satisfy the preconditions.
        List<ITweet> tList = new ArrayList<>();
        Instant now = Instant.now();
        final String msg = "test message here";
        tList.add(createMockTweet(msg, now));
        when(tweets.getScheduledTweets()).thenReturn(tList);
        JobExecutionContext mockCtx = mock(JobExecutionContext.class);
        JobDetail mockDetail = mock(JobDetail.class);
        JobDataMap mockMap = mock(JobDataMap.class);
        when(mockCtx.getJobDetail()).thenReturn(mockDetail);
        when(mockDetail.getJobDataMap()).thenReturn(mockMap);
        when(mockMap.getString(MSG_KEY)).thenReturn(msg);
        when(mockMap.get(TAC_KEY)).thenReturn(tac);

        //When we construct the scheduler.
        TweetScheduler ts = new TweetScheduler(sched, tac, tweets);
        ArgumentCaptor<JobDetail> job = ArgumentCaptor.forClass(JobDetail.class);
        ArgumentCaptor<Trigger> trigger = ArgumentCaptor.forClass(Trigger.class);
        verify(sched).scheduleJob(job.capture(), trigger.capture());

        //Then executing the job should throw a JobExecutionException.
        TwitterException mockExc = mock(TwitterException.class);
        when(mockExc.getErrorMessage()).thenReturn("some error");
        when(tac.makeTweet(any())).thenThrow(mockExc);
        assertEquals(job.getAllValues().size(), 1);
        Constructor c = job.getAllValues().get(0).getJobClass()
                .getDeclaredConstructor();
        c.setAccessible(true);
        Job runner = (Job)c.newInstance();
        try {
            runner.execute(mockCtx);
            fail("Exception was expected");
        } catch (JobExecutionException e) {
            assertEquals(e.getMessage(), "some error");
        }
    }
}