package service;

import model.ContaCorrente;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ContaService {
    public ContaCorrente lerConta(String caminho) throws IOException {
        String linha = Files.readString(Paths.get(caminho));
        String[] dados = linha.split(",");
        int numero = Integer.parseInt(dados[0]);
        String titular = dados[1].trim();
        double saldo = Double.parseDouble(dados[2]);
        return new ContaCorrente(numero, titular, saldo);
    }

    public void salvarConta(String caminho, ContaCorrente conta) throws IOException {
        String dados = conta.getNumero() + "," + conta.getTitular() + "," + conta.getSaldo();
        Files.write(Paths.get(caminho), dados.getBytes());
    }
}
