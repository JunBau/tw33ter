package uk.junbau.tools.JSON;

import com.google.gson.*;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.time.Clock;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

public class ScheduledTweets {
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(ScheduledTweets.class, new ScheduledTweets.ScheduledTweetsAdapter())
            .create();

    private List<ITweet> scheduledTweets = new LinkedList<>();

    public ScheduledTweets() {}

    public List<ITweet> getScheduledTweets() {
        return scheduledTweets;
    }

    public String serialize() {
        return gson.toJson(this, ScheduledTweets.class);
    }

    public static ScheduledTweets deserialize(InputStreamReader input) {
        return gson.fromJson(input, ScheduledTweets.class);
    }

    public static class ScheduledTweetsAdapter  implements JsonDeserializer<ScheduledTweets>, JsonSerializer<ScheduledTweets> {
        @Override
        public ScheduledTweets deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) {
            ScheduledTweets ret = new ScheduledTweets();
            JsonArray arr = json.getAsJsonArray();
            Instant now = Clock.systemUTC().instant();
            for (JsonElement elem : arr) {
                JsonObject item = elem.getAsJsonObject();
                Instant date = Instant.ofEpochSecond(item.get("scheduledTime").getAsLong());
                if (!now.isAfter(date))
                    ret.scheduledTweets.add(new Tweet(item.get("msg").getAsString(), date));
            }
            return ret;
        }

        @Override
        public JsonElement serialize(ScheduledTweets tweets, Type type, JsonSerializationContext ctx) {
            JsonArray ret = new JsonArray();
            Instant now = Clock.systemUTC().instant();
            for (ITweet tweet : tweets.getScheduledTweets()) {
                if (tweet.getScheduledTime().isBefore(now))
                    continue;
                JsonObject elem = new JsonObject();
                elem.add("scheduledTime", new JsonPrimitive(tweet.getScheduledTime().getEpochSecond()));
                elem.add("msg", new JsonPrimitive(tweet.getMsg()));
                ret.add(elem);
            }
            return ret;
        }
    }
}
