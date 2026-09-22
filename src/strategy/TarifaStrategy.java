package strategy;

public enum TarifaStrategy {
    FIXA {
        @Override
        public double calcular(double valor) {
            return 10.00;
        }
    },

    PERCENTUAL {
        @Override
        public double calcular(double valor) {
            return valor * 0.01;
        }
    },

    ISENTA {
        @Override
        public double calcular(double valor) {
            return 0.00;
        }
    };

    public abstract double calcular(double valor);
}
