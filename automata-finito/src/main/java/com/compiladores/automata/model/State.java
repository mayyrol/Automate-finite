package com.compiladores.automata.model;

import java.util.Objects;

public record State(String id, String name, boolean accepting) {
    public State {
        if (id == null || id.isBlank()) {
            throw new DomainException("El estado debe tener un identificador.");
        }
        if (name == null || name.isBlank()) {
            throw new DomainException("El nombre del estado no puede estar vacío.");
        }
        name = name.trim();
    }

    public State renamedTo(String newName) {
        return new State(id, Objects.requireNonNull(newName), accepting);
    }

    public State withAccepting(boolean value) {
        return new State(id, name, value);
    }

    /**
     * Indica si este estado es un estado final (de aceptación) del autómata.
     *
     * @return {@code true} si el estado es final; {@code false} en caso contrario.
     */
    public boolean isFinalState() {
        return accepting;
    }
}
