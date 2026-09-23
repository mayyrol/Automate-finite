package com.compiladores.automata.model;

/** Error causado por una operación que viola las reglas de un AFD. */
@SuppressWarnings("serial")
public final class DomainException extends IllegalArgumentException {
    public DomainException(String message) {
        super(message);
    }
}
