package main.Server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
//http://localhost:8078/
    URI url;
    private final String API_TOKEN;
    
    
    public URI getUrl() {

        return url;
    }

    public String getApiToken() {
        return API_TOKEN;
    }

    

    public KVTaskClient(String url) throws IOException, InterruptedException {
        this.url = URI.create(url);
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                // указываем HTTP-метод запроса
                .uri(this.url) // указываем адрес ресурса
                .GET()
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос
        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();
        try {
            // HTTP-клиент с настройками по умолчанию

            // получаем стандартный обработчик тела запроса с конвертацией содержимого в строку
            HttpResponse.BodyHandler<String> responseBodyHandler = HttpResponse.BodyHandlers.ofString();

            // отправляем запрос и получаем ответ от сервера
            HttpResponse<String> response = client.send(request, responseBodyHandler);
            API_TOKEN = response.body();
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    void put(String key, String json){
        URI putUrl = URI.create("http://localhost:8078/save/" + key +"?API_TOKEN=" + API_TOKEN);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(this.url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse.BodyHandler<String> responseBodyHandler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, responseBodyHandler);
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    String load(String key){
        URI putUrl = URI.create("http://localhost:8078/save/" + key +"?API_TOKEN=" + API_TOKEN);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(this.url)
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonElement jsonElement = JsonParser.parseString(response.body());
                if(!jsonElement.isJsonObject()) { // проверяем, точно ли мы получили JSON-объект
                    System.out.println("Ответ от сервера не соответствует ожидаемому.");
                    return null;
                }

                JsonObject jsonObject = jsonElement.getAsJsonObject();
////НАДО ДОПИСАТЬ
                return response;
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }
    }
}
