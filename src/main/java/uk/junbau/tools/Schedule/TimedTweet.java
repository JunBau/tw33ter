package uk.junbau.tools.Schedule;

import uk.junbau.tools.Functions.RandomLine;
import uk.junbau.tools.Functions.TwitterAPI;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TimedTweet extends TwitterAPI implements org.quartz.Job  {

    private static String filePath = "";
    static RandomLine random = new RandomLine();

    public TimedTweet() {
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Executing scheduled tweets...");
        System.out.println("Grabbing tweet from: " + filePath);

        String tweet = random.getTweetText();

        System.out.println(tweet);

        random.removeTweet(tweet);

        Tweet(tweet);
    }

    public static void setFilePath(String filePath) {
        TimedTweet.filePath = filePath;
        random.readFile(filePath);
    }

    public static void printArrayList() {
        random.printTweetArray();
    }
}
