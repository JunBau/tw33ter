package uk.junbau.tools;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.junbau.tools.Functions.RandomLine;
import uk.junbau.tools.Functions.TwitterAPI;
import uk.junbau.tools.Schedule.TimedTweet;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import uk.junbau.tools.JSON.TwitterCredentials;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Main extends TwitterAPI {

    private static Scanner userInput = new Scanner(System.in);

    public static void main(String[] args) throws Throwable {
        switch (args.length) {
            case 0:
                startInInteractiveMode();
                break;
            case 2:
                startInAutomaticMode(args[0], args[1]);
                break;
            default:
                System.out.println("Tw33ter requires either no arguments or a conffile.json and tweetdb.json");
                break;
        }
    }

    private static void startInInteractiveMode() {
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
            } if (choice > 5 || choice < 1) {
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
                case 4:
                    scheduledTweets();
                    break;
                case 5:
                    TimedTweet.printArrayList();
                    break;

                default:
                    System.out.println("Please pick a valid option.");
                    break;
            }
        }
    }

    private static void displayOptions () {
        System.out.println("Choose from the following: " +
                "\n1 - Tweet" +
                "\n2 - Random line print" +
                "\n3 - Tweet something random" +
                "\n4 - Schedule random tweet" +
                "\n5 - Print tweet list");
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
        random.readFile(file);
        System.out.println(random.getTweetText());
    }

    //Takes a random line from a txt file and tweets it.
    private static void randomTweet() {
            System.out.println("\nEnter path for the file.");
            RandomLine random = new RandomLine();
            String input = userInput.nextLine();
            random.readFile(input);
            String line = random.getTweetText();
            System.out.println(random.getTweetText());
            Tweet(line);
    }

    private static void scheduledTweets() {

        System.out.println("Let's set the file path: ");
        String filePath = userInput.nextLine();
        TimedTweet.setFilePath(filePath);

        try {

            JobDetail job = newJob(TimedTweet.class)
                    .withIdentity("TimedTweet", "group1")
                    .build();

            Date runTime = evenMinuteDate(new Date());

            Trigger trigger = newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startAt(runTime)
                    .withSchedule(simpleSchedule()
                            .withIntervalInMinutes(5)
                            .repeatForever())
                    .build();

            Scheduler sf = new StdSchedulerFactory().getScheduler();
            sf.start();
            sf.scheduleJob(job,trigger);


        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    private static void startInAutomaticMode(String tweetDb, String credsFile) throws Throwable {
        new Tw33ter(tweetDb, credsFile);
    }
}
