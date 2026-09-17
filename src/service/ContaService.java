package service;

import exception.CarregarContasException;
import exception.SalvarContasException;
import model.Conta;
import model.ContaCorrente;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ContaService {
    public ArrayList<ContaCorrente> contas = new ArrayList<>();
    private final Path caminhoLeitura = Paths.get("contas.txt");
    private final Path caminhoSalvamento = Paths.get("contas_atualizadas.txt");

    public void lerContas() throws CarregarContasException {
        try {
            List<String> linhas = Files.readAllLines(this.caminhoLeitura);

            for (String linha : linhas) {
                String[] dados = linha.split(",");

                int numero = Integer.parseInt(dados[0]);
                String titular = dados[1].trim();
                double saldo = Double.parseDouble(dados[2]);

                this.contas.add(new ContaCorrente(numero, titular, saldo));
            }
        } catch (Exception e) {
            throw new CarregarContasException(e.getMessage());
        }
    }

    public void salvarContas() throws SalvarContasException {
        try {
            List<String> linhas = new ArrayList<>();

            for (Conta c : contas) {
                String dados = c.getNumero() + "," + c.getTitular() + "," + c.getSaldo();
                linhas.add(dados);
            }

            Files.write(caminhoSalvamento, linhas);
        } catch (Exception e) {
            throw new SalvarContasException(e.getMessage());
        }
    }
}

/*
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
 */
