package com.compiladores.automata.controller;

import com.compiladores.automata.model.AllowedColor;
import com.compiladores.automata.model.Automaton;
import com.compiladores.automata.model.AutomatonSimulator;
import com.compiladores.automata.model.DomainException;
import com.compiladores.automata.model.SimulationResult;
import com.compiladores.automata.model.State;
import com.compiladores.automata.model.StateVisual;
import com.compiladores.automata.model.Transition;
import com.compiladores.automata.model.TransitionGroup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class AutomatonController {
    private final Automaton automaton = new Automaton();
    private final AutomatonSimulator simulator = new AutomatonSimulator();
    private final Map<String, StateVisual> visuals = new LinkedHashMap<>();

    public Automaton automaton() {
        return automaton;
    }

    public State createState(double x, double y) {
        State state = automaton.addState();
        visuals.put(state.id(), new StateVisual(x, y, AllowedColor.WHITE));
        return state;
    }

    public void deleteState(String stateId) {
        automaton.removeState(stateId);
        visuals.remove(stateId);
    }

    public void renameState(String stateId, String name) {
        automaton.renameState(stateId, name);
    }

    public void setInitial(String stateId, boolean initial) {
        if (initial) {
            automaton.setInitialState(stateId);
        } else if (automaton.initialStateId().filter(stateId::equals).isPresent()) {
            automaton.clearInitialState();
        }
    }

    public void setAccepting(String stateId, boolean accepting) {
        automaton.setAccepting(stateId, accepting);
    }

    public void setColor(String stateId, AllowedColor color) {
        requireVisual(stateId).setColor(color);
    }

    public void moveState(String stateId, double x, double y) {
        requireVisual(stateId).moveTo(x, y);
    }

    public StateVisual visual(String stateId) {
        return requireVisual(stateId);
    }

    public void replaceAlphabet(String rawSymbols) {
        automaton.replaceAlphabet(SymbolParser.parse(rawSymbols));
    }

    public void addTransitions(String sourceId, String targetId, Collection<Character> symbols) {
        automaton.addTransitions(sourceId, targetId, symbols);
    }

    public void deleteTransitionGroup(String sourceId, String targetId) {
        automaton.removeTransitionsBetween(sourceId, targetId);
    }

    public List<TransitionGroup> transitionGroups() {
        Map<String, List<Character>> groupedSymbols = new LinkedHashMap<>();
        Map<String, String[]> endpoints = new LinkedHashMap<>();
        for (Transition transition : automaton.transitions()) {
            String key = transition.sourceId() + "\u0000" + transition.targetId();
            groupedSymbols.computeIfAbsent(key, ignored -> new ArrayList<>()).add(transition.symbol());
            endpoints.putIfAbsent(key, new String[]{transition.sourceId(), transition.targetId()});
        }
        List<TransitionGroup> groups = new ArrayList<>();
        groupedSymbols.forEach((key, symbols) -> {
            String[] pair = endpoints.get(key);
            groups.add(new TransitionGroup(pair[0], pair[1], symbols));
        });
        return List.copyOf(groups);
    }

    public SimulationResult simulate(String input) {
        return simulator.simulate(automaton, input);
    }

    public String alphabetText() {
        return SymbolParser.format(automaton.alphabet());
    }

    private StateVisual requireVisual(String stateId) {
        StateVisual visual = visuals.get(stateId);
        if (visual == null) {
            throw new DomainException("El estado no tiene representación visual.");
        }
        return visual;
    }
}
