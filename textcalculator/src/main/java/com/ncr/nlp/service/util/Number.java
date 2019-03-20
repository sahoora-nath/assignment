package com.ncr.nlp.service.util;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Number {
    ZERO("zero", 0),
    ONE("one", 1),
    TWO("two", 2),
    THREE("three", 3),
    FOUR("four", 4),
    FIVE("five", 5),
    SIX("six", 6),
    SEVEN("seven", 7),
    EIGHT("eight", 8),
    NINE("nine", 9),
    TEN("ten", 10);

    private final String name;
    private final int value;

    private Number(String name, int value) {
        this.name = name;
        this.value = value;
    }

    private static final Map<String, Number> nameToNumber;

    static {
        nameToNumber =
                Arrays.stream(Number.values())
                        .collect(Collectors.toMap(n -> n.name, Function.identity()));
    }

    /**
     * retrievea a specific {@link Number} enum constant given its human readable string.
     * @param numberName is the readable string.
     * @return the enum constant.
     */
    public static Number fromName(String numberName) {
        return Optional.ofNullable(nameToNumber.get(numberName))
                .orElseThrow(() -> new IllegalArgumentException("Unable to find number from name" + numberName));
    }

    /**
     * checks if the given token is a Number.
     * @param tokenName token name
     * @return if the toeknName is a number.
     */
    public static boolean isNumber(String tokenName) {
        return nameToNumber.containsKey(tokenName);
    }

    /**
     * gets string value of the number.
     * @return string value of value
     */
    public String getValue() {
        return String.valueOf(value);
    }
}
