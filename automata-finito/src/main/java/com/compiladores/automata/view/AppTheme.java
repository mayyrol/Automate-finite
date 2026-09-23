package com.compiladores.automata.view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.border.TitledBorder;

public final class AppTheme {
    public static final Color BACKGROUND = new Color(243, 244, 242);
    public static final Color SURFACE = new Color(255, 255, 253);
    public static final Color GRAPHITE = new Color(42, 44, 49);
    public static final Color MUTED = new Color(104, 108, 108);
    public static final Color BORDER = new Color(215, 218, 214);
    public static final Color VIOLET = new Color(126, 82, 170);
    public static final Color VIOLET_DARK = new Color(95, 58, 135);
    public static final Color GREEN = new Color(45, 148, 94);
    public static final Color RED = new Color(190, 62, 62);
    public static final Color ORANGE = new Color(230, 137, 58);

    private AppTheme() {
    }

    public static void install() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
            // El aspecto base de Swing continúa siendo funcional.
        }
        Font regular = new Font("SansSerif", Font.PLAIN, 13);
        Font medium = new Font("SansSerif", Font.BOLD, 13);
        UIManager.put("Label.font", regular);
        UIManager.put("Button.font", medium);
        UIManager.put("ToggleButton.font", medium);
        UIManager.put("TextField.font", regular);
        UIManager.put("TextArea.font", new Font("Monospaced", Font.PLAIN, 13));
        UIManager.put("CheckBox.font", regular);
        UIManager.put("ComboBox.font", regular);
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("OptionPane.background", SURFACE);
        UIManager.put("TextField.background", SURFACE);
        UIManager.put("TextArea.background", SURFACE);
        UIManager.put("Button.background", new Color(235, 236, 233));
        UIManager.put("Button.foreground", GRAPHITE);
        UIManager.put("Button.select", VIOLET);
        UIManager.put("ToggleButton.select", VIOLET);
        UIManager.put("ToggleButton.focus", VIOLET_DARK);
        UIManager.put("ToggleButton.highlight", new Color(73, 75, 80));
        UIManager.put("List.selectionBackground", VIOLET);
        UIManager.put("List.selectionForeground", Color.WHITE);
        UIManager.put("ComboBox.selectionBackground", VIOLET);
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);
        UIManager.put("ComboBox.disabledForeground", MUTED);
        UIManager.put("ComboBox.disabledBackground", new Color(242, 243, 240));
        UIManager.put("ComboBox.buttonBackground", new Color(238, 239, 236));
        UIManager.put("ComboBox.buttonShadow", BORDER);
        UIManager.put("ComboBox.buttonDarkShadow", GRAPHITE);
        UIManager.put("ComboBox.buttonHighlight", SURFACE);
        UIManager.put("ComboBox.border", new BorderUIResource.LineBorderUIResource(BORDER));
        UIManager.put("CheckBox.icon", new ModernCheckBoxIcon());
        UIManager.put("CheckBox.focus", VIOLET_DARK);
        UIManager.put("CheckBox.background", SURFACE);
        UIManager.put("CheckBox.disabledText", MUTED);
        UIManager.put("Label.disabledForeground", MUTED);
        UIManager.put("Button.disabledText", MUTED);
        UIManager.put("TextField.inactiveForeground", MUTED);
        UIManager.put("TextField.inactiveBackground", new Color(242, 243, 240));
        UIManager.put("List.focusCellHighlightBorder",
                new BorderUIResource.LineBorderUIResource(VIOLET_DARK));
        UIManager.put("TextField.border", new BorderUIResource.LineBorderUIResource(BORDER));
        UIManager.put("Menu.selectionBackground", VIOLET);
        UIManager.put("Menu.selectionForeground", Color.WHITE);
        UIManager.put("MenuItem.selectionBackground", VIOLET);
        UIManager.put("MenuItem.selectionForeground", Color.WHITE);
        UIManager.put("TextField.selectionBackground", new Color(215, 194, 230));
        UIManager.put("TextArea.selectionBackground", new Color(215, 194, 230));
        UIManager.put("TextPane.selectionBackground", new Color(215, 194, 230));
        UIManager.put("ScrollBar.width", 12);
        UIManager.put("ScrollBar.thumb", VIOLET);
        UIManager.put("ScrollBar.thumbHighlight", new Color(152, 112, 190));
        UIManager.put("ScrollBar.thumbShadow", VIOLET_DARK);
        UIManager.put("ScrollBar.thumbDarkShadow", GRAPHITE);
        UIManager.put("ScrollBar.track", new Color(232, 234, 231));
        UIManager.put("ScrollBar.trackHighlight", new Color(220, 222, 219));
    }

    public static Border cardBorder(String title) {
        TitledBorder titled = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER, 1, true), title);
        titled.setTitleColor(GRAPHITE);
        titled.setTitleFont(new Font("SansSerif", Font.BOLD, 13));
        return BorderFactory.createCompoundBorder(titled,
                BorderFactory.createEmptyBorder(8, 10, 10, 10));
    }

    public static void card(JComponent component, String title) {
        component.setOpaque(true);
        component.setBackground(SURFACE);
        component.setBorder(cardBorder(title));
    }

    public static void primaryButton(JButton button) {
        button.setBackground(VIOLET);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VIOLET_DARK, 1, true),
                BorderFactory.createEmptyBorder(7, 13, 7, 13)));
    }

    public static void secondaryButton(JButton button) {
        button.setBackground(new Color(238, 239, 236));
        button.setForeground(GRAPHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(7, 12, 7, 12)));
    }

    public static void scrollPane(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(14);
    }
}
