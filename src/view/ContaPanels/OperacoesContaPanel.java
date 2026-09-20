package view.ContaPanels;

import javax.swing.*;

public class OperacoesContaPanel extends JPanel {
    private final JButton botaoSacar = new JButton("Sacar");
    private final JButton botaoDepositar = new JButton("Depositar");
    private final JButton botaoTarifa = new JButton("Calcular Tarifa");
    private final JButton botaoAdicionar = new JButton("Adicionar Conta");

    public OperacoesContaPanel() {
        this.setBorder(BorderFactory.createTitledBorder("Operações"));
        this.add(botaoSacar);
        this.add(botaoDepositar);
        this.add(botaoTarifa);
        this.add(botaoAdicionar);
    }

    public JButton getBotaoSacar() { return botaoSacar; }
    public JButton getBotaoDepositar() { return botaoDepositar; }
    public JButton getBotaoTarifa() { return botaoTarifa; }
    public JButton getBotaoAdicionar() { return botaoAdicionar; }
}
