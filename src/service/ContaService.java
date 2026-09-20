package service;

import dao.ContaDAO;
import exception.CarregarContasException;
import exception.SalvarContasException;
import model.Conta;
import model.ContaCorrente;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class ContaService {
    public ArrayList<ContaCorrente> contas = new ArrayList<>();

    public void carregarContas() throws SQLException {
        this.contas = ContaDAO.listar();
    }

    public List<ContaCorrente> filtrarMaiorDezMil() {
        return this.contas
            .stream()
            .filter(c -> c.getSaldo() > 10000)
            .toList();
    }

    public double calcularTotal() {
        return this.contas
            .stream()
            .map(ContaCorrente::getSaldo)
            .reduce(0.0, Double::sum);
    }

    public Map<String, List<ContaCorrente>> agruparSaldos() {
        return contas.stream()
            .collect(groupingBy(
                c -> {
                    if (c.getSaldo() <= 5000) return "(a) Até R$5.000";
                    if (c.getSaldo() <= 10000) return "(b) De R$5.001 à R$10.000";
                    return "(c) Acima de 10.000";
                },
                TreeMap::new,
                toList()
            ));
    }

    public List<ContaCorrente> filtrarMaiorCincoMil() {
        Predicate<ContaCorrente> maiorCincoMil = (c) -> c.getSaldo() > 5000;
        return this.contas.stream().filter(maiorCincoMil).toList();
    }

    public List<ContaCorrente> filtrarNumeroPar() {
        Predicate<ContaCorrente> numeroPar = (c) -> c.getNumero() % 2 == 0;
        return this.contas.stream().filter(numeroPar).toList();
    }

    public List<ContaCorrente> ordenarPorSaldoDecrescente() {
        Comparator<Conta> porSaldoDecrescente = (a, b) -> Double.compare(a.getSaldo(), b.getSaldo());
        return this.contas.stream().sorted(porSaldoDecrescente).toList();
    }

    public List<ContaCorrente> ordenarPorTitular() {
        Comparator<ContaCorrente> porTitular =
                (a, b) ->
                        String.CASE_INSENSITIVE_ORDER.compare(a.getTitular(), b.getTitular());
        return this.contas.stream().sorted(porTitular).toList();
    }
}

