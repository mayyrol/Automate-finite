package com.compiladores.automata.model;

public record TransitionKey(String sourceId, char symbol) {
    public TransitionKey {
        if (sourceId == null || sourceId.isBlank()) {
            throw new DomainException("La transición debe tener un estado de origen.");
        }
    }
}
