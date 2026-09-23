package com.compiladores.automata.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.AbstractButton;
import javax.swing.Icon;

final class ModernCheckBoxIcon implements Icon {
    private static final int SIZE = 17;

    @Override
    public void paintIcon(Component component, Graphics graphics, int x, int y) {
        AbstractButton button = (AbstractButton) component;
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        boolean selected = button.getModel().isSelected();
        boolean focused = button.hasFocus();
        g.setColor(selected ? AppTheme.VIOLET : AppTheme.SURFACE);
        g.fillRoundRect(x + 1, y + 1, SIZE - 2, SIZE - 2, 5, 5);
        g.setColor(focused || selected ? AppTheme.VIOLET_DARK : AppTheme.MUTED);
        g.setStroke(new BasicStroke(focused ? 2f : 1.4f));
        g.drawRoundRect(x + 1, y + 1, SIZE - 3, SIZE - 3, 5, 5);
        if (selected) {
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.drawLine(x + 4, y + 9, x + 7, y + 12);
            g.drawLine(x + 7, y + 12, x + 13, y + 5);
        }
        g.dispose();
    }

    @Override
    public int getIconWidth() {
        return SIZE;
    }

    @Override
    public int getIconHeight() {
        return SIZE;
    }
}
