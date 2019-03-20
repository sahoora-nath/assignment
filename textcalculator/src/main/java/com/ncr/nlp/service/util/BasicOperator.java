package com.ncr.nlp.service.util;

import java.util.*;

public enum BasicOperator {

    ADD("+", 1, "add", "plus") {
        public double apply(double operand1, double operand2) {
            return operand1 + operand2;
        }
    },
    SUBTRACT("-", 2, "subtract", "minus", "less") {
        @Override
        public double apply(double operand1, double operand2) {
            return operand1 - operand2;
        }
    },
    MULTIPLY("*", 3, "multiplied-by", "times") {
        @Override
        public double apply(double operand1, double operand2) {
            return operand1 * operand2;
        }
    },
    DIVIDE("/", 4, "divided-by", "over") {
        @Override
        public double apply(double operand1, double operand2) {
            return operand1 / operand2;
        }
    };

    /**
     * Symbol represents the arithmetic operator (e.g. '+', '-', '*', '/').
     */
    private final String symbol;

    /**
     * int value represents the arithmetic operator precedence.
     */
    private final int precedence;

    /**
     * Set of permitted arithmetic operator.
     */
    private final Set<String> aliases;

    BasicOperator(String symbol, int precedence, String... aliases) {
        this.symbol = symbol;
        this.precedence = precedence;
        this.aliases = new HashSet<>();
        this.aliases.add(symbol);
        Collections.addAll(this.aliases, aliases);
    }

    /**
     * Each method implements this method to provide their operations.
     *
     * @param operand1 first num involved in calculation.
     * @param operand2 second num involved in calculation.
     * @return the result of operations.
     */
    public abstract double apply(double operand1, double operand2);


    private static final Map<String, BasicOperator> aliasToOperator = new HashMap<>();
    static {
        Arrays.stream(BasicOperator.values())
                .forEach(operator ->
                    operator.aliases.forEach(alias -> aliasToOperator.put(alias, operator))
                );
    }

    /**
     * checks the precendence of one arithmetic operators with another.
     *
     * @param basicOperator operator to check the precedence
     * @return if precedence is lesser than other
     */
    public boolean hasLessPriority(BasicOperator basicOperator) {
        return this.precedence < basicOperator.precedence;
    }

    /**
     * Validates the token
     * @param token to validate
     * @return true if the token is valid operator.
     */
    public static boolean isOperator(String token) {
        return aliasToOperator.containsKey(token);
    }

    public static BasicOperator fromAlias(String operatorAlias) {
        return Optional.ofNullable(aliasToOperator.get(operatorAlias))
                .orElseThrow(() -> new IllegalArgumentException("Unable to find operator from alias" + operatorAlias));
    }

    public String getArithmeticSymbol() {
        return symbol;
    }
}
