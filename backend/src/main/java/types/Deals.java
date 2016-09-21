package types;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Deals {
    
    public final String departureAirportLocation;
    public final String departureAirportCode;
    public final String arrivalAirportLocation;
    public final String arrivalAirportCode;
    public final String airlineName;
    public final String departureTimeRaw;
    public final String arrivalTimeRaw;
    public final String totalFare;
//    public final Date timestamp;
    
    private static Random random = new Random();
    
    public Deals(String departureAirportLocation, String departureAirportCode, String arrivalAirportLocation, String arrivalAirportCode, String airlineName, String departureTimeRaw, String arrivalTimeRaw, String totalFare) {
        this.departureAirportLocation = departureAirportLocation;
        this.departureAirportCode = departureAirportCode;
        this.arrivalAirportLocation = arrivalAirportLocation;
        this.arrivalAirportCode = arrivalAirportCode;
        this.airlineName = airlineName;
        this.departureTimeRaw = departureTimeRaw;
        this.arrivalTimeRaw = arrivalTimeRaw;
        this.totalFare = totalFare;
//        Calendar ct = Calendar.getInstance();
        // Delay data by a random (Gaussian) amount of seconds.  99.7% of data come within 3 seconds
//        ct.add(Calendar.SECOND, (int) Math.round(Math.min(0, random.nextGaussian())));
//        timestamp = ct.getTime();
    }
    
    public String toString() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(this);
    }
    
    public static Deals fromString(String record) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.fromJson(record, Deals.class);
    }
}