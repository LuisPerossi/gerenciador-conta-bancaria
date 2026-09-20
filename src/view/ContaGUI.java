package view;

import exception.OperacaoException;
import exception.SalvarContasException;
import model.ContaCorrente;
import service.ContaService;
import service.TarifaService;
import strategy.TarifaStrategy;
import view.ContaPanels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;

public class ContaGUI extends JFrame {
    private final ContaService cs;
    private final ListaContasPanel listaContasPanel;
    private final OperacoesContaPanel operacoesContaPanel;
    private final RelatoriosContasPanel relatoriosContasPanel;
    private final OrdenarContasPanel ordenarContasPanel;

    public ContaGUI(ContaService cs) {
        //Inicializando o ContaService
        this.cs = cs;

        //Configurações da janela
        this.setTitle("Gerenciador de Contas Bancárias");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setSize(650, 400);
        this.setResizable(false);

        //Criando painéis
        listaContasPanel = new ListaContasPanel();
        operacoesContaPanel = new OperacoesContaPanel();
        relatoriosContasPanel = new RelatoriosContasPanel();
        ordenarContasPanel = new OrdenarContasPanel();

        //Configurando a GUI
        listaContasPanel.carregarContas(cs.contas);
        this.configurarEventos();

        //Adicionando painéis
        this.add(operacoesContaPanel, BorderLayout.NORTH);
        this.add(ordenarContasPanel, BorderLayout.WEST);
        this.add(listaContasPanel, BorderLayout.CENTER);
        this.add(relatoriosContasPanel, BorderLayout.SOUTH);

        //Adicionando um listener para salvar as contas ao fechar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            try {
                cs.salvarContas();
                dispose();
            } catch (SalvarContasException ex) {
                MensagemGUI.exibirErro("Erro ao salvar contas:\n" + ex.getMessage());
            }
            }
        });
    }

    private void configurarEventos() {
        //OperacoesContaPanel
        operacoesContaPanel.getBotaoSacar().addActionListener(this::sacar);
        operacoesContaPanel.getBotaoDepositar().addActionListener(this::depositar);
        operacoesContaPanel.getBotaoTarifa().addActionListener(this::calcularTarifa);
        operacoesContaPanel.getBotaoAdicionar().addActionListener(this::adicionarConta);
        //RelatoriosContasPanel
        relatoriosContasPanel.getBotaoFiltrarDez().addActionListener(this::filtrarDezMil);
        relatoriosContasPanel.getBotaoTotal().addActionListener(this::calcularTotal);
        relatoriosContasPanel.getBotaoAgrupar().addActionListener(this::agruparSaldos);
        relatoriosContasPanel.getBotaoFiltrarCinco().addActionListener(this::filtrarCincoMil);
        relatoriosContasPanel.getBotaoFiltrarPar().addActionListener(this::filtrarNumeroPar);
        //OrdenarContasPanel
        ordenarContasPanel.getBotaoOrdenarSaldo().addActionListener(this::ordenarPorSaldo);
        ordenarContasPanel.getBotaoOrdenarTitular().addActionListener(this::ordenarPorTitular);
        ordenarContasPanel.getBotaoOrdenarPadrao().addActionListener(this::ordenarPadrao);
    }

    private void sacar(ActionEvent e) {
        ContaCorrente conta = listaContasPanel.getContaSelecionada();

        if (conta == null) {
            MensagemGUI.exibirAlerta("Nenhuma conta selecionada.");
            return;
        }

        try {
          String valorSaqueString = MensagemGUI.receberInput("Valor para saque:");
          if (valorSaqueString == null) { return; }

          double valorSaque = Double.parseDouble(valorSaqueString);

          conta.sacar(valorSaque);
          listaContasPanel.carregarContas(cs.contas);

          MensagemGUI.exibirMensagem("Sucesso ao sacar!");
        } catch (OperacaoException ex) {
            MensagemGUI.exibirErro("Erro de operação:\n" + ex.getMessage());
        } catch (Exception ex) {
            MensagemGUI.exibirErro("Erro ao sacar:\n" + ex.getMessage());
        }
    }

    private void depositar(ActionEvent e) {
        ContaCorrente conta = listaContasPanel.getContaSelecionada();

        if (conta == null) {
            MensagemGUI.exibirAlerta("Nenhuma conta selecionada.");
            return;
        }

        try {
            String valorDepositoString = MensagemGUI.receberInput("Valor para depósito:");
            if (valorDepositoString == null) { return; }

            double valorDeposito = Double.parseDouble(valorDepositoString);

            conta.depositar(valorDeposito);
            listaContasPanel.carregarContas(cs.contas);

            MensagemGUI.exibirMensagem("Sucesso ao depositar!");
        } catch (OperacaoException ex) {
            MensagemGUI.exibirErro("Erro de operação:\n" + ex.getMessage());
        } catch (Exception ex) {
            MensagemGUI.exibirErro("Erro ao depositar:\n" + ex.getMessage());
        }
    }

    private void adicionarConta(ActionEvent e) {
        AdicionarContaPanel adicionarContaPanel = new AdicionarContaPanel();

        int resultado = JOptionPane.showConfirmDialog(
                this,
                adicionarContaPanel,
                "Adicionar Conta",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resultado != JOptionPane.OK_OPTION) { return; }

        try {
            int numero = adicionarContaPanel.getNumero();
            String titular = adicionarContaPanel.getTitular();

            if (titular.isEmpty()) { throw new Exception("O titular não pode ser vazio."); }
            cs.contas.add(new ContaCorrente(numero, titular, 0));
            listaContasPanel.carregarContas(cs.contas);

            MensagemGUI.exibirMensagem("Conta adicionada com sucesso!");
        } catch (Exception ex) {
            MensagemGUI.exibirErro("Erro ao adicionar conta:\n" + ex.getMessage());
        }
    }

    private void filtrarDezMil(ActionEvent e) {
        List<ContaCorrente> contasFiltradas = cs.filtrarMaiorDezMil();

        StringBuilder resultado = new StringBuilder();
        for (ContaCorrente c : contasFiltradas) {
            resultado.append(c).append("\n");
        }

        JTextArea areaTexto = new JTextArea(resultado.toString());
        areaTexto.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(areaTexto);
        scrollPane.setPreferredSize(new Dimension(300, 250));

        MensagemGUI.exibirObjeto(scrollPane, "Contas com saldo maior que R$10.000,00");
    }

    private void calcularTotal(ActionEvent e) {
        double total = cs.calcularTotal();
        String mensagem = String.format("Saldo total: R$%.2f", total);
        MensagemGUI.exibirMensagem(mensagem);
    }

    private void agruparSaldos(ActionEvent e) {
        Map<String, List<ContaCorrente>> contasAgrupadas = cs.agruparSaldos();
        StringBuilder resultado = new StringBuilder();

        contasAgrupadas.forEach((grupo, contas) -> {
            resultado.append(String.format("==== %s ====", grupo)).append("\n");

            for (ContaCorrente c : contas) {
                resultado.append(c).append("\n");
            }
        });

        JTextArea areaTexto = new JTextArea(resultado.toString());
        areaTexto.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(areaTexto);
        scrollPane.setPreferredSize(new Dimension(300, 250));

        MensagemGUI.exibirObjeto(scrollPane, "Contas agrupadas por saldo");
    }

    private void filtrarCincoMil(ActionEvent e) {
        List<ContaCorrente> contasFiltradas = cs.filtrarMaiorCincoMil();

        StringBuilder resultado = new StringBuilder();
        for (ContaCorrente c : contasFiltradas) {
            resultado.append(c).append("\n");
        }

        JTextArea areaTexto = new JTextArea(resultado.toString());
        areaTexto.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(areaTexto);
        scrollPane.setPreferredSize(new Dimension(300, 250));

        MensagemGUI.exibirObjeto(scrollPane, "Contas com saldo maior que R$5.000,00");
    }

    private void filtrarNumeroPar(ActionEvent e) {
        List<ContaCorrente> contasFiltradas = cs.filtrarNumeroPar();

        StringBuilder resultado = new StringBuilder();
        for (ContaCorrente c : contasFiltradas) {
            resultado.append(c).append("\n");
        }

        JTextArea areaTexto = new JTextArea(resultado.toString());
        areaTexto.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(areaTexto);
        scrollPane.setPreferredSize(new Dimension(300, 250));

        MensagemGUI.exibirObjeto(scrollPane, "Contas com número par");
    }


    private void ordenarPorSaldo(ActionEvent e) {
        this.listaContasPanel.carregarContas(this.cs.ordenarPorSaldoDecrescente());
    }

    private void ordenarPorTitular(ActionEvent e) {
        this.listaContasPanel.carregarContas(this.cs.ordenarPorTitular());
    }

    private void ordenarPadrao(ActionEvent e) {
        this.listaContasPanel.carregarContas(this.cs.contas);
    }

    private void calcularTarifa(ActionEvent e) {
        ContaCorrente conta = listaContasPanel.getContaSelecionada();

        if (conta == null) {
            MensagemGUI.exibirAlerta("Nenhuma conta selecionada.");
            return;
        }

        String[] opcoes = { "(a) Fixa", "(b) Percentual", "(c) Isenta" };

        int resultado = MensagemGUI.receberEscolha(
                opcoes,
                "Estratégia da tarifa",
                "Escolha uma estratégia:"
        );

        if (resultado == -1) { return; }

        TarifaStrategy strategy = switch (resultado) {
            case 0 -> TarifaStrategy.FIXA;
            case 1 -> TarifaStrategy.PERCENTUAL;
            default -> TarifaStrategy.ISENTA;
        };

        double tarifa = TarifaService.calcularTarifa(conta, strategy);

        String mensagem = String.format(
                """
                Conta: %d
                Titular: %s
                Saldo: %.2f
                Tarifa: %.2f
                Final: %.2f
                """,
                conta.getNumero(),
                conta.getTitular(),
                conta.getSaldo(),
                tarifa,
                conta.getSaldo() + tarifa
        );

        MensagemGUI.exibirMensagem(mensagem);
    }
}