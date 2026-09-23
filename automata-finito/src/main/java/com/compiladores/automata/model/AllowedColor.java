package com.compiladores.automata.model;

import java.awt.Color;

public enum AllowedColor {
    WHITE("Blanco", new Color(250, 250, 248)),
    GRAPHITE("Grafito", new Color(65, 68, 74)),
    GRAY("Gris", new Color(174, 178, 181)),
    GREEN("Verde", new Color(84, 166, 116)),
    VIOLET("Violeta", new Color(142, 104, 185)),
    ORANGE("Naranja", new Color(235, 145, 66)),
    RED("Rojo", new Color(214, 87, 87)),
    YELLOW("Amarillo", new Color(238, 205, 86));

    private final String displayName;
    private final Color awtColor;

    AllowedColor(String displayName, Color awtColor) {
        this.displayName = displayName;
        this.awtColor = awtColor;
    }

    public Color awtColor() {
        return awtColor;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
