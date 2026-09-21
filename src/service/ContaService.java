package service;

import dao.ContaDAO;
import model.ContaCorrente;

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

    public ContaCorrente buscarPorNumero(int numero) {
        Predicate<ContaCorrente> porNumero = c -> c.getNumero() == numero;
        return contas.stream().filter(porNumero).findFirst().orElse(null);
    }

    public List<ContaCorrente> filtrarContas(Predicate<ContaCorrente> predicate) {
        return this.contas.stream().filter(predicate).toList();
    }

    public List<ContaCorrente> ordenarContas(Comparator<ContaCorrente> comparator) {
        return this.contas.stream().sorted(comparator).toList();
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
}

