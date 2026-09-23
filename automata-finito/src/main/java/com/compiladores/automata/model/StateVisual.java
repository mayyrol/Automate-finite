package com.compiladores.automata.model;

public final class StateVisual {
    private double x;
    private double y;
    private AllowedColor color;

    public StateVisual(double x, double y, AllowedColor color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public AllowedColor color() {
        return color;
    }

    public void moveTo(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setColor(AllowedColor color) {
        this.color = color;
    }
}
