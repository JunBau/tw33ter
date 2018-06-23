package junbau.tools.Functions;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomLine {

    private static String tweetText = "";

    public void Begin(String file, TwitterFactory tf) {
        try {
            FileInputStream fs = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            ArrayList<String> array = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null)
                array.add(line);

            Random rand = new Random();

            int randomIndex = rand.nextInt(array.size());

            String newLine = array.get(randomIndex);

            tweetText = newLine;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String UniqueString (TwitterFactory tf) {
        List<Status> statusList = null;
        Twitter twitter = tf.getInstance();
        try {
            statusList = twitter.getUserTimeline("@TapiJun");
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        for (Status st : statusList) {
            String status = st.getText();
            System.out.println(status);
            return status;
        }
        return null;
    }

    public static String getTweetText() {
        return tweetText;
    }
}
