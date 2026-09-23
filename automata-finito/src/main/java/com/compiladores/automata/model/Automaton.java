package com.compiladores.automata.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class Automaton {
    private final Map<String, State> states = new LinkedHashMap<>();
    private final Set<Character> alphabet = new LinkedHashSet<>();
    private final Map<TransitionKey, String> transitionTargets = new LinkedHashMap<>();
    private String initialStateId;
    private int nextStateId;
    private int nextGeneratedName;

    public State addState() {
        String name;
        do {
            name = "q" + nextGeneratedName++;
        } while (hasStateNamed(name, null));
        return addStateWithName(name);
    }

    public State addStateWithName(String name) {
        String normalized = normalizeName(name);
        if (hasStateNamed(normalized, null)) {
            throw new DomainException("Ya existe un estado llamado " + normalized + ".");
        }
        String id = "s" + nextStateId++;
        State state = new State(id, normalized, false);
        states.put(id, state);
        return state;
    }

    public void renameState(String stateId, String newName) {
        State state = requireState(stateId);
        String normalized = normalizeName(newName);
        if (hasStateNamed(normalized, stateId)) {
            throw new DomainException("Ya existe un estado llamado " + normalized + ".");
        }
        states.put(stateId, state.renamedTo(normalized));
    }

    public void setInitialState(String stateId) {
        requireState(stateId);
        initialStateId = stateId;
    }

    public void clearInitialState() {
        initialStateId = null;
    }

    public void setAccepting(String stateId, boolean accepting) {
        State state = requireState(stateId);
        states.put(stateId, state.withAccepting(accepting));
    }

    public void removeState(String stateId) {
        requireState(stateId);
        states.remove(stateId);
        transitionTargets.entrySet().removeIf(entry ->
                entry.getKey().sourceId().equals(stateId) || entry.getValue().equals(stateId));
        if (stateId.equals(initialStateId)) {
            initialStateId = null;
        }
        if (states.isEmpty()) {
            nextStateId = 0;
            nextGeneratedName = 0;
        }
    }

    public void addSymbol(char symbol) {
        validateVisibleSymbol(symbol);
        if (!alphabet.add(symbol)) {
            throw new DomainException("El símbolo '" + symbol + "' ya pertenece al alfabeto.");
        }
    }

    public void replaceAlphabet(Collection<Character> symbols) {
        if (symbols == null) {
            throw new DomainException("El alfabeto no puede ser nulo.");
        }
        LinkedHashSet<Character> replacement = new LinkedHashSet<>();
        for (Character symbol : symbols) {
            if (symbol == null) {
                throw new DomainException("El alfabeto no puede contener valores nulos.");
            }
            validateVisibleSymbol(symbol);
            if (!replacement.add(symbol)) {
                throw new DomainException("El símbolo '" + symbol + "' está repetido.");
            }
        }
        for (TransitionKey key : transitionTargets.keySet()) {
            if (!replacement.contains(key.symbol())) {
                throw new DomainException("No se puede eliminar '" + key.symbol()
                        + "' porque está utilizado por una transición.");
            }
        }
        alphabet.clear();
        alphabet.addAll(replacement);
    }

    public void removeSymbol(char symbol) {
        boolean inUse = transitionTargets.keySet().stream().anyMatch(key -> key.symbol() == symbol);
        if (inUse) {
            throw new DomainException("No se puede eliminar '" + symbol
                    + "' porque está utilizado por una transición.");
        }
        alphabet.remove(symbol);
    }

    public void addTransition(String sourceId, String targetId, char symbol) {
        addTransitions(sourceId, targetId, List.of(symbol));
    }

    public void addTransitions(String sourceId, String targetId, Collection<Character> symbols) {
        requireState(sourceId);
        requireState(targetId);
        if (symbols == null || symbols.isEmpty()) {
            throw new DomainException("Selecciona al menos un símbolo para la transición.");
        }
        LinkedHashSet<Character> uniqueSymbols = new LinkedHashSet<>();
        for (Character symbol : symbols) {
            if (symbol == null || !alphabet.contains(symbol)) {
                throw new DomainException("El símbolo '" + symbol + "' no pertenece al alfabeto.");
            }
            uniqueSymbols.add(symbol);
            TransitionKey key = new TransitionKey(sourceId, symbol);
            String existingTarget = transitionTargets.get(key);
            if (existingTarget != null && !existingTarget.equals(targetId)) {
                throw new DomainException("El estado " + requireState(sourceId).name()
                        + " ya tiene una transición para '" + symbol + "'.");
            }
        }
        for (Character symbol : uniqueSymbols) {
            transitionTargets.put(new TransitionKey(sourceId, symbol), targetId);
        }
    }

    public void removeTransition(String sourceId, char symbol) {
        transitionTargets.remove(new TransitionKey(sourceId, symbol));
    }

    public void removeTransitionsBetween(String sourceId, String targetId) {
        transitionTargets.entrySet().removeIf(entry ->
                entry.getKey().sourceId().equals(sourceId) && entry.getValue().equals(targetId));
    }

    public Optional<String> nextState(String sourceId, char symbol) {
        requireState(sourceId);
        return Optional.ofNullable(transitionTargets.get(new TransitionKey(sourceId, symbol)));
    }

    public State requireState(String stateId) {
        State state = states.get(stateId);
        if (state == null) {
            throw new DomainException("El estado indicado no existe.");
        }
        return state;
    }

    public Collection<State> states() {
        return Collections.unmodifiableCollection(states.values());
    }

    public Set<Character> alphabet() {
        return Collections.unmodifiableSet(alphabet);
    }

    public List<Transition> transitions() {
        List<Transition> result = new ArrayList<>();
        transitionTargets.forEach((key, target) ->
                result.add(new Transition(key.sourceId(), target, key.symbol())));
        return List.copyOf(result);
    }

    public Optional<String> initialStateId() {
        return Optional.ofNullable(initialStateId);
    }

    public Optional<State> initialState() {
        return initialStateId().map(states::get);
    }

    public Optional<State> findState(String stateId) {
        return Optional.ofNullable(states.get(stateId));
    }

    private boolean hasStateNamed(String name, String ignoredId) {
        return states.values().stream().anyMatch(state ->
                !state.id().equals(ignoredId) && state.name().equals(name));
    }

    private static String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainException("El nombre del estado no puede estar vacío.");
        }
        return name.trim();
    }

    private static void validateVisibleSymbol(char symbol) {
        if (Character.isWhitespace(symbol) || Character.isISOControl(symbol)) {
            throw new DomainException("El símbolo debe ser un único carácter visible.");
        }
    }
}
