package view.ContaPanels;

import javax.swing.*;

public class RelatoriosContasPanel extends JPanel {
    private final JButton botaoFiltrarDez = new JButton("Filtrar > 10.000");
    private final JButton botaoTotal = new JButton("Calcular Total");
    private final JButton botaoAgrupar = new JButton("Agrupar Saldos");
    private final JButton botaoFiltrarCinco = new JButton("Filtrar > 5.000");
    private final JButton botaoFiltrarPar = new JButton("Filtrar Nº Par");

    public RelatoriosContasPanel() {
        this.setBorder(BorderFactory.createTitledBorder("Relatórios"));
        this.add(botaoFiltrarDez);
        this.add(botaoTotal);
        this.add(botaoAgrupar);
        this.add(botaoFiltrarCinco);
        this.add(botaoFiltrarPar);
    }

    public JButton getBotaoFiltrarDez() { return botaoFiltrarDez; }
    public JButton getBotaoTotal() { return botaoTotal; }
    public JButton getBotaoAgrupar() { return botaoAgrupar; }
    public JButton getBotaoFiltrarCinco() { return botaoFiltrarCinco; }
    public JButton getBotaoFiltrarPar() { return botaoFiltrarPar; }
}
