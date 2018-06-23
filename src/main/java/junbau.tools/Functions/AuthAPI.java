package junbau.tools.Functions;

public class AuthAPI {

    private static String consumerKey = "Your key here.";
    private static String consumerSecret = "App secret here.";
    private static String userToken = "UserToken here.";
    private static String secretKey = "Secret key here.";

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
