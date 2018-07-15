package uk.junbau.tools.JSON;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import twitter4j.Twitter;

import java.io.InputStreamReader;

public class TwitterCredentials implements ITwitterCredentials {
    private String consumerKey;
    private String consumerSecret;
    private String userToken;
    private String secretKey;

    private static Gson gson = new GsonBuilder().create();

    @Override
    public String getConsumerKey() {
        return consumerKey;
    }

    @Override
    public String getConsumerSecret() {
        return consumerSecret;
    }

    @Override
    public String getUserToken() {
        return userToken;
    }

    @Override
    public String getSecretKey() {
        return secretKey;
    }

    public static ITwitterCredentials deserialize(InputStreamReader in) {
        return gson.fromJson(in, TwitterCredentials.class);
    }
}
