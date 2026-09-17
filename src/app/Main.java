import exception.CarregarContasException;
import exception.SalvarContasException;
import service.ContaService;
import view.ContaGUI;
import view.MensagemGUI;

void main() {
    try {
        ContaService cs = new ContaService();
        cs.lerContas();

        ContaGUI gui = new ContaGUI(cs);
        gui.setVisible(true);
    } catch (CarregarContasException e) {
        MensagemGUI.exibirErro("Erro ao carregar contas:\n" + e.getMessage());
    } catch (SalvarContasException e) {
        MensagemGUI.exibirErro("Erro ao salvar contas:\n" + e.getMessage());
    } catch (Exception e) {
        MensagemGUI.exibirErro("Erro inesperado:\n" + e.getMessage());
    }
}

/*
void main() {
    ContaService cs = new ContaService();

    try {
        cs.lerContas();
        for (ContaCorrente c : cs.contas)
            System.out.println(c.getTitular());

        ContaCorrente conta = cs.lerConta("conta.txt");

        JOptionPane.showMessageDialog(
                null,
                "Conta carregada:" +
                "\nNº: " + conta.getNumero() +
                "\nTitular: " + conta.getTitular() +
                "\nSaldo: " + conta.getSaldo()
                );

        String saqueStr = JOptionPane.showInputDialog("Insira um valor para saque:");
        double saque = Double.parseDouble(saqueStr);

        try {
            conta.sacar(saque);
            JOptionPane.showMessageDialog(null, "Saque realizado com sucesso");
        } catch (OperacaoException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Erro ao sacar: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        cs.salvarConta("conta_atualizada.txt", conta);

    } catch (IOException e) {
        JOptionPane.showMessageDialog(
                null,
                "Erro ao acessar arquivo: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
*/
