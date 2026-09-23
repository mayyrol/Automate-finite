package com.compiladores.automata.view;

import java.awt.Component;
import java.awt.Color;
import java.awt.Dimension;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.plaf.basic.BasicToggleButtonUI;

@SuppressWarnings("serial")
public final class ToolPalette extends JPanel {
    private final Map<EditorTool, JToggleButton> buttons = new EnumMap<>(EditorTool.class);

    public ToolPalette(Consumer<EditorTool> toolListener) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(AppTheme.GRAPHITE);
        setBorder(BorderFactory.createEmptyBorder(18, 12, 18, 12));
        setPreferredSize(new Dimension(172, 500));
        JLabel title = new JLabel("Herramientas");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(java.awt.Font.BOLD, 16f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);
        add(Box.createVerticalStrut(10));

        ButtonGroup group = new ButtonGroup();
        for (EditorTool tool : EditorTool.values()) {
            JToggleButton button = new JToggleButton(labelFor(tool));
            button.setMaximumSize(new Dimension(148, 42));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setHorizontalAlignment(JToggleButton.LEFT);
            button.setUI(new BasicToggleButtonUI());
            button.setForeground(Color.WHITE);
            button.setBackground(AppTheme.GRAPHITE);
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
            button.addItemListener(event -> button.setBackground(
                    button.isSelected() ? AppTheme.VIOLET : AppTheme.GRAPHITE));
            button.addActionListener(event -> toolListener.accept(tool));
            group.add(button);
            buttons.put(tool, button);
            add(button);
            add(Box.createVerticalStrut(7));
        }
        buttons.get(EditorTool.SELECT).setSelected(true);
        add(Box.createVerticalGlue());
        JLabel hint = new JLabel("Supr: eliminar");
        hint.setForeground(new Color(195, 198, 195));
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(hint);
    }

    private String labelFor(EditorTool tool) {
        return switch (tool) {
            case SELECT -> "↖   Seleccionar";
            case STATE -> "●   Estado";
            case TRANSITION -> "→   Transición";
            case DELETE -> "✕   Eliminar";
        };
    }
}
