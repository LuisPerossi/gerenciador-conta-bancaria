package model;

import exception.OperacaoException;

public class ContaCorrente extends Conta {

    public ContaCorrente(int numero, String titular, double saldo) {
        super(numero, titular, saldo);
    }

    @Override
    public void sacar(double valor) throws OperacaoException {
        if (valor < 0) {
            throw new OperacaoException("O valor do saque deve ser positivo");
        }

        if (valor > this.saldo) {
            throw new OperacaoException("Saldo insuficiente para saque");
        }

        this.saldo -= valor;
    }
}
