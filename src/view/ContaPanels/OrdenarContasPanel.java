package view.ContaPanels;

import model.ContaCorrente;
import service.ContaService;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

public class OrdenarContasPanel extends JPanel {
    private final ListaContasPanel listaContasPanel;
    private final ContaService cs;

    private final JButton botaoOrdenarSaldo = new JButton("Por Saldo (>)");
    private final JButton botaoOrdenarTitular = new JButton("Por Titular (<)");
    private final JButton botaoOrdenarPadrao = new JButton("Por Número (<)");

    public OrdenarContasPanel(ListaContasPanel listaContasPanel, ContaService cs) {
        this.listaContasPanel = listaContasPanel;
        this.cs = cs;

        this.configurarBotoes();
        this.setBorder(BorderFactory.createTitledBorder("Ordenação"));
        this.setLayout(new GridLayout(3, 1, 5, 5));

        this.add(botaoOrdenarSaldo);
        this.add(botaoOrdenarTitular);
        this.add(botaoOrdenarPadrao);
    }

    private void configurarBotoes() {
        //Ordenar por saldo decrescente
        botaoOrdenarSaldo.addActionListener(e -> {
            Comparator<ContaCorrente> comparator =
                    (a, b) -> Double.compare(b.getSaldo(), a.getSaldo());

            this.listaContasPanel.carregarContas(this.cs.ordenarContas(comparator));
        });

        //Ordenar por titular crescente
        botaoOrdenarTitular.addActionListener(e -> {
            Comparator<ContaCorrente> comparator =
                    (a, b) ->
                            String.CASE_INSENSITIVE_ORDER.compare(a.getTitular(), b.getTitular());

            this.listaContasPanel.carregarContas(this.cs.ordenarContas(comparator));
        });

        //Ordenar por número (padrão)
        botaoOrdenarPadrao.addActionListener(e -> this.listaContasPanel.carregarContas(this.cs.contas));
    }
}
