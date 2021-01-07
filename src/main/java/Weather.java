

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class Weather {
    private static final String API_TOKEN = "a0e0636dcd1bb8b330a292b6c053c0b9";
    private static final String URL_CURRENT = "http://api.openweathermap.org/data/2.5/weather?" +
            "lang=ru&" +
            "units=metric&" +
            "q=%s&" +
            "appid=%s";
    private static final String URL_FORECAST = "https://api.openweathermap.org/data/2.5/onecall?" +
            "lang=ru&" +
            "units=metric&" +
            "lat=50.0&lon=36.0&" +
            "exclude=hourly&" +
            "appid=%s";

    public static String getCurrentWeather(String city) throws IOException {
        //формирование строки запроса
        String requestUrl = String.format(URL_CURRENT, city, API_TOKEN);

        //получение соединения
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        System.out.println("Connection done!");

        //положить результат запроса в буферирированный поток
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer buffer = new StringBuffer();
        //запись из потока в строковый буфер
        reader.lines().forEach(line -> buffer.append(line));
        reader.close();
        //из JSON-строки в Карту (набор ключ-значение)
        JSONObject jsonObject = new JSONObject(buffer.toString());
        Map<String, Object> fullMap = jsonObject.toMap();
        //извлечение из карты нужных данных
        Map<String, Object> mainMap = (Map<String, Object>) fullMap.get("main");
        Object weatherMap = ((ArrayList) fullMap.get("weather")).get(0);
        //название города
        String name = (String) fullMap.get("name");
        //температура
        Object temp = mainMap.get("temp");
        if (temp instanceof Double) {
            temp = ((Double) temp).intValue();
        }

        //знак температуры
        String sign = (int) temp >= 1 ? "+" : "";
        //описание погоды
        String description = ((Map<String, String>) weatherMap).get("description");

        //формирование строки с информацией о погоде
        String result = String.format("%s: %s%d, %s", name, sign, (int) temp, description);

        return result;
    }

    public static String getForecastWeather(String city) throws IOException {
        String requestUrl = String.format(URL_FORECAST, API_TOKEN);
        String result = "тут будет прогноз";
        return result;
    }
}
