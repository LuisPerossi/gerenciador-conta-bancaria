package view;


import service.ContaService;
import view.panels.*;

import javax.swing.*;
import java.awt.*;

public class ContaGUI extends JFrame {

    public ContaGUI(ContaService cs) {
        //Inicializando o ContaService

        //Configurações da janela
        this.setTitle("Gerenciador de Contas Bancárias");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setSize(700, 400);
        this.setResizable(false);

        //Criando painéis
        ListaContasPanel listaContasPanel = new ListaContasPanel();
        OperacoesContaPanel operacoesContaPanel = new OperacoesContaPanel(listaContasPanel, cs);
        OrdenarContasPanel ordenarContasPanel = new OrdenarContasPanel(listaContasPanel, cs);
        RelatoriosContasPanel relatoriosContasPanel = new RelatoriosContasPanel(cs);

        //Carregando as contas
        listaContasPanel.carregarContas(cs.getContas());

        //Adicionando painéis
        this.add(operacoesContaPanel, BorderLayout.NORTH);
        this.add(ordenarContasPanel, BorderLayout.WEST);
        this.add(listaContasPanel, BorderLayout.CENTER);
        this.add(relatoriosContasPanel, BorderLayout.SOUTH);
    }
}