package br.com.alura.servicos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.client.ClientUtils;
import br.com.alura.domain.Pet;

public class PetService {
  HttpClient client = HttpClient.newHttpClient();
  public void listaPets(String idOuNome) throws IOException, InterruptedException {
    ClientUtils clientServices = new ClientUtils(client, "http://localhost:8080");
    String uri = "/abrigos/" + idOuNome + "/pets";
    HttpResponse<String> response = clientServices.disparaGetHTTP(uri);
    int statusCode = response.statusCode();
    if (statusCode == 404 || statusCode == 500) {
      System.out.println("ID ou nome não cadastrado!");
      return;
    }
    String responseBody = response.body();
    Pet[] pets = new ObjectMapper().readValue(responseBody, Pet[].class);
    List<Pet> petList = Arrays.stream(pets).toList();
    System.out.println("Pets cadastrados:");
    for (Pet pet : petList) {
      long id = pet.getId();
      String tipo = pet.getTipo();
      String nome = pet.getNome();
      String raca = pet.getRaca();
      String idade = pet.getNome();
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

      Pet pet = new Pet(tipo.toUpperCase(), nome, raca, idade, cor, peso);

      ClientUtils clientServices = new ClientUtils(client, "http://localhost:8080");
      String uri = "/abrigos/" + idOuNome + "/pets";
      HttpResponse<String> response = clientServices.dispararPostHTTP(uri, pet);
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
