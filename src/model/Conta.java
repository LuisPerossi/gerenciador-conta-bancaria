package model;

import exception.OperacaoException;

public abstract class Conta {
    protected int numero;
    protected String titular;
    protected double saldo;

    public Conta(int numero, String titular, double saldo) {
        this.numero = numero;
        this.titular = titular;
        this.saldo = saldo;
    }

    public void depositar(double valor) throws OperacaoException {
        if (valor < 0) {
            throw new OperacaoException("O depósito deve ser positivo");
        }

        this.saldo += valor;
        System.out.println("Depósito realizado com sucesso");
    }

    public abstract void sacar(double valor) throws OperacaoException;

    public void imprimirDados() {
        System.out.println("Dados da conta:");
        System.out.println("Nº: " + this.numero);
        System.out.println("Titular: " + this.titular);
        System.out.println("Saldo: " + this.saldo);
    }

    public int getNumero() {
        return numero;
    }

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }
}
