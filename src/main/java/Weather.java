import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Weather {
//передаем сообщение и модель

    //fcb165f4e7374c840326c72cbc652f86
    //message это город который идет в сообщении
    public static String getWeather(String message, Model model) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=fcb165f4e7374c840326c72cbc652f86");
//получаем контекст
        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";//помещаем результат
        while (in.hasNext()) {//пробигаемся сканером
            result += in.nextLine();//сохраняем результат в переменную
        }

        JSONObject object = new JSONObject(result);
        model.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");//получили малеький обьект json "main"
        model.setTemp(main.getDouble("temp"));// получаем температуру маленького обьект json
        model.setHumidity(main.getDouble("humidity"));// получаем влажность маленького обьект json

        JSONArray getArray = object.getJSONArray("weather");//инициализировали массив json "weather"
        for (int i = 0; i < getArray.length(); i++) {//пробегамся по массиву json "weather"
            JSONObject obj = getArray.getJSONObject(i);//натыкаемся на обьект, и заходим в модель json "weather"
            model.setIcon((String) obj.get("icon"));//берем значения и забиваем в модель
            model.setMain((String) obj.get("main"));
        }

        return "City: " + model.getName() + "\n" +
                "Temperature: " + model.getTemp() + "C" + "\n" +
                "Humidity:" + model.getHumidity() + "%" + "\n" +
                "Main: " + model.getMain() + "\n" +
                "http://openweathermap.org/img/w/" + model.getIcon() + ".png";
    }
}
