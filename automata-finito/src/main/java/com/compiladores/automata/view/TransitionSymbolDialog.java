package com.compiladores.automata.view;

import com.compiladores.automata.controller.AutomatonController;
import com.compiladores.automata.model.State;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

final class TransitionSymbolDialog {
    private TransitionSymbolDialog() {
    }

    static Optional<List<Character>> choose(
            Component parent, AutomatonController controller, String sourceId, String targetId) {
        State source = controller.automaton().requireState(sourceId);
        State target = controller.automaton().requireState(targetId);
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        content.add(new JLabel("Transición: " + source.name() + " → " + target.name()));
        content.add(new JLabel("Selecciona uno o varios símbolos:"));

        List<SymbolOption> options = new ArrayList<>();
        for (Character symbol : controller.automaton().alphabet()) {
            JCheckBox checkBox = new JCheckBox("'" + symbol + "'");
            var existingTarget = controller.automaton().nextState(sourceId, symbol);
            if (existingTarget.isPresent()) {
                State existing = controller.automaton().requireState(existingTarget.orElseThrow());
                if (existing.id().equals(targetId)) {
                    checkBox.setSelected(true);
                    checkBox.setText("'" + symbol + "'  (ya asignado aquí)");
                } else {
                    checkBox.setEnabled(false);
                    checkBox.setText("'" + symbol + "'  (ocupado → " + existing.name() + ")");
                    checkBox.setToolTipText("En un AFD este símbolo ya tiene un destino desde "
                            + source.name() + ".");
                }
            }
            options.add(new SymbolOption(symbol, checkBox));
            content.add(checkBox);
        }

        JScrollPane scroll = new JScrollPane(content);
        AppTheme.scrollPane(scroll);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(340,
                Math.min(340, 80 + controller.automaton().alphabet().size() * 28)));
        int choice = JOptionPane.showConfirmDialog(parent, scroll, "Crear transición",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (choice != JOptionPane.OK_OPTION) {
            return Optional.empty();
        }
        List<Character> selected = options.stream()
                .filter(option -> option.checkBox().isEnabled() && option.checkBox().isSelected())
                .map(SymbolOption::symbol)
                .toList();
        return Optional.of(selected);
    }

    private record SymbolOption(char symbol, JCheckBox checkBox) {
    }
}
