import java.io.*;
import java.util.*;
import java.math.BigDecimal;

public class TripPoint {
    
    // Class variables 
    private int time;
    private double lat;
    private double lon;

    private static ArrayList<TripPoint> trip = new ArrayList<>();  //trip points

    // Constructor
    public TripPoint(int time, double lat, double lon) {
        this.time = time;
        this.lat = lat;
        this.lon = lon;
    }

    // Getters
    public int getTime() {
        return time;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    
    public static ArrayList<TripPoint> getTrip() {
        return new ArrayList<>(trip);  // 
    }

    // CSV file reading
    public static void readFile(String filename) throws FileNotFoundException, IOException {
        // erase data of trip ArrayList to load new data 
        trip.clear();

        try (BufferedReader read = new BufferedReader(new FileReader(filename))) {
            String line;
            read.readLine(); 

            while ((line = read.readLine()) != null) {
                String[] data = line.split(",");
                int time = Integer.parseInt(data[0].trim());
                double lat = Double.parseDouble(data[1].trim());
                double lon = Double.parseDouble(data[2].trim());

                // Addition of new TripPoint to trip list
                trip.add(new TripPoint(time, lat, lon));
            }
        }
    }

    // total time(hours)
    public static double totalTime() {
        if (trip.isEmpty()) return 0;
        
        int start = trip.get(0).getTime();
        int end = trip.get(trip.size() - 1).getTime();
        
        return (end - start) / 60.0; // Convert minutes to hours
    }

    // Haversine distance between two points
    public static double haversineDistance(TripPoint a, TripPoint b) {
        final double R = 6371.0; // Earth radius in km
        double lat1 = Math.toRadians(a.getLat());
        double lon1 = Math.toRadians(a.getLon());
        double lat2 = Math.toRadians(b.getLat());
        double lon2 = Math.toRadians(b.getLon());

        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        double aHav = Math.sin(dlat / 2) * Math.sin(dlat / 2)
                    + Math.cos(lat1) * Math.cos(lat2)
                    * Math.sin(dlon / 2) * Math.sin(dlon / 2);

        double cHav = 2 * Math.atan2(Math.sqrt(aHav), Math.sqrt(1 - aHav));

        return R * cHav;  // Distance in kilometers
    }

    // total distance
    public static double totalDistance() {
        double totalDistance = 0;
        for (int i = 0; i < trip.size() - 1; i++) {
            totalDistance += haversineDistance(trip.get(i), trip.get(i + 1));
        }
        return totalDistance;
    }
    public static double avgSpeed(TripPoint a, TripPoint b) {
        // distance between points a and b
        double distance = haversineDistance(a, b);
        //time difference in hours between the two points
        double timeDifference = Math.abs(b.getTime() - a.getTime()) / 60.0;  // Convert minutes to hours
        
        if (timeDifference == 0) {
            return 0; 
        }
        //average speed in km
        return distance / timeDifference; 
    }

}
