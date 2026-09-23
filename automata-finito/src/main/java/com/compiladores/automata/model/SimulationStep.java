package com.compiladores.automata.model;

public record SimulationStep(int position, String sourceId, char symbol, String targetId) {
}
