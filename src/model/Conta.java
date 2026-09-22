package model;

import exception.SaldoInsuficienteException;
import exception.ValorInvalidoException;

public abstract class Conta {
    protected int numero;
    protected String titular;
    protected double saldo;

    public Conta(int numero, String titular, double saldo) {
        this.numero = numero;
        this.titular = titular;
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return String.format(
                "Nº: %d - Titular: %s - Saldo: RS%.2f",
                this.numero, this.titular, this.saldo
        );
    }

    public void depositar(double valor) throws ValorInvalidoException {
        if (valor < 0) {
            throw new ValorInvalidoException("O valor do depósito deve ser positivo.");
        }

        this.saldo += valor;
    }

    public abstract void sacar(double valor) throws SaldoInsuficienteException, ValorInvalidoException;

    public int getNumero() {
        return numero;
    }
    public String getTitular() {
        return titular;
    }
    public double getSaldo() {
        return saldo;
    }
    public void setSaldo(double saldo) { this.saldo = saldo; }
}
