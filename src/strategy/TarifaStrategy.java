package strategy;

import model.Conta;

public enum TarifaStrategy {
    FIXA {
        @Override
        public double calcular(Conta conta) {
            return 10.00;
        }
    },

    PERCENTUAL {
        @Override
        public double calcular(Conta conta) {
            return conta.getSaldo() * 0.01;
        }
    },

    ISENTA {
        @Override
        public double calcular(Conta conta) {
            return 0.00;
        }
    };

    public abstract double calcular(Conta conta);
}
