

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Map;

public class Weather {
    private static final String API_TOKEN = "a0e0636dcd1bb8b330a292b6c053c0b9";
    private static final String URL_FORMAT = "http://api.openweathermap.org/data/2.5/weather?" +
            "lang=ru&" +
            "units=metric&" +
            "q=%s&" +
            "appid=%s";

    public static String getWeather(String city) throws IOException {
        String requestUrl = String.format(URL_FORMAT, city, API_TOKEN);
        StringBuffer buffer = new StringBuffer();

        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        System.out.println("Connection done!");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        //из буфера в строку
        reader.lines().forEach(line -> buffer.append(line));
        reader.close();

        Map<String, Object> fullMap = jsonToMap(buffer.toString());
        Map<String, Object> mainMap = (Map<String, Object>) fullMap.get("main");
        Object weatherMap = ((ArrayList) fullMap.get("weather")).get(0);


        String name = (String) fullMap.get("name");
        double temp = (double) mainMap.get("temp");
        String sign = temp >= 1 ? "+" : "";
        String description = ((Map<String, String>) weatherMap).get("description");

        String result = String.format("%s: %s%d, %s", name, sign, (int) temp, description);
        return result;
    }

    private static Map<String, Object> jsonToMap(String str) {
        Map<String, Object> map = (Map<String, Object>) new Gson().fromJson(str, Object.class);
        return map;
    }
}
