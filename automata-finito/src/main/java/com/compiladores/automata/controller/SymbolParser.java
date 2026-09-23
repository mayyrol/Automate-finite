package com.compiladores.automata.controller;

import com.compiladores.automata.model.DomainException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/** Traduce la notación académica del alfabeto a símbolos de un carácter. */
public final class SymbolParser {
    private SymbolParser() {
    }

    public static Set<Character> parse(String notation) {
        if (notation == null) {
            throw new DomainException("El alfabeto no puede ser nulo.");
        }
        LinkedHashSet<Character> symbols = new LinkedHashSet<>();
        for (int index = 0; index < notation.length(); index++) {
            char symbol = notation.charAt(index);
            if (isNotationSeparator(symbol)) {
                continue;
            }
            if (!symbols.add(symbol)) {
                throw new DomainException("El símbolo '" + symbol + "' está repetido.");
            }
        }
        return symbols;
    }

    public static String format(Collection<Character> symbols) {
        StringBuilder result = new StringBuilder("{");
        int index = 0;
        for (Character symbol : symbols) {
            if (index++ > 0) {
                result.append(", ");
            }
            result.append(symbol);
        }
        return result.append('}').toString();
    }

    private static boolean isNotationSeparator(char symbol) {
        return Character.isWhitespace(symbol)
                || symbol == ',' || symbol == ';'
                || symbol == '{' || symbol == '}'
                || symbol == '(' || symbol == ')'
                || symbol == '[' || symbol == ']';
    }
}
