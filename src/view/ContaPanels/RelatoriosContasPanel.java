package view.ContaPanels;

import model.ContaCorrente;
import service.ContaService;
import view.MensagemGUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class RelatoriosContasPanel extends JPanel {
    private final ContaService cs;

    private final JButton botaoFiltrarDez = new JButton("Filtrar > 10.000");
    private final JButton botaoTotal = new JButton("Calcular Total");
    private final JButton botaoAgrupar = new JButton("Agrupar Saldos");
    private final JButton botaoFiltrarCinco = new JButton("Filtrar > 5.000");
    private final JButton botaoFiltrarPar = new JButton("Filtrar Nº Par");

    public RelatoriosContasPanel(ContaService cs) {
        this.cs = cs;
        this.configurarBotoes();
        this.setBorder(BorderFactory.createTitledBorder("Relatórios"));

        this.add(botaoFiltrarCinco);
        this.add(botaoFiltrarDez);
        this.add(botaoFiltrarPar);
        this.add(botaoAgrupar);
        this.add(botaoTotal);
    }

    private void configurarBotoes() {
        //Filtrar saldo > 5.000
        botaoFiltrarCinco.addActionListener(e -> {
            Predicate<ContaCorrente> predicate = (c) -> c.getSaldo() > 5000;
            List<ContaCorrente> contasFiltradas = this.cs.filtrarContas(predicate);

            StringBuilder resultado = new StringBuilder();
            for (ContaCorrente c : contasFiltradas) { resultado.append(c).append("\n"); }

            this.exibirResultado(resultado.toString(), "Contas com saldo maior que R$5.000,00:");
        });

        //Filtrar saldo > 10.000
        botaoFiltrarDez.addActionListener(e -> {
            Predicate<ContaCorrente> predicate = (c) -> c.getSaldo() > 10000;
            List<ContaCorrente> contasFiltradas = this.cs.filtrarContas(predicate);

            StringBuilder resultado = new StringBuilder();
            for (ContaCorrente c : contasFiltradas) { resultado.append(c).append("\n"); }

            this.exibirResultado(resultado.toString(), "Contas com saldo maior que R$10.000,00:");
        });

        //Filtrar numeros pares
        botaoFiltrarPar.addActionListener(e -> {
            Predicate<ContaCorrente> predicate = (c) -> c.getNumero() % 2 == 0;
            List<ContaCorrente> contasFiltradas = this.cs.filtrarContas(predicate);

            StringBuilder resultado = new StringBuilder();
            for (ContaCorrente c : contasFiltradas) { resultado.append(c).append("\n"); }

            this.exibirResultado(resultado.toString(), "Contas com números pares:");
        });

        //Calcular total
        botaoTotal.addActionListener(e -> {
            double total = this.cs.calcularTotal();
            MensagemGUI.exibirMensagem(String.format("Saldo total: R$%.2f.", total));
        });

        //Agrupar por saldo
        botaoAgrupar.addActionListener(e -> {
            Map<String, List<ContaCorrente>> contasAgrupadas = this.cs.agruparSaldos();
            StringBuilder resultado = new StringBuilder();

            contasAgrupadas.forEach((grupo, contas) -> {
                resultado.append(String.format("==== %s ====", grupo)).append("\n");
                for (ContaCorrente c : contas) { resultado.append(c).append("\n"); }
            });

            this.exibirResultado(resultado.toString(), "Contas agrupadas por saldo:");
        });
    }

    private void exibirResultado(String resultado, String titulo) {
        JTextArea areaTexto = new JTextArea(resultado);
        areaTexto.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(areaTexto);
        scrollPane.setPreferredSize(new Dimension(300, 250));

        MensagemGUI.exibirObjeto(scrollPane, titulo);
    }
}
