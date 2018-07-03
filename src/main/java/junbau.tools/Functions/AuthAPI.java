package junbau.tools.Functions;

public class AuthAPI {

    // Shouldn't really store keys within the code, will re-evaluate.

    private static String consumerKey = "YOUR APP KEY HERE";
    private static String consumerSecret = "YOUR APP SECRET KEY HERE";
    private static String userToken = "YOUR USER TOKEN HERE";
    private static String secretKey = "YOUR USER SECRET KEY HERE";

    public static String getUserToken() {
        return userToken;
    }

    public static String getSecretKey() {
        return secretKey;
    }

    public static String getConsumerKey() {
        return consumerKey;
    }

    public static String getConsumerSecret() {
        return consumerSecret;
    }
}
