package view.ContaPanels;

import javax.swing.*;
import java.awt.*;

public class AdicionarContaPanel extends JPanel {
    private final JTextField campoNumero = new JTextField();
    private final JTextField campoTitular = new JTextField();

    public AdicionarContaPanel() {
        this.setLayout(new GridLayout(2,2));
        this.add(new JLabel("Número da conta:"));
        this.add(campoNumero);
        this.add(new JLabel("Nome do titular:"));
        this.add(campoTitular);
    }

    public int getNumero() {
        return Integer.parseInt(campoNumero.getText());
    }
    public String getTitular() {
        return campoTitular.getText().trim();
    }
}