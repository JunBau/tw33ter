package junbau.tools;


import junbau.tools.Functions.RandomLine;
import junbau.tools.Functions.TwitterAPI;
import junbau.tools.Schedule.TimedTweet;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.Scanner;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Main extends TwitterAPI {

    private static Scanner userInput = new Scanner(System.in);

    public static void main(String[] args) throws SchedulerException {

        System.out.println("Welcome to tw33ter!");
        System.out.println("tw33ter is a tweet scheduler.\n");

        boolean quit = false;
        int choice = 0;

        while (!quit) {

            displayOptions();

            try {
                choice = Integer.parseInt(userInput.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input.");
            } if (choice > 5) {
                System.out.println("Wrong number mate.");
            }

            switch (choice) {
                case 1:
                    postTweet();
                    break;
                case 2:
                    randLine();
                    break;
                case 3:
                    randomTweet();
                    break;
//                case 4:
//                    returnStatus();
//                    break;
                case 5:
                    scheduledTweets();
                    break;
            }
        }

    }

    private static void displayOptions () {
        System.out.println("Choose from the following: " +
                "\n1 - Tweet" +
                "\n2 - Random line print" +
                "\n3 - Tweet something random");
    }

    private static void postTweet() {
        System.out.println("\nEnter your tweet:");
        String input = userInput.nextLine();
        Tweet(input);
    }

    //Just prints random line to console for visual purposes.
    private static void randLine() {
        String file = userInput.nextLine();
        RandomLine random = new RandomLine();
        random.Begin(file);
        System.out.println(random.getTweetText());
    }

    //Takes a random line from a txt file and tweets it.
    private static void randomTweet() {
        System.out.println("\nEnter path for the file.");
        RandomLine random = new RandomLine();
        String input = userInput.nextLine();
        random.Begin(input);
        String line = random.getTweetText();
        System.out.println(random.getTweetText());
        Tweet(line);
    }

//    private static void returnStatus() {
//        RandomLine random = new RandomLine();
//        random.UniqueString(tf);
//    }

    private static void scheduledTweets() {

        System.out.println("Let's set the file path: ");
        String filePath = userInput.nextLine();
        TimedTweet.setFilePath(filePath);

        try {

            JobDetail job = newJob(TimedTweet.class)
                    .withIdentity("TimedTweet", "group1")
                    .build();

            Date runTime = evenMinuteDate(new Date());

// Trigger the job to run on the next round minute
            Trigger trigger = newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startAt(runTime)
                    .withSchedule(simpleSchedule()
                            .withIntervalInMinutes(30)
                            .repeatForever())
                    .build();

            Scheduler sf = new StdSchedulerFactory().getScheduler();
            sf.start();
            sf.scheduleJob(job,trigger);


        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }
}
