package uk.junbau.tools.Functions;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class RandomLine {

    private ArrayList<String> tweetArray = new ArrayList<>();
    private Random rand = new Random();

    public void readFile(String file) {
        try {
            FileInputStream fs = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));

            String line;

            while ((line = br.readLine()) != null)
                tweetArray.add(line);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTweetText() {
        if(!tweetArray.isEmpty()) {
            int randomIndex = rand.nextInt(tweetArray.size());
            return tweetArray.get(randomIndex);
        } else {
            return null;
        }
    }

    public void printTweetArray() {
        System.out.println(" ");
        for (int i = 0; i < tweetArray.size() ; i++) {
            System.out.println(i + ": " + tweetArray.get(i));
        }
        System.out.println(" ");
    }

    public void removeTweet(String tweetIn) {
        System.out.println("Removing " + tweetArray.get(findTweet(tweetIn)));
        tweetArray.remove(findTweet(tweetIn));
    }

    private int findTweet(String tweetIn) {
        for (int i = 0; i < tweetArray.size(); i++) {
            String tweet = tweetArray.get(i);

            if (tweet.equals(tweetIn)) {
                return i;
            }
        }
        return -1;
    }
}
