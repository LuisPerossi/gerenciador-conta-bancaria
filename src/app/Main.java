import service.ContaService;
import view.ContaGUI;
import view.MensagemGUI;

import java.sql.SQLException;

void main() {
    try {
        ContaService cs = new ContaService();
        cs.carregarContas();

        ContaGUI gui = new ContaGUI(cs);
        gui.setVisible(true);

        MensagemGUI.setPai(gui);
    } catch (SQLException e) {
        MensagemGUI.exibirErro("Erro ao carregar contas:\n" + e.getMessage());
    } catch (Exception e) {
        MensagemGUI.exibirErro("Erro inesperado:\n" + e.getMessage());
    }
}