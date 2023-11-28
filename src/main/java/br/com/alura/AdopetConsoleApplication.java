package br.com.alura;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import br.com.alura.servicos.AbrigoService;
import br.com.alura.servicos.PetService;

public class AdopetConsoleApplication {

    public static void main(String[] args) {
        PetService petServices = new PetService();
        AbrigoService abrigoServices = new AbrigoService();
        System.out.println("##### BOAS VINDAS AO SISTEMA ADOPET CONSOLE #####");
        try {
            int opcaoEscolhida = 0;
            while (opcaoEscolhida != 5) {
                System.out.println("\nDIGITE O NÚMERO DA OPERAÇÃO DESEJADA:");
                System.out.println("1 -> Listar abrigos cadastrados");
                System.out.println("2 -> Cadastrar novo abrigo");
                System.out.println("3 -> Listar pets do abrigo");
                System.out.println("4 -> Importar pets do abrigo");
                System.out.println("5 -> Sair");

                String textoDigitado = new Scanner(System.in).nextLine();
                opcaoEscolhida = Integer.parseInt(textoDigitado);

                if (opcaoEscolhida == 1) {
                    abrigoServices.listarAbrigos();
                } else if (opcaoEscolhida == 2) {
                    abrigoServices.cadastraAbrigo();
                } else if (opcaoEscolhida == 3) {
                    System.out.println("Digite o id ou nome do abrigo:");
                    String idOuNome = new Scanner(System.in).nextLine();
                    petServices.listaPets(idOuNome);
                } else if (opcaoEscolhida == 4) {
                    System.out.println("Digite o id ou nome do abrigo:");
                    String idOuNome = new Scanner(System.in).nextLine();
                    System.out.println("Digite o nome do arquivo CSV:");
                    String nomeArquivo = new Scanner(System.in).nextLine();

                    petServices.importarPetsCSV(idOuNome, nomeArquivo);

                } else if (opcaoEscolhida == 5) {
                    break;
                } else {
                    System.out.println("NÚMERO INVÁLIDO!");
                    opcaoEscolhida = 0;
                }
            }
            System.out.println("Finalizando o programa...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
