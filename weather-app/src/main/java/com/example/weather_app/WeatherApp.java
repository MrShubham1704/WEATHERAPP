package com.example.weather_app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeatherApp {

    public static void main(String[] args) {
        // OpenWeatherMap API Key (Replaced with your key)
        String apiKey = "ef53f1e36825d88bc8bda84e00dbabca"; // Your API key

        // Step 1: Get city name from user input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter city name: ");
        String city = scanner.nextLine();
        scanner.close();

        // API URL
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";

        try {
            // Step 2: Create URL object
            URL url = new URL(apiUrl);

            // Step 3: Open connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Step 4: Check response code
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) { // HTTP OK
                // Step 5: Read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Step 6: Parse JSON response
                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                String cityName = jsonResponse.get("name").getAsString();
                JsonObject main = jsonResponse.getAsJsonObject("main");
                double temperature = main.get("temp").getAsDouble();
                int humidity = main.get("humidity").getAsInt();

                JsonObject weather = jsonResponse.getAsJsonArray("weather").get(0).getAsJsonObject();
                String description = weather.get("description").getAsString();

                // Step 7: Display the weather data
                System.out.println("\nWeather Data for " + cityName + ":");
                System.out.println("Temperature: " + temperature + " °C");
                System.out.println("Humidity: " + humidity + "%");
                System.out.println("Description: " + description);
            } else {
                System.out.println("Error: Unable to fetch data. HTTP Response Code: " + responseCode);
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}