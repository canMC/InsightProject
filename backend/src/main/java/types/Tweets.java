package types;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Tweets {
    
    public final String location;
    public final String name;
    public final String text;
//    public final Date timestamp;
    
    private static Random random = new Random();
    
    public Tweets(String location, String name, String text) {
        this.location = location;
        this.name = name;
        this.text = text;
//        Calendar ct = Calendar.getInstance();
        // Delay data by a random (Gaussian) amount of seconds.  99.7% of data come within 3 seconds
//        ct.add(Calendar.SECOND, (int) Math.round(Math.min(0, random.nextGaussian())));
//        timestamp = ct.getTime();
    }
    
    public String toString() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(this);
    }
    
    public static Tweets fromString(String record) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.fromJson(record, Tweets.class);
    }
}