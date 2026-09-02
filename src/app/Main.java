import exception.OperacaoException;
import model.ContaCorrente;
import service.ContaService;

import javax.swing.*;

void main() {
    ContaService cs = new ContaService();

    try {
        ContaCorrente conta = cs.lerConta("conta.txt");

        JOptionPane.showMessageDialog(
                null,
                "Conta carregada:" +
                "\nNº: " + conta.getNumero() +
                "\nTitular: " + conta.getTitular() +
                "\nSaldo: " + conta.getSaldo()
                );

        String saqueStr = JOptionPane.showInputDialog("Insira um valor para saque:");
        double saque = Double.parseDouble(saqueStr);

        try {
            conta.sacar(saque);
            JOptionPane.showMessageDialog(null, "Saque realizado com sucesso");
        } catch (OperacaoException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Erro ao sacar: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        cs.salvarConta("conta_atualizada.txt", conta);

    } catch (IOException e) {
        JOptionPane.showMessageDialog(
                null,
                "Erro ao acessar arquivo: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
