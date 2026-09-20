package view.ContaPanels;

import javax.swing.*;
import java.awt.*;

public class OrdenarContasPanel extends JPanel {
    private final JButton botaoOrdenarSaldo = new JButton("Por Saldo (<)");
    private final JButton botaoOrdenarTitular = new JButton("Por Titular (<)");
    private final JButton botaoOrdenarPadrao = new JButton("Ordem Padrão");

    public OrdenarContasPanel() {
        this.setBorder(BorderFactory.createTitledBorder("Filtros"));
        this.setLayout(new GridLayout(3, 1, 5, 5));
        this.add(botaoOrdenarSaldo);
        this.add(botaoOrdenarTitular);
        this.add(botaoOrdenarPadrao);
    }

    public JButton getBotaoOrdenarSaldo() { return botaoOrdenarSaldo; }
    public JButton getBotaoOrdenarTitular() { return botaoOrdenarTitular; }
    public JButton getBotaoOrdenarPadrao() { return botaoOrdenarPadrao; }
}
