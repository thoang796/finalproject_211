package com.google.api_client.geoLocation;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.simple.JSONArray;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class App 
{
    public static void main( String[] args )  {
        System.out.println( "Hello World! " ); // just to check whether
        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=George+Mason+University&key=API_KEY");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            con.disconnect();
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------\n");
            System.out.println("This is JSON file that will use to get our current Location : \n");
            System.out.println(content.toString()+" \n");

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(content.toString());


            JSONArray arr = (JSONArray) json.get("results");
            JSONObject results = (JSONObject) arr.get(0);
            JSONObject geometry = (JSONObject) results.get("geometry");
            JSONObject location = (JSONObject) geometry.get("location");
            Object lng = location.get("lng");

            Object lat = location.get("lat");

            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------\n");
            System.out.println("Current Location coordinates are : (Latitude : "+lat.toString()+") " + ",(Longitude : " + lng.toString()+")");

            // TODO: Make the http request to get nearby places
            URL urlSchools = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat.toString()+","+lng.toString()+"&radius=24140.16&type=school&key=AIzaSyCkf0g-y0n_OWMG5FXaSHBwOYpiNeiC3oY");
            HttpURLConnection con1 = (HttpURLConnection) urlSchools.openConnection();
            con1.setRequestMethod("GET");

            int status1 = con1.getResponseCode();

            BufferedReader in1 = new BufferedReader(
                    new InputStreamReader(con1.getInputStream()));
            String inputLine1;
            StringBuffer schoolCon = new StringBuffer();
            while ((inputLine1 = in1.readLine()) != null) {
                schoolCon.append(inputLine1);
            }
            in1.close();

            con1.disconnect();
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------\n");
            System.out.println("This is JSON file that will use to get the all the schools nearby : \n");
            System.out.println(schoolCon.toString()+"\n");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------\n");



            // TODO: Parse the new json to get the coordinates

            JSONParser schoolParser = new JSONParser();
            JSONObject schoolJson = (JSONObject) schoolParser.parse(schoolCon.toString());

            JSONArray schoolArr = (JSONArray) schoolJson.get("results");
            Iterator it = schoolArr.iterator();

            while(it.hasNext()) {
                JSONObject results1 = (JSONObject) it.next();
                //JSONObject results1 =  (JSONObject) schoolArr.get(0) ;
                String schoolName = (String)results1.get("name");

               System.out.println("School name : "+schoolName);

                JSONObject geometry1 = (JSONObject) results1.get("geometry");
                JSONObject location1 = (JSONObject) geometry1.get("location");

                Object sLng = location1.get("lng");

                Object sLat = location1.get("lat");

                // TODO: Calculate the distance between each school and gmu and print to user

                System.out.println("School coordinates are : (Latitude : "+sLat.toString()+") " + ",(Longitude : " + sLng.toString()+")");
                double schoolDist = distance(Double.parseDouble(lat.toString()) ,Double.parseDouble(sLat.toString()), Double.parseDouble(lng.toString()),Double.parseDouble(sLng.toString()));
                System.out.println("Distance from George Mason University :"+ schoolDist+" meters (Approx)");
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------\n");

            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("its working fine!! Peace! ");
        }
    }
    // Method to get the Approximate distance between our current Location and nearby Voting centres/schools
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2) {

        final int R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    } //end distance
}//end class
