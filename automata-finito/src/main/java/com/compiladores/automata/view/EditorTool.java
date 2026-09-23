package com.compiladores.automata.view;

public enum EditorTool {
    SELECT("Seleccionar"),
    STATE("Estado"),
    TRANSITION("Transición"),
    DELETE("Eliminar");

    private final String label;

    EditorTool(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
