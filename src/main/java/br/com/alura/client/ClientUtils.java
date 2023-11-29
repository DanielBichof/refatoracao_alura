package br.com.alura.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;

public class ClientUtils {
  HttpClient client;
  public String API_URI;

  public ClientUtils(HttpClient client, String API_URI) {
    this.client = client;
    this.API_URI = API_URI;
  }

  public HttpResponse<String> disparaGetHTTP(String path) throws IOException, InterruptedException {
    String full_url = API_URI + path;

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(full_url))
        .method("GET", HttpRequest.BodyPublishers.noBody())
        .build();
    return client.send(request, HttpResponse.BodyHandlers.ofString());
  }

  public HttpResponse<String> dispararPostHTTP(String path, Object object) throws IOException, InterruptedException {
    String full_url = API_URI + path;
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(full_url))
        .header("Content-Type", "application/json")
        .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(object)))
        .build();

    return client.send(request, HttpResponse.BodyHandlers.ofString());
  }

}
