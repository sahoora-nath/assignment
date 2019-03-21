package com.ncr.nlp.service.util;

import java.util.*;

public enum Bracket {

    LEFTB("(", "leftb", "left-bracket"),
    RIGHTB(")", "rightb", "right-bracket");

    /**
     * Symbol represents the arithmetic operator e.g. '(', ')'.
     */
    private final String symbol;

    /**
     * Set of permitted arithmetic operator.
     */
    private final Set<String> aliases;

    Bracket(String symbol, String... aliases) {
        this.symbol = symbol;
        this.aliases = new HashSet<>();
        this.aliases.add(symbol);
        Collections.addAll(this.aliases, aliases);
    }

    private static final Map<String, Bracket> aliasBracketToOperator = new HashMap<>();
    static {
        Arrays.stream(Bracket.values())
                .forEach(operator ->
                        operator.aliases.forEach(alias -> aliasBracketToOperator.put(alias, operator))
                );
    }

    public static boolean isBracket(String token) {
        return aliasBracketToOperator.containsKey(token);
    }

    public static boolean isLeftBracket(String token) {
        return aliasBracketToOperator.containsKey(token) && token.equals(LEFTB.getValue());
    }

    public static boolean isRightBracket(String token) {
        return aliasBracketToOperator.containsKey(token) && token.equals(RIGHTB.getValue());
    }

    public static Bracket fromAlias(String operatorAlias) {
        return Optional.ofNullable(aliasBracketToOperator.get(operatorAlias))
                .orElseThrow(() -> new IllegalArgumentException("Unable to find operator from alias" + operatorAlias));
    }

    public String getValue() {
        return String.valueOf(symbol);
    }
}