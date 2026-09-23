package com.compiladores.automata.model;

import java.util.ArrayList;
import java.util.List;

public final class AutomatonValidator {
    public List<ValidationIssue> validateForSimulation(Automaton automaton) {
        List<ValidationIssue> issues = new ArrayList<>();
        if (automaton.states().isEmpty()) {
            issues.add(new ValidationIssue("NO_STATES", "El autómata no tiene estados."));
        }
        if (automaton.initialStateId().isEmpty()) {
            issues.add(new ValidationIssue("NO_INITIAL_STATE", "Selecciona un estado inicial."));
        }
        return List.copyOf(issues);
    }
}
