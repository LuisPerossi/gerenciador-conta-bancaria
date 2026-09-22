package service;

import strategy.TarifaStrategy;

public class TarifaService {
    public static double calcularTarifa(double valor, TarifaStrategy strategy) {
        return strategy.calcular(valor);
    }
}
