package com.compiladores.automata.model;

import java.util.List;

public record TransitionGroup(String sourceId, String targetId, List<Character> symbols) {
    public TransitionGroup {
        symbols = List.copyOf(symbols);
    }

    public String label() {
        StringBuilder result = new StringBuilder();
        for (int index = 0; index < symbols.size(); index++) {
            if (index > 0) {
                result.append(", ");
            }
            result.append(symbols.get(index));
        }
        return result.toString();
    }
}
