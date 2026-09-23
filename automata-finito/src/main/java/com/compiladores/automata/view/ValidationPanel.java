package com.compiladores.automata.view;

import com.compiladores.automata.controller.AutomatonController;
import com.compiladores.automata.model.AutomatonNotReadyException;
import com.compiladores.automata.model.DomainException;
import com.compiladores.automata.model.SimulationResult;
import com.compiladores.automata.model.SimulationStep;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

@SuppressWarnings("serial")
public final class ValidationPanel extends JPanel {
    private static final Color SUCCESS = AppTheme.GREEN;
    private static final Color FAILURE = AppTheme.RED;

    private final AutomatonController controller;
    private final AutomatonCanvas canvas;
    private final Consumer<String> statusListener;
    private final JTextField inputField = new JTextField(16);
    private final JLabel currentSymbol = new JLabel("Símbolo actual: —");
    private final JLabel resultLabel = new JLabel("Resultado pendiente");
    private final JTextArea routeArea = new JTextArea(7, 23);
    private final JButton validateButton = new JButton("Validar cadena");
    private final JButton stopButton = new JButton("Detener");
    private Timer timer;
    private SimulationResult result;
    private int nextStepIndex;

    public ValidationPanel(AutomatonController controller, AutomatonCanvas canvas,
                           Consumer<String> statusListener) {
        this.controller = controller;
        this.canvas = canvas;
        this.statusListener = statusListener;
        setLayout(new BorderLayout(5, 5));
        AppTheme.card(this, "4. Validar cadena");

        JPanel top = new JPanel(new BorderLayout(4, 4));
        top.add(new JLabel("Cadena (vacío = ε):"), BorderLayout.NORTH);
        top.add(inputField, BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        buttons.add(validateButton);
        buttons.add(stopButton);
        AppTheme.primaryButton(validateButton);
        AppTheme.secondaryButton(stopButton);
        top.add(buttons, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        routeArea.setEditable(false);
        routeArea.setLineWrap(true);
        routeArea.setWrapStyleWord(true);
        JScrollPane routeScroll = new JScrollPane(routeArea);
        AppTheme.scrollPane(routeScroll);
        add(routeScroll, BorderLayout.CENTER);

        JPanel summary = new JPanel(new BorderLayout());
        summary.add(currentSymbol, BorderLayout.NORTH);
        summary.add(resultLabel, BorderLayout.SOUTH);
        add(summary, BorderLayout.SOUTH);

        stopButton.setEnabled(false);
        validateButton.addActionListener(event -> startSimulation());
        stopButton.addActionListener(event -> stopSimulation("Simulación detenida."));
        inputField.addActionListener(event -> validateButton.doClick());
    }

    private void startSimulation() {
        stopTimer();
        canvas.clearSimulationHighlight();
        routeArea.setText("");
        currentSymbol.setText("Símbolo actual: —");
        resultLabel.setText("Validando...");
        resultLabel.setForeground(Color.DARK_GRAY);
        try {
            result = controller.simulate(inputField.getText());
        } catch (AutomatonNotReadyException error) {
            StringBuilder message = new StringBuilder();
            error.issues().forEach(issue -> message.append("• ").append(issue.message()).append('\n'));
            routeArea.setText(message.toString());
            resultLabel.setText("No se puede iniciar la simulación");
        resultLabel.setForeground(FAILURE);
            statusListener.accept(error.getMessage());
            return;
        } catch (DomainException error) {
            routeArea.setText(error.getMessage());
            resultLabel.setText("Entrada inválida");
            resultLabel.setForeground(FAILURE);
            statusListener.accept(error.getMessage());
            return;
        }

        nextStepIndex = 0;
        String initialName = controller.automaton().requireState(result.initialStateId()).name();
        routeArea.setText("Inicio: " + initialName);
        canvas.highlightState(result.initialStateId());
        stopButton.setEnabled(true);
        validateButton.setEnabled(false);
        statusListener.accept("Simulación en curso.");

        if (result.steps().isEmpty()) {
            finishSimulation();
            return;
        }
        timer = new Timer(700, event -> advanceOneStep());
        timer.setInitialDelay(500);
        timer.start();
    }

    private void advanceOneStep() {
        if (nextStepIndex >= result.steps().size()) {
            finishSimulation();
            return;
        }
        SimulationStep step = result.steps().get(nextStepIndex++);
        String targetName = controller.automaton().requireState(step.targetId()).name();
        currentSymbol.setText("Símbolo actual: " + step.symbol()
                + " (posición " + (step.position() + 1) + ")");
        routeArea.append("\n--" + step.symbol() + "--> " + targetName);
        routeArea.setCaretPosition(routeArea.getDocument().getLength());
        canvas.highlightStep(step.sourceId(), step.targetId(), step.targetId());
        if (nextStepIndex >= result.steps().size()) {
            timer.setInitialDelay(0);
            timer.setDelay(700);
        }
    }

    private void finishSimulation() {
        stopTimer();
        stopButton.setEnabled(false);
        validateButton.setEnabled(true);
        canvas.highlightState(result.finalStateId());
        if (result.failureSymbolValue().isPresent()) {
            currentSymbol.setText("Sin transición para: " + result.failureSymbolValue().orElseThrow()
                    + " (posición " + (result.failurePositionValue().orElseThrow() + 1) + ")");
        } else {
            currentSymbol.setText("Cadena consumida por completo");
        }
        resultLabel.setText(result.accepted() ? "✓ Cadena aceptada" : "✗ Cadena rechazada");
        resultLabel.setForeground(result.accepted() ? SUCCESS : FAILURE);
        routeArea.append("\n\n" + result.explanation());
        statusListener.accept(result.accepted() ? "Cadena aceptada." : "Cadena rechazada.");
    }

    private void stopSimulation(String message) {
        stopTimer();
        stopButton.setEnabled(false);
        validateButton.setEnabled(true);
        canvas.clearSimulationHighlight();
        currentSymbol.setText("Símbolo actual: —");
        resultLabel.setText("Simulación detenida");
        resultLabel.setForeground(Color.DARK_GRAY);
        statusListener.accept(message);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }
}
