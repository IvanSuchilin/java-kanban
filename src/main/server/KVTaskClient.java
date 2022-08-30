package main.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private String apiToken;

    public KVTaskClient(String url) {

        URI registerUrl = URI.create(url + "/register");
        HttpRequest request = createRequest(registerUrl, "GET", null);
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse.BodyHandler<String> responseBodyHandler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, responseBodyHandler);
            apiToken = response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + registerUrl + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String getApiToken() {
        return apiToken;
    }

    public void put(String key, String json, String url) {
        URI putUrl = createUrl(url, "/save/", key);
        HttpRequest request = createRequest(putUrl, "POST", json);
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse.BodyHandler<String> responseBodyHandler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = client.send(request, responseBodyHandler);
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + putUrl + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String load(String key, String url) {
        String responseReturn = null;
        URI loadUrl = createUrl(url, "/load/", key);
        HttpRequest request = createRequest(loadUrl, "GET", null);
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                if (response.statusCode() > 499) {
                    System.out.println("Во время загрузки на сервере возникли проблемы");
                } else {
                    System.out.println("Проверьте значение URL и key и повторите запрос");
                }
                return null;
            }
            if (response.statusCode() == 200) {
                JsonElement jsonElement = JsonParser.parseString(response.body());
                if (!jsonElement.isJsonObject() && !jsonElement.isJsonArray()) {
                    System.out.println("Ответ от сервера не соответствует ожидаемому.");
                    return null;
                }
                responseReturn = String.valueOf(jsonElement.getAsJsonArray());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + loadUrl + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return responseReturn;
    }

    private URI createUrl(String url, String path, String key) {
        return URI.create(url + path + key + "?API_TOKEN=" + apiToken);
    }

    private HttpRequest createRequest(URI url, String method, String json) {
        HttpRequest request = null;
        if (method.equals("GET")) {
            request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();
        } else {
            request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
        }
        return request;
    }
}

