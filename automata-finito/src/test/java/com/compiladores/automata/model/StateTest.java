package com.compiladores.automata.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StateTest {

    @Test
    void isFinalState_debeRetornarTrue_cuandoElEstadoEsAceptador() {
        State estado = new State("s0", "q0", true);
        assertTrue(estado.isFinalState(), "Se esperaba que el estado fuera final");
    }

    @Test
    void isFinalState_debeRetornarFalse_cuandoElEstadoNoEsAceptador() {
        State estado = new State("s1", "q1", false);
        assertFalse(estado.isFinalState(), "Se esperaba que el estado no fuera final");
    }
}
