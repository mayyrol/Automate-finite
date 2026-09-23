package com.compiladores.automata.view;

import com.compiladores.automata.controller.AutomatonController;
import com.compiladores.automata.model.DomainException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public final class AlphabetPanel extends JPanel {
    private final AutomatonController controller;
    private final JTextField symbolsField = new JTextField(16);

    public AlphabetPanel(AutomatonController controller, Consumer<String> statusListener,
                         Runnable modelChangeListener) {
        this.controller = controller;
        setLayout(new BorderLayout(5, 5));
        AppTheme.card(this, "1. Alfabeto");
        add(new JLabel("Símbolos de un carácter:"), BorderLayout.NORTH);

        JPanel editor = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        editor.add(symbolsField);
        JButton applyButton = new JButton("Aplicar");
        AppTheme.primaryButton(applyButton);
        editor.add(applyButton);
        add(editor, BorderLayout.CENTER);
        add(new JLabel("Ejemplos: ab  ·  a,b  ·  {a,b}"), BorderLayout.SOUTH);

        applyButton.addActionListener(event -> {
            try {
                controller.replaceAlphabet(symbolsField.getText());
                symbolsField.setText(controller.alphabetText());
                statusListener.accept("Alfabeto actualizado.");
                modelChangeListener.run();
            } catch (DomainException error) {
                statusListener.accept(error.getMessage());
            }
        });
        symbolsField.addActionListener(event -> applyButton.doClick());
    }
}
