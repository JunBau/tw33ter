package junbau.tools.Schedule;

import junbau.tools.Functions.RandomLine;
import junbau.tools.Functions.TwitterAPI;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TimedTweet extends TwitterAPI implements org.quartz.Job  {

    private static String filePath = "";

    public TimedTweet() {
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Executing scheduled tweets...");
        System.out.println("Grabbing tweet from: " + filePath);

        RandomLine random = new RandomLine();
        random.Begin(filePath);
        String line = random.getTweetText();

        System.out.println(line);

        Tweet(line);
    }

    public static void setFilePath(String filePath) {
        TimedTweet.filePath = filePath;
    }
}
