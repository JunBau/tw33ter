package junbau.tools;


import junbau.tools.Functions.RandomLine;
import junbau.tools.Functions.TwitterAPI;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Scanner;

public class Main {

    static Scanner userInput = new Scanner(System.in);
    static TwitterAPI tw33t = new TwitterAPI();
    static ConfigurationBuilder cb = new ConfigurationBuilder();

    public static void main(String[] args) {

        System.out.println("Welcome to tw33ter!");
        System.out.println("tw33ter is a tweet scheduler.\n");

        boolean quit = false;
        int choice = 0;

        tw33t.settingKeys(cb);

        TwitterFactory tf = new TwitterFactory(cb.build());

        while (!quit) {

            displayOptions();

            try {
                choice = Integer.parseInt(userInput.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input.");
            } if (choice > 3) {
                System.out.println("Wrong number mate.");
            }

            switch (choice) {
                case 1:
                    postTweet(tf);
                    break;
                case 2:
                    randLine(tf);
                    break;
                case 3:
                    randomTweet(tf);
                    break;
                case 4:
                    returnStatus(tf);
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

    private static void postTweet(TwitterFactory tf) {
        System.out.println("\nEnter your tweet:");
        String input = userInput.nextLine();
        tw33t.Tweet(input, tf);
    }

    private static void randLine(TwitterFactory tf) {
        String file = userInput.nextLine();
        RandomLine random = new RandomLine();
        random.Begin(file, tf);
        System.out.println(random.getTweetText());
    }

    //Takes a random line from a txt file and tweets it.
    private static void randomTweet(TwitterFactory tf) {
        System.out.println("\nEnter path for the file.");
        RandomLine random = new RandomLine();
        String input = userInput.nextLine();
        random.Begin(input, tf);
        String line = random.getTweetText();
        System.out.println(random.getTweetText());
        tw33t.Tweet(line, tf);
    }

    private static void returnStatus(TwitterFactory tf) {
        RandomLine random = new RandomLine();
        random.UniqueString(tf);
    }
}
