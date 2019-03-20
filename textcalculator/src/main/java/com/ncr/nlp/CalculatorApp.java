package com.ncr.nlp;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Scanner;

/**
 * Natural Language Calculator.
 * This is the main console application to perform simple natural language calculations.
 */
@ComponentScan(basePackages = "com.ncr.nlp")
public class CalculatorApp {
    private static final Logger logger = Logger.getLogger(CalculatorApp.class);

    @Autowired
    private CalculatorRunner runner;

    public static void main(String[] args) {
        ApplicationContext context
                = new AnnotationConfigApplicationContext(CalculatorApp.class);

        CalculatorApp calculatorApp = context.getBean(CalculatorApp.class);

        logger.info("System is ready. Please enter calculation text:");
        String expression = calculatorApp.readFromStandardInput();

        calculatorApp.start(expression);
    }

    private String readFromStandardInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextLine();
        }
    }

    private void start(String expression) {
        runner.run(expression, System.out);
    }
}
