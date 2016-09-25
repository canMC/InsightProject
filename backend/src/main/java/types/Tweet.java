package types;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Tweet {

    public final String location;
    public final String name;
    public final String text;
    public final Date timestamp;

    public Tweet(String location, String name, String text) {
        this.location = location;
        this.name = name;
        this.text = text;

        Calendar ct = Calendar.getInstance();
        timestamp = ct.getTime();
    }

    public String toString() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(this);
    }

    public static Tweet fromString(String record) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        Tweet tweet = null;
        try {
            tweet = gson.fromJson(record, Tweet.class);
        } catch (Exception e) {
            tweet = new Tweet("Error", "Error", "Error");
        }
        return tweet;
    }
}
