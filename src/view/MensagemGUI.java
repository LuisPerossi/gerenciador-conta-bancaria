package view;

import javax.swing.*;
import java.awt.*;

public final class MensagemGUI {
    private static Component pai = null;

    public static void setPai(Component pai) { MensagemGUI.pai = pai; }

    public static void exibirMensagem(String mensagem) {
        JOptionPane.showMessageDialog(pai, mensagem, "Mensagem", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void exibirAlerta(String mensagem) {
        JOptionPane.showMessageDialog(pai, mensagem, "Alerta", JOptionPane.WARNING_MESSAGE);
    }

    public static void exibirErro(String mensagem) {
        JOptionPane.showMessageDialog(pai, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void exibirObjeto(Object objeto, String titulo) {
        JOptionPane.showMessageDialog(pai, objeto, titulo, JOptionPane.PLAIN_MESSAGE);
    }

    public static String receberInput(String mensagem) {
        return JOptionPane.showInputDialog(pai, mensagem);
    }

    public static int receberEscolha(String[] opcoes, String titulo, String mensagem) {
        return JOptionPane.showOptionDialog(
                pai,
                mensagem,
                titulo,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );
    }

    public static int confirmar(String mensagem) {
        return JOptionPane.showConfirmDialog(
                pai,
                mensagem,
                "Confirmar",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
    }
}
