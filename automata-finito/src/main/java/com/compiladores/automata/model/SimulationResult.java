package com.compiladores.automata.model;

import java.util.List;
import java.util.Optional;

public record SimulationResult(
        boolean accepted,
        String initialStateId,
        String finalStateId,
        List<SimulationStep> steps,
        String explanation,
        Integer failurePosition,
        Character failureSymbol) {

    public SimulationResult {
        steps = List.copyOf(steps);
    }

    public Optional<Integer> failurePositionValue() {
        return Optional.ofNullable(failurePosition);
    }

    public Optional<Character> failureSymbolValue() {
        return Optional.ofNullable(failureSymbol);
    }
}
