package view.ContaPanels;

import dao.ContaDAO;
import exception.CampoVazioException;
import exception.ContaNaoEncontradaException;
import exception.ValorNegativoException;
import model.ContaCorrente;
import service.ContaService;
import service.TarifaService;
import strategy.TarifaStrategy;
import view.MensagemGUI;

import javax.swing.*;

public class OperacoesContaPanel extends JPanel {
    private final ListaContasPanel listaContasPanel;
    private final ContaService cs;

    private final JButton botaoSacar = new JButton("Sacar");
    private final JButton botaoDepositar = new JButton("Depositar");
    private final JButton botaoTransefrir = new JButton("Transferir");
    private final JButton botaoTarifa = new JButton("Calcular Tarifa");
    private final JButton botaoAdicionar = new JButton("Adicionar Conta");
    private final JButton botaoRemover = new JButton("Remover conta");

    public OperacoesContaPanel(ListaContasPanel listaContasPanel, ContaService cs) {
        this.listaContasPanel = listaContasPanel;
        this.cs = cs;

        this.configurarBotoes();
        this.setBorder(BorderFactory.createTitledBorder("Operações"));

        this.add(botaoSacar);
        this.add(botaoDepositar);
        this.add(botaoTransefrir);
        this.add(botaoTarifa);
        this.add(botaoAdicionar);
        this.add(botaoRemover);
    }

    private void configurarBotoes() {
        //Saque
        botaoSacar.addActionListener(e -> {
            try {
                ContaCorrente conta = this.listaContasPanel.getContaSelecionada();
                if (conta == null) {
                    MensagemGUI.exibirAlerta("Nenhuma conta selecionada.");
                    return;
                }

                String valorString = MensagemGUI.receberInput("Valor para saque:");
                if (valorString == null) { return; }

                double valor = Double.parseDouble(valorString);

                conta.sacar(valor);
                ContaDAO.atualizarSaldo(conta.getNumero(), conta.getSaldo());

                this.listaContasPanel.carregarContas(this.cs.contas);
                MensagemGUI.exibirMensagem("Sucesso ao sacar!");
            } catch (Exception ex) {
                MensagemGUI.exibirErro("Erro ao sacar:\n" + ex.getMessage());
            }
        });

        //Depósito
        botaoDepositar.addActionListener(e -> {
            try {
                ContaCorrente conta = this.listaContasPanel.getContaSelecionada();
                if (conta == null) {
                    MensagemGUI.exibirAlerta("Nenhuma conta selecionada.");
                    return;
                }

                String valorString = MensagemGUI.receberInput("Valor para deposito:");
                if (valorString == null) { return; }

                double valor = Double.parseDouble(valorString);

                conta.depositar(valor);
                ContaDAO.atualizarSaldo(conta.getNumero(), conta.getSaldo());

                this.listaContasPanel.carregarContas(this.cs.contas);
                MensagemGUI.exibirMensagem("Sucesso ao depositar!");
            } catch (Exception ex) {
                MensagemGUI.exibirErro("Erro ao depositar:\n" + ex.getMessage());
            }
        });

        //Transferência
        botaoTransefrir.addActionListener(e -> {
            try {
                ContaCorrente contaOrigem = this.listaContasPanel.getContaSelecionada();
                if (contaOrigem == null) {
                    MensagemGUI.exibirAlerta("Nenhuma conta selecionada.");
                    return;
                }

                String numeroDestinoString = MensagemGUI.receberInput("Número da conta de destino:");
                if (numeroDestinoString == null) { return; }
                int numeroDestino = Integer.parseInt(numeroDestinoString);

                String valorString = MensagemGUI.receberInput("Valor para transferência:");
                if (valorString == null) { return; }
                double valor = Double.parseDouble(valorString);

                ContaCorrente contaDestino = this.cs.buscarPorNumero(numeroDestino);
                if (contaDestino == null) {
                    throw new ContaNaoEncontradaException(String.format(
                            "Não foi possível encontrar a conta de destino: %s.", numeroDestino
                    ));
                }

                contaOrigem.sacar(valor);
                contaDestino.depositar(valor);
                ContaDAO.transferir(contaOrigem.getNumero(), numeroDestino, valor);
                listaContasPanel.carregarContas(this.cs.contas);

                MensagemGUI.exibirMensagem("Sucesso ao transferir!");
            } catch (Exception ex) {
                MensagemGUI.exibirErro("Erro ao transferir:\n" + ex.getMessage());
            }
        });

        //Calcular tarifa
        botaoTarifa.addActionListener(e -> {
            ContaCorrente conta = this.listaContasPanel.getContaSelecionada();

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
                    Saldo: R$%.2f
                    Tarifa: R$%.2f
                    ----- // -----
                    Final: R$%.2f
                    """,
                    conta.getNumero(),
                    conta.getTitular(),
                    conta.getSaldo(),
                    tarifa,
                    conta.getSaldo() + tarifa
            );

            MensagemGUI.exibirMensagem(mensagem);
        });

        //Adicionar conta
        botaoAdicionar.addActionListener(e -> {
            AdicionarContaPanel adicionarContaPanel = new AdicionarContaPanel();

            int resultado = JOptionPane.showConfirmDialog(
                    this.getParent(),
                    adicionarContaPanel,
                    "Adicionar Conta",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (resultado != JOptionPane.OK_OPTION) { return; }

            try {
                String titular = adicionarContaPanel.getTitular();
                double saldo = adicionarContaPanel.getSaldo();

                if (titular.isEmpty()) { throw new CampoVazioException("O titular não pode ser vazio."); }
                if (saldo < 0) { throw new ValorNegativoException("O saldo não pode ser negativo."); }

                int numero = ContaDAO.inserir(new ContaCorrente(-1, titular, saldo));
                this.cs.contas.add(new ContaCorrente(numero, titular, saldo));
                this.listaContasPanel.carregarContas(this.cs.contas);

                MensagemGUI.exibirMensagem("Conta adicionada com sucesso!");
            } catch (Exception ex) {
                MensagemGUI.exibirErro("Erro ao adicionar conta:\n" + ex.getMessage());
            }
        });

        //Remover conta
        botaoRemover.addActionListener(e -> {
            ContaCorrente conta = this.listaContasPanel.getContaSelecionada();

            if (conta == null) {
                MensagemGUI.exibirAlerta("Nenhuma conta selecionada.");
                return;
            }

            try {
                int confirm = MensagemGUI.confirmar("Remover conta?\n" + conta);
                if (confirm != JOptionPane.OK_OPTION) { return; }

                ContaDAO.remover(conta.getNumero());
                this.cs.contas.remove(conta);
                this.listaContasPanel.carregarContas(cs.contas);

                MensagemGUI.exibirMensagem("Conta removida com sucesso!");
            } catch (Exception ex) {
                MensagemGUI.exibirErro("Erro ao remover conta: " + ex.getMessage());
            }
        });
    }
}
