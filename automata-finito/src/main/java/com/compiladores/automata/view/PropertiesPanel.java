package com.compiladores.automata.view;

import com.compiladores.automata.controller.AutomatonController;
import com.compiladores.automata.model.AllowedColor;
import com.compiladores.automata.model.DomainException;
import com.compiladores.automata.model.State;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public final class PropertiesPanel extends JPanel {
    private final AutomatonController controller;
    private final Consumer<String> statusListener;
    private final Runnable modelChangeListener;
    private final JTextField nameField = new JTextField(13);
    private final JCheckBox initialCheck = new JCheckBox("Estado inicial");
    private final JCheckBox acceptingCheck = new JCheckBox("Estado final");
    private final JComboBox<AllowedColor> colorCombo = new JComboBox<>(AllowedColor.values());
    private final JButton applyButton = new JButton("Aplicar cambios");
    private final JLabel hint = new JLabel("Selecciona un estado.");
    private String selectedStateId;

    public PropertiesPanel(AutomatonController controller, Consumer<String> statusListener,
                           Runnable modelChangeListener) {
        this.controller = controller;
        this.statusListener = statusListener;
        this.modelChangeListener = modelChangeListener;
        setLayout(new GridBagLayout());
        AppTheme.card(this, "3. Estado seleccionado");
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.insets = new Insets(3, 4, 3, 4);
        add(hint, constraints);

        constraints.gridy++;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        add(new JLabel("Nombre:"), constraints);
        constraints.gridx = 1;
        constraints.weightx = 1;
        add(nameField, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        add(initialCheck, constraints);
        constraints.gridy++;
        add(acceptingCheck, constraints);

        constraints.gridy++;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        add(new JLabel("Color:"), constraints);
        constraints.gridx = 1;
        constraints.weightx = 1;
        add(colorCombo, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        add(applyButton, constraints);
        applyButton.addActionListener(event -> applyChanges());
        AppTheme.primaryButton(applyButton);
        colorCombo.setUI(new ModernComboBoxUI());
        colorCombo.setBorder(javax.swing.BorderFactory.createLineBorder(AppTheme.BORDER));
        setSelection(null);
    }

    public void setSelection(String stateId) {
        selectedStateId = stateId;
        boolean enabled = stateId != null && controller.automaton().findState(stateId).isPresent();
        nameField.setEnabled(enabled);
        initialCheck.setEnabled(enabled);
        acceptingCheck.setEnabled(enabled);
        colorCombo.setEnabled(enabled);
        applyButton.setEnabled(enabled);
        if (!enabled) {
            hint.setText("Selecciona un estado.");
            nameField.setText("");
            initialCheck.setSelected(false);
            acceptingCheck.setSelected(false);
            return;
        }
        State state = controller.automaton().requireState(stateId);
        hint.setText("Editando " + state.name());
        nameField.setText(state.name());
        initialCheck.setSelected(controller.automaton().initialStateId().filter(stateId::equals).isPresent());
        acceptingCheck.setSelected(state.accepting());
        colorCombo.setSelectedItem(controller.visual(stateId).color());
    }

    public void refreshSelection() {
        setSelection(selectedStateId);
    }

    private void applyChanges() {
        if (selectedStateId == null) {
            return;
        }
        try {
            controller.renameState(selectedStateId, nameField.getText());
            controller.setInitial(selectedStateId, initialCheck.isSelected());
            controller.setAccepting(selectedStateId, acceptingCheck.isSelected());
            controller.setColor(selectedStateId, (AllowedColor) colorCombo.getSelectedItem());
            statusListener.accept("Propiedades del estado actualizadas.");
            refreshSelection();
            modelChangeListener.run();
        } catch (DomainException error) {
            statusListener.accept(error.getMessage());
        }
    }
}
