package com.compiladores.automata.model;

import java.util.ArrayList;
import java.util.List;

public final class AutomatonSimulator {
    private final AutomatonValidator validator;

    public AutomatonSimulator() {
        this(new AutomatonValidator());
    }

    public AutomatonSimulator(AutomatonValidator validator) {
        this.validator = validator;
    }

    public SimulationResult simulate(Automaton automaton, String input) {
        if (input == null) {
            throw new DomainException("La cadena no puede ser nula.");
        }
        List<ValidationIssue> issues = new ArrayList<>(validator.validateForSimulation(automaton));
        for (int index = 0; index < input.length(); index++) {
            char symbol = input.charAt(index);
            if (!automaton.alphabet().contains(symbol)) {
                issues.add(new ValidationIssue("SYMBOL_OUTSIDE_ALPHABET",
                        "El símbolo '" + symbol + "' en la posición " + (index + 1)
                                + " no pertenece al alfabeto."));
                break;
            }
        }
        if (!issues.isEmpty()) {
            throw new AutomatonNotReadyException(issues);
        }

        String initialId = automaton.initialStateId().orElseThrow();
        String currentId = initialId;
        List<SimulationStep> steps = new ArrayList<>();

        for (int index = 0; index < input.length(); index++) {
            char symbol = input.charAt(index);
            String sourceId = currentId;
            var target = automaton.nextState(sourceId, symbol);
            if (target.isEmpty()) {
                String stateName = automaton.requireState(sourceId).name();
                return new SimulationResult(false, initialId, sourceId, steps,
                        "No existe una transición desde " + stateName + " con '" + symbol + "'.",
                        index, symbol);
            }
            currentId = target.get();
            steps.add(new SimulationStep(index, sourceId, symbol, currentId));
        }

        State finalState = automaton.requireState(currentId);
        boolean accepted = finalState.accepting();
        String explanation = accepted
                ? "La cadena terminó en el estado final " + finalState.name() + "."
                : "La cadena terminó en " + finalState.name() + ", que no es un estado final.";
        return new SimulationResult(accepted, initialId, currentId, steps, explanation, null, null);
    }
}
