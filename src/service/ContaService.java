package service;

import dao.ContaDAO;
import exception.CampoVazioException;
import exception.ContaNaoEncontradaException;
import exception.SaldoInsuficienteException;
import exception.ValorInvalidoException;
import model.ContaCorrente;

import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class ContaService {
    private ArrayList<ContaCorrente> contas = new ArrayList<>();

    public void carregarContas() throws SQLException {
        this.contas = ContaDAO.listar();
    }

    public List<ContaCorrente> getContas() {
        return this.contas;
    }

    public void depositar(int numero, double valor)
            throws ValorInvalidoException, ContaNaoEncontradaException, SaldoInsuficienteException, SQLException
    {
        if (valor <= 0) { throw new ValorInvalidoException("O valor deve ser positivo."); }

        ContaCorrente conta = this.buscarPorNumero(numero);
        if (conta == null) { throw new ContaNaoEncontradaException("Conta não encontrada."); }
        double saldo = conta.getSaldo();

        ContaDAO.depositar(numero, valor);
        conta.setSaldo(saldo + valor);
    }

    public void sacar(int numero, double valor)
            throws ValorInvalidoException, ContaNaoEncontradaException, SaldoInsuficienteException, SQLException
    {
        if (valor <= 0) { throw new ValorInvalidoException("O valor deve ser positivo."); }

        ContaCorrente conta = this.buscarPorNumero(numero);
        if (conta == null) { throw new ContaNaoEncontradaException("Conta não encontrada."); }

        double saldo = conta.getSaldo();
        if (valor > saldo) { throw new SaldoInsuficienteException("Saldo insuficiente."); }

        ContaDAO.sacar(numero, valor);
        conta.setSaldo(saldo - valor);
    }

    public void adicionarConta(String titular, double saldo)
            throws CampoVazioException, ValorInvalidoException, SQLException
    {
        if (titular.isBlank()) { throw new CampoVazioException("O titular não pode ser vazio."); }
        if (saldo < 0) { throw new ValorInvalidoException("O saldo não pode ser negativo."); }

        int numero = ContaDAO.inserir(titular, saldo);
        this.contas.add(new ContaCorrente(numero, titular, saldo));
    }

    public void removerConta(int numero) throws ContaNaoEncontradaException, SQLException {
        ContaCorrente conta = this.buscarPorNumero(numero);
        if (conta == null) { throw new ContaNaoEncontradaException("Conta não encontrada."); }

        ContaDAO.remover(numero);
        this.contas.remove(conta);
    }

    public void transferir(int origem, int destino, double valor)
        throws ValorInvalidoException, ContaNaoEncontradaException, SaldoInsuficienteException, SQLException
    {
        if (valor <= 0) { throw new ValorInvalidoException("O valor deve ser positivo."); }

        ContaCorrente contaOrigem = this.buscarPorNumero(origem);
        if (contaOrigem == null) { throw new ContaNaoEncontradaException("Conta de origem não encontrada."); }

        ContaCorrente contaDestino = this.buscarPorNumero(destino);
        if (contaDestino == null) { throw new ContaNaoEncontradaException("Conta de destino não encontrada."); }

        if (valor > contaOrigem.getSaldo()) { throw new SaldoInsuficienteException("Saldo insuficiente."); }

        ContaDAO.transferir(origem, destino, valor);
        contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
        contaDestino.setSaldo(contaDestino.getSaldo() + valor);
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

