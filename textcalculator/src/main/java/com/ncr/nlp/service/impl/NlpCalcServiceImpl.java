package com.ncr.nlp.service.impl;

import com.ncr.nlp.service.NlpCalcService;
import com.ncr.nlp.service.util.BasicOperator;
import com.ncr.nlp.service.util.Bracket;
import com.ncr.nlp.service.util.Number;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NlpCalcServiceImpl implements NlpCalcService {
    @Override
    public double calculate(String expression) {
        List<String> evaluateSymbols = evaluateSymbol(expression);
        List<String> precendenceCorrectedExpr = processPrecendenceOperator(evaluateSymbols);
        return evaluateOperationStack(precendenceCorrectedExpr);
    }

    /**
     * converts claculation text into arithmetic representation.
     * @param expression - input text provied by user
     * @return list of arithmetic string representation
     */
    protected List<String> evaluateSymbol(String expression) {
        return Arrays.stream(expression.toLowerCase().split("\\s+"))
                .map(token -> {
                    if (BasicOperator.isOperator(token)) {
                        return BasicOperator.fromAlias(token).getArithmeticSymbol();
                    } else if (Number.isNumber(token)) {
                        return Number.fromName(token).getValue();
                    } else if (Bracket.isBracket(token)) {
                        return Bracket.fromAlias(token).getValue();
                    } else {
                        throw new IllegalArgumentException("Unable to parse token-" + token);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Simplified implementation of the shunting-yard algorithm used for parsing mathematical expressions.
     * @param evaluatedExpression list of arithmetic string values.
     * @return outputs the value based on precendence.
     */
    protected List<String> processPrecendenceOperator(List<String> evaluatedExpression) {
        List<String> output = new ArrayList<>();
        Deque<String> stack = new LinkedList<>();

        for(String expression : evaluatedExpression) {
            if (BasicOperator.isOperator(expression)) {
                BasicOperator basicOperator = BasicOperator.fromAlias(expression);
                while (!stack.isEmpty() && BasicOperator.isOperator(stack.peek())
                        && basicOperator.hasLessPriority(BasicOperator.fromAlias(stack.peek()))) {
                    output.add(stack.pop());
                }
                stack.push(expression);
            } else if (Bracket.isLeftBracket(expression)) {
                stack.push(expression);
            } else if (Bracket.isRightBracket(expression)) {
                while(!Bracket.isLeftBracket(stack.peek())) { //While there's not a left bracket at the top of the stack
                    output.add(stack.pop()); //Pop operators from the stack onto the output queue
                }
                stack.pop(); //Pop the left bracket from the stack and discard it
            }
            else {
                output.add(expression);
            }
        }

        while (!stack.isEmpty()) {
            output.add(stack.pop());
        }
        //a+b*c -> abc*+
        //a*b+c -> ab*c+
        //Reverse Polish Notation
        return output;
    }

    /**
     * Evaluates the value
     * @param precendenceCorrectedExpr - precendence corrected expression
     * @return - final calculated value
     */
    protected double evaluateOperationStack(List<String> precendenceCorrectedExpr) {
        Deque<String> stack = new LinkedList<>();
        for (String token : precendenceCorrectedExpr) {
            if (BasicOperator.isOperator(token)) {
                BasicOperator basicOperator = BasicOperator.fromAlias(token);
                double operand2 = Double.parseDouble(stack.pop());
                if(stack.isEmpty()){
                    throw new IllegalArgumentException("Insufficient token for calculation.");
                }
                double operand1 = Double.parseDouble(stack.pop());
                double result = basicOperator.apply(operand1, operand2);

                stack.push(String.valueOf(result));
            } else {
                stack.push(token);
            }
        }
        return Double.parseDouble(stack.pop());
    }
}
