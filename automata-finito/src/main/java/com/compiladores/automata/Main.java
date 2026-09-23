package com.compiladores.automata;

import com.compiladores.automata.view.MainFrame;
import com.compiladores.automata.view.AppTheme;
import javax.swing.SwingUtilities;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppTheme.install();
            new MainFrame().setVisible(true);
        });
    }
}
