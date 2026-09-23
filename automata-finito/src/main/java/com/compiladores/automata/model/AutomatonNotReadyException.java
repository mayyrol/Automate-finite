package com.compiladores.automata.model;

import java.util.List;

@SuppressWarnings("serial")
public final class AutomatonNotReadyException extends IllegalStateException {
    private final List<ValidationIssue> issues;

    public AutomatonNotReadyException(List<ValidationIssue> issues) {
        super(issues.stream().map(ValidationIssue::message).reduce((a, b) -> a + " " + b).orElse(""));
        this.issues = List.copyOf(issues);
    }

    public List<ValidationIssue> issues() {
        return issues;
    }
}
