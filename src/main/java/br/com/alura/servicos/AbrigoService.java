package br.com.alura.servicos;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.client.ClientUtils;
import br.com.alura.domain.Abrigo;

public class AbrigoService {

  HttpClient client = HttpClient.newHttpClient();
  public void listarAbrigos() throws IOException, InterruptedException {
    ClientUtils clientUtils = new ClientUtils(client, "http://localhost:8080/");

    HttpResponse<String> response = clientUtils.disparaGetHTTP("abrigos");
    String responseBody = response.body();
    Abrigo[] abrigos = new ObjectMapper().readValue(responseBody, Abrigo[].class);
    List<Abrigo> abrigosList = Arrays.stream(abrigos).toList();
    System.out.println("Abrigos cadastrados:");
    for (Abrigo abrigo : abrigosList) {
      long id = abrigo.getId();
      String nome = abrigo.getNome();
      System.out.println(id + " - " + nome);
    }
  }

  public void cadastraAbrigo() throws IOException, InterruptedException {
    System.out.println("Digite o nome do abrigo:");
    String nome = new Scanner(System.in).nextLine();
    System.out.println("Digite o telefone do abrigo:");
    String telefone = new Scanner(System.in).nextLine();
    System.out.println("Digite o email do abrigo:");
    String email = new Scanner(System.in).nextLine();

    Abrigo abrigo = new Abrigo(nome, telefone, email);

    ClientUtils clientUtils = new ClientUtils(client, "http://localhost:8080/");
    HttpResponse<String> response = clientUtils.dispararPostHTTP("abrigos", abrigo);

    int statusCode = response.statusCode();
    String responseBody = response.body();
    if (statusCode == 200) {
      System.out.println("Abrigo cadastrado com sucesso!");
      System.out.println(responseBody);
    } else if (statusCode >= 400 && statusCode <= 500) {
      System.out.println("Erro ao cadastrar o abrigo:");
      System.out.println(responseBody);
    }
  }

}
