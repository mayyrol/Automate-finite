package com.compiladores.automata.view;

import java.awt.Color;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;

public final class ModernComboBoxUI extends BasicComboBoxUI {
    public static ComponentUI createUI(JComponent component) {
        return new ModernComboBoxUI();
    }

    @Override
    protected JButton createArrowButton() {
        JButton button = new JButton("▾");
        button.setForeground(AppTheme.GRAPHITE);
        button.setBackground(new Color(238, 239, 236));
        button.setFocusPainted(false);
        button.setMargin(new Insets(0, 7, 0, 7));
        button.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, AppTheme.BORDER));
        return button;
    }
}
