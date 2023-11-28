package br.com.alura.servicos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.alura.servicos.AbrigoService;

public class PetService {
  public void listaPets(String idOuNome) throws IOException, InterruptedException {
    AbrigoService abrigo = new AbrigoService();
    HttpClient client = HttpClient.newHttpClient();
    String uri = "http://localhost:8080/abrigos/" + idOuNome + "/pets";
    HttpResponse<String> response = abrigo.disparaGetHTTP(client, uri);
    int statusCode = response.statusCode();
    if (statusCode == 404 || statusCode == 500) {
      System.out.println("ID ou nome não cadastrado!");
      return;
    }
    String responseBody = response.body();
    JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
    System.out.println("Pets cadastrados:");
    for (JsonElement element : jsonArray) {
      JsonObject jsonObject = element.getAsJsonObject();
      long id = jsonObject.get("id").getAsLong();
      String tipo = jsonObject.get("tipo").getAsString();
      String nome = jsonObject.get("nome").getAsString();
      String raca = jsonObject.get("raca").getAsString();
      int idade = jsonObject.get("idade").getAsInt();
      System.out.println(id + " - " + tipo + " - " + nome + " - " + raca + " - " + idade + " ano(s)");
    }
  }

  public void importarPetsCSV(String idOuNome, String nomeArquivo)
      throws NumberFormatException, IOException, InterruptedException {

    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(nomeArquivo));
    } catch (IOException e) {
      System.out.println("Erro ao carregar o arquivo: " + nomeArquivo);
      return;
    }
    String line;
    while ((line = reader.readLine()) != null) {
      String[] campos = line.split(",");
      String tipo = campos[0];
      String nome = campos[1];
      String raca = campos[2];
      int idade = Integer.parseInt(campos[3]);
      String cor = campos[4];
      Float peso = Float.parseFloat(campos[5]);

      JsonObject json = new JsonObject();
      json.addProperty("tipo", tipo.toUpperCase());
      json.addProperty("nome", nome);
      json.addProperty("raca", raca);
      json.addProperty("idade", idade);
      json.addProperty("cor", cor);
      json.addProperty("peso", peso);

      HttpClient client = HttpClient.newHttpClient();
      String uri = "http://localhost:8080/abrigos/" + idOuNome + "/pets";
      AbrigoService abrigo = new AbrigoService();
      HttpResponse<String> response = abrigo.dispararPostHTTP(client, uri, json);
      int statusCode = response.statusCode();
      String responseBody = response.body();
      if (statusCode == 200) {
        System.out.println("Pet cadastrado com sucesso: " + nome);
      } else if (statusCode == 404) {
        System.out.println("Id ou nome do abrigo não encontado!");
        break;
      } else if (statusCode == 400 || statusCode == 500) {
        System.out.println("Erro ao cadastrar o pet: " + nome);
        System.out.println(responseBody);
        break;
      }
    }
    reader.close();
  }
}
