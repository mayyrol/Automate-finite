package com.compiladores.automata.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

final class ModernScrollBarUI extends BasicScrollBarUI {
    @Override
    protected void configureScrollBarColors() {
        thumbColor = AppTheme.VIOLET;
        thumbHighlightColor = new Color(152, 112, 190);
        thumbDarkShadowColor = AppTheme.VIOLET_DARK;
        thumbLightShadowColor = AppTheme.VIOLET;
        trackColor = new Color(232, 234, 231);
        trackHighlightColor = new Color(220, 222, 219);
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return invisibleButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return invisibleButton();
    }

    @Override
    protected void paintThumb(Graphics graphics, JComponent component, Rectangle bounds) {
        if (bounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(isDragging ? AppTheme.VIOLET_DARK : AppTheme.VIOLET);
        g.fillRoundRect(bounds.x + 2, bounds.y + 2,
                Math.max(6, bounds.width - 4), Math.max(6, bounds.height - 4), 10, 10);
        g.dispose();
    }

    private JButton invisibleButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }
}
