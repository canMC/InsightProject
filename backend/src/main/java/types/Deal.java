package types;

import java.util.Calendar;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Deal {


    public final String departureAirportLocation;
    public final String departureAirportCode;
    public final String arrivalAirportLocation;
    public final String arrivalAirportCode;
    public final String airlineName;
    public final String departureTimeRaw;
    public final String arrivalTimeRaw;
    public final String totalFare;
    public final Date timestamp;

    public Deal(String departureAirportLocation, String departureAirportCode, String arrivalAirportLocation, String arrivalAirportCode, String airlineName,
                String departureTimeRaw, String arrivalTimeRaw, String totalFare) {
        this.departureAirportLocation = departureAirportLocation;
        this.departureAirportCode = departureAirportCode;
        this.arrivalAirportLocation = arrivalAirportLocation;
        this.arrivalAirportCode = arrivalAirportCode;
        this.airlineName = airlineName;
        this.departureTimeRaw = departureTimeRaw;
        this.arrivalTimeRaw = arrivalTimeRaw;
        this.totalFare = totalFare;

        Calendar ct = Calendar.getInstance();
        timestamp = ct.getTime();
    }

    public String toString() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(this);
    }

    public static Deal fromString(String record) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        Deal deal = null;
        try {
            deal = gson.fromJson(record, Deal.class);
        } catch (Exception e) {
            deal = new Deal("Error", "Error", "Error", "Error", "Error", "Error", "Error", "Error");
        }
        return deal;
    }

}
