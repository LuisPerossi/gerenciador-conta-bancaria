package view;

import model.Conta;
import model.ContaCorrente;
import service.ContaService;

import javax.swing.*;
import java.awt.*;

public class ContaGUI extends JFrame {
    private DefaultListModel<ContaCorrente> listModel = new DefaultListModel<>();
    private JList<ContaCorrente> contaList = new JList<>(listModel);
    private ContaCorrente contaSelecionada;
    private final ContaService cs;

    public ContaGUI(ContaService cs) {
        //Configurações da janela
        setTitle("Gerenciador de Contas Bancárias");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(350, 400);

        //Inicializando o ContaService e carregando contas
        this.cs = cs;
        carregarContas();

        contaList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) { return; }
            contaSelecionada = contaList.getSelectedValue();
        });

        //Painel da lista de contas
        JScrollPane painelListaContas = new JScrollPane(contaList);
        painelListaContas.setBorder(BorderFactory.createTitledBorder("Contas"));

        //Painel de opções (Botões)
        JButton botaoSacar = new JButton("Sacar");
        JButton botaoDepositar = new JButton("Depositar");
        JButton botaoAdicionarConta = new JButton("Adicionar Conta");

        //Botão de saque
        botaoSacar.addActionListener(e -> {
            if (contaSelecionada == null) {
                MensagemGUI.exibirAlerta("Nenhuma conta selecionada.");
                return;
            }

            String valorSaqueString = JOptionPane.showInputDialog("Valor para saque:");

            try {
                double valorSaque = Double.parseDouble(valorSaqueString);
                contaSelecionada.sacar(valorSaque);
                MensagemGUI.exibirMensagem("Sucesso ao sacar!");
                carregarContas();
                cs.salvarContas();
            } catch (Exception ex) {
                MensagemGUI.exibirErro("Erro ao sacar:\n" + ex.getMessage());
            }
        });

        //Botão de depósito
        botaoDepositar.addActionListener(e -> {
            if (contaSelecionada == null) {
                MensagemGUI.exibirAlerta("Nenhuma conta selecionada.");
                return;
            }

            String valorDepositoString = JOptionPane.showInputDialog("Valor para depósito:");

            try {
                double valorDeposito = Double.parseDouble(valorDepositoString);
                contaSelecionada.depositar(valorDeposito);
                MensagemGUI.exibirMensagem("Sucesso ao depositar!");
                carregarContas();
                cs.salvarContas();
            } catch (Exception ex) {
                MensagemGUI.exibirErro("Erro ao depositar:\n" + ex.getMessage());
            }
        });

        //Botão de adicionar conta
        botaoAdicionarConta.addActionListener(e -> {
            JTextField campoNumero = new JTextField();
            JTextField campoTitular = new JTextField();
            JPanel formulario = new JPanel(new GridLayout(2, 2));

            //Adicionar bloqueio de duplicidade no futuro
            formulario.add(new JLabel("Número da conta:"));
            formulario.add(campoNumero);

            formulario.add(new JLabel("Titular da conta:"));
            formulario.add(campoTitular);

            int resultado = JOptionPane.showConfirmDialog(
                    this,
                    formulario,
                    "Adicionar Conta",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (resultado != JOptionPane.OK_OPTION) { return; }

            try {
                int numero = Integer.parseInt(campoNumero.getText());
                String titular = campoTitular.getText().trim();

                if (titular.isEmpty()) {
                    throw new Exception("O titular não pode ser vazio");
                }

                ContaCorrente c = new ContaCorrente(numero, titular, 0);
                cs.contas.add(c);

                this.carregarContas();
                cs.salvarContas();

                MensagemGUI.exibirMensagem("Conta adicionada com sucesso");
            } catch (Exception ex) {
                MensagemGUI.exibirErro("Erro ao adicionar conta:\n" + ex.getMessage());
            }
        });

        //Painel de opções
        JPanel painelBotoes = new JPanel();
        painelBotoes.setBorder(BorderFactory.createTitledBorder("Opções"));
        painelBotoes.add(botaoAdicionarConta);
        painelBotoes.add(botaoSacar);
        painelBotoes.add(botaoDepositar);

        //Posicionando os painéis
        add(painelListaContas, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void carregarContas() {
        this.listModel.clear();
        for (ContaCorrente c : cs.contas) { listModel.addElement(c); }
    }
}
