package com.example.runningtracker;

import android.util.Log;

import static java.lang.Math.*;

public class Config {

    public static final int timeInterval = 1;
    public static final int distInterval = 1;

    public static final String discardMessage = "Are you sure you want to discard this run?";
    public static final String discardConfirmation = "Confirm Discard";

    public static final String progressFilter = "com.example.runningtracker.SEND_PROGRESS";

    public static final String duration = "duration";
    public static final String distance = "distance";
    public static final String pace = "pace";

    // approximate earth radius in metres
    public static final int earthRadius = 6378137;

    public static final String[] dayOfWeek = {
            "EMPTY",
            "SUNDAY",
            "MONDAY",
            "TUESDAY",
            "WEDNESDAY",
            "THURSDAY",
            "FRIDAY",
            "SATURDAY"
    };

    public static final String[] monthName = {
            "JANUARY",
            "FEBRUARY",
            "MARCH",
            "APRIL",
            "MAY",
            "JUNE",
            "JULY",
            "AUGUST",
            "SEPTEMBER",
            "OCTOBER",
            "NOVEMBER",
            "DECEMBER"
    };

    public static final String getDistanceString(int dist) {
        String res = (dist/1000) + "." + (dist%1000/100) + (dist%100/10);
        return res;
    }

    public static final String getPaceString(int p) {
        String res = (p/60) + "\'" + (p%60) + "\"";
        return res;
    }

    public static final String getDurationString(int dur) {
        String res = dur/60 + ":" + (dur % 60 < 10 ? "0"+(dur%60):(dur%60));
        return res;
    }

    /**
     * calculate distance in metres between two coordinates
     *
     * gathered from
     * https://www.movable-type.co.uk/scripts/latlong.html
     *
     * Haversine formula
     *
     * @param lat1 old latitude
     * @param lon1 old longitude
     * @param lat2 new latitude
     * @param lon2 new longitude
     * @return distance in metres
     */
    public static final int calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = toRadians(lat2 - lat1);
        double dLon = toRadians(lon2 - lon1);

        double a = sin(dLat/2) * sin(dLat/2) +
                    cos(toRadians(lat1)) * cos(toRadians(lat2)) *
                    sin(dLon/2) * sin(dLon/2);

        double c = 2 * atan2(sqrt(a), sqrt(1-a));

        Log.i("CONFIG", "distance = " + earthRadius*c);
        return (int)(earthRadius * c);
    }
}
