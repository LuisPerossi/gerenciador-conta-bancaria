package service;

import model.Conta;
import strategy.TarifaStrategy;

public class TarifaService {
    public static double calcularTarifa(Conta conta, TarifaStrategy strategy) {
        return strategy.calcular(conta);
    }
}
