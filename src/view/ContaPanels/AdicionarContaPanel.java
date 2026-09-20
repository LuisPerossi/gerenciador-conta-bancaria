package view.ContaPanels;

import javax.swing.*;
import java.awt.*;

public class AdicionarContaPanel extends JPanel {
    private final JTextField campoTitular = new JTextField();
    private final JTextField campoSaldo = new JTextField();

    public AdicionarContaPanel() {
        this.setLayout(new GridLayout(2,2));
        this.add(new JLabel("Nome do titular:"));
        this.add(campoTitular);
        this.add(new JLabel("Saldo da conta:"));
        this.add(campoSaldo);
    }

    public double getSaldo() { return Double.parseDouble(campoSaldo.getText()); }
    public String getTitular() {
        return campoTitular.getText().trim();
    }
}