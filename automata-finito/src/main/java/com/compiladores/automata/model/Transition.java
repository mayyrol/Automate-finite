package com.compiladores.automata.model;

public record Transition(String sourceId, String targetId, char symbol) {
    public Transition {
        if (sourceId == null || sourceId.isBlank() || targetId == null || targetId.isBlank()) {
            throw new DomainException("La transición debe tener origen y destino.");
        }
    }
}
