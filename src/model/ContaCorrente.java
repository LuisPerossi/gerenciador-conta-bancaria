package model;

import exception.SaldoInsuficienteException;
import exception.ValorNegativoException;

public class ContaCorrente extends Conta {

    public ContaCorrente(int numero, String titular, double saldo) {
        super(numero, titular, saldo);
    }

    @Override
    public void sacar(double valor) throws SaldoInsuficienteException, ValorNegativoException {
        if (valor < 0) {
            throw new ValorNegativoException("O valor do saque deve ser positivo.");
        }

        if (valor > this.saldo) {
            throw new SaldoInsuficienteException("Saldo insuficiente para saque.");
        }

        this.saldo -= valor;
    }
}
