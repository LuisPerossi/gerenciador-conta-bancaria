package view.ContaPanels;

import model.ContaCorrente;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ListaContasPanel extends JPanel {
    private final DefaultListModel<ContaCorrente> listModel = new DefaultListModel<>();
    private final JList<ContaCorrente> contaList = new JList<>(listModel);

    public ListaContasPanel() {
        this.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(contaList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Contas"));
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void carregarContas(List<ContaCorrente> contas) {
        this.listModel.clear();
        for (ContaCorrente c : contas) { this.listModel.addElement(c); }
    }

    public ContaCorrente getContaSelecionada() { return this.contaList.getSelectedValue(); }
}
