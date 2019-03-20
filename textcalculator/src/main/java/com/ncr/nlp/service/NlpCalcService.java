package com.ncr.nlp.service;

public interface NlpCalcService {
    /**
     * performs simple natural language calculations.
     * @param expression - provided b user.
     * @return final calculated value.
     */
    double calculate(String expression);
}
