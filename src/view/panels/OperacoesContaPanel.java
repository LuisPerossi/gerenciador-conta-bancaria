package view.panels;

import exception.CampoVazioException;
import exception.ContaNaoEncontradaException;
import exception.SaldoInsuficienteException;
import exception.ValorInvalidoException;
import model.ContaCorrente;
import service.ContaService;
import service.TarifaService;
import strategy.TarifaStrategy;
import view.MensagemGUI;

import javax.swing.*;
import java.sql.SQLException;

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

    private ContaCorrente getContaSelecionada() {
        ContaCorrente conta = this.listaContasPanel.getContaSelecionada();
        if (conta == null) { MensagemGUI.exibirAlerta("Nenhuma conta selecionada"); }
        return conta;
    }

    private void recarregarContas() {
        this.listaContasPanel.carregarContas(this.cs.getContas());
    }

    private void configurarBotoes() {
        //Saque
        botaoSacar.addActionListener(e -> {
            try {
                ContaCorrente conta = this.getContaSelecionada();
                if (conta == null) { return; }
                int numero = conta.getNumero();

                String valorString = MensagemGUI.receberInput("Valor para saque:");
                if (valorString == null) { return; }

                double valor = Double.parseDouble(valorString);
                this.cs.sacar(numero, valor);

                this.recarregarContas();
                MensagemGUI.exibirMensagem("Sucesso ao sacar!");

            } catch (NumberFormatException ex) {
                MensagemGUI.exibirErro("Erro ao converter valor:\n" + ex.getMessage());
            } catch (ValorInvalidoException | ContaNaoEncontradaException | SaldoInsuficienteException ex) {
                MensagemGUI.exibirErro("Erro ao sacar:\n" + ex.getMessage());
            } catch (SQLException ex) {
                MensagemGUI.exibirErro("Erro de banco de dados:\n" + ex.getMessage());
            }
        });

        //Depósito
        botaoDepositar.addActionListener(e -> {
            try {
                ContaCorrente conta = this.getContaSelecionada();
                if (conta == null) { return; }
                int numero = conta.getNumero();

                String valorString = MensagemGUI.receberInput("Valor para depósito:");
                if (valorString == null) { return; }

                double valor = Double.parseDouble(valorString);
                this.cs.depositar(numero, valor);

                this.recarregarContas();
                MensagemGUI.exibirMensagem("Sucesso ao depositar!");

            } catch (NumberFormatException ex) {
                MensagemGUI.exibirErro("Erro ao converter valor:\n" + ex.getMessage());
            } catch (ValorInvalidoException | ContaNaoEncontradaException | SaldoInsuficienteException ex) {
                MensagemGUI.exibirErro("Erro ao depositar:\n" + ex.getMessage());
            } catch (SQLException ex) {
                MensagemGUI.exibirErro("Erro de banco de dados:\n" + ex.getMessage());
            }
        });

        //Adicionar conta
        botaoAdicionar.addActionListener(e -> {
            try {
                AdicionarContaPanel adicionarContaPanel = new AdicionarContaPanel();

                int resultado = JOptionPane.showConfirmDialog(
                        this.getParent(),
                        adicionarContaPanel,
                        "Adicionar Conta",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (resultado != JOptionPane.OK_OPTION) { return; }

                String titular = adicionarContaPanel.getTitular();
                double saldo = adicionarContaPanel.getSaldo();

                this.cs.adicionarConta(titular, saldo);
                this.recarregarContas();

                MensagemGUI.exibirMensagem("Conta adicionada com sucesso!");
            } catch (NumberFormatException ex) {
                MensagemGUI.exibirErro("Erro ao converter valor:\n" + ex.getMessage());
            } catch (CampoVazioException | ValorInvalidoException ex) {
                MensagemGUI.exibirErro("Erro ao adicionar conta:\n" + ex.getMessage());
            } catch (SQLException ex) {
                MensagemGUI.exibirErro("Erro de banco de dados:\n" + ex.getMessage());
            }
        });

        //Remover conta
        botaoRemover.addActionListener(e -> {
            try {
                ContaCorrente conta = this.getContaSelecionada();
                if (conta == null) { return; }
                int numero = conta.getNumero();

                int confirm = MensagemGUI.confirmar("Remover conta?\n" + conta);
                if (confirm != JOptionPane.OK_OPTION) { return; }

                this.cs.removerConta(numero);
                this.recarregarContas();

                MensagemGUI.exibirMensagem("Conta removida com sucesso!");
            }  catch (ContaNaoEncontradaException ex) {
                MensagemGUI.exibirErro("Erro ao remover conta:\n" + ex.getMessage());
            } catch (SQLException ex) {
                MensagemGUI.exibirErro("Erro de banco de dados:\n" + ex.getMessage());
            }
        });

        //Calcular tarifa
        botaoTarifa.addActionListener(e -> {
            ContaCorrente conta = this.getContaSelecionada();
            if (conta == null) { return; }

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

            double tarifa = TarifaService.calcularTarifa(conta.getSaldo(), strategy);

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

        //Transferência
        botaoTransefrir.addActionListener(e -> {
            try {
                ContaCorrente contaOrigem = this.getContaSelecionada();
                if (contaOrigem == null) { return; }
                int numeroOrigem = contaOrigem.getNumero();

                String numeroDestinoString = MensagemGUI.receberInput("Número da conta de destino:");
                if (numeroDestinoString == null) { return; }
                int numeroDestino = Integer.parseInt(numeroDestinoString);

                String valorString = MensagemGUI.receberInput("Valor para transferência:");
                if (valorString == null) { return; }
                double valor = Double.parseDouble(valorString);

                this.cs.transferir(numeroOrigem, numeroDestino, valor);
                this.recarregarContas();

                MensagemGUI.exibirMensagem("Transferência realizada com sucesso!");
            } catch (NumberFormatException ex) {
                MensagemGUI.exibirErro("Erro ao converter valor:\n" + ex.getMessage());
            } catch (ValorInvalidoException | ContaNaoEncontradaException | SaldoInsuficienteException ex) {
                MensagemGUI.exibirErro("Erro ao transferir:\n" + ex.getMessage());
            } catch (SQLException ex) {
                MensagemGUI.exibirErro("Erro de banco de dados:\n" + ex.getMessage());
            }
        });
    }
}
