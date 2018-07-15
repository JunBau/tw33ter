package uk.junbau.tools.Functions;

public class AuthAPI {

    // Shouldn't really store keys within the code, will re-evaluate.

    private static String consumerKey = "YOUR APP KEY HERE";
    private static String consumerSecret = "YOUR APP SECRET KEY HERE";
    private static String userToken = "YOUR USER TOKEN HERE";
    private static String secretKey = "YOUR USER SECRET KEY HERE";

    protected static String getUserToken() {
        return userToken;
    }
    protected static String getSecretKey() {
        return secretKey;
    }
    protected static String getConsumerKey() {
        return consumerKey;
    }
    protected static String getConsumerSecret() {
        return consumerSecret;
    }
}
