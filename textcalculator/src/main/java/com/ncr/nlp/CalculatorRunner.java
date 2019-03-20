package com.ncr.nlp;

import com.ncr.nlp.service.NlpCalcService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

@Component
public class CalculatorRunner {

    private static final Logger logger = Logger.getLogger(CalculatorRunner.class);

    @Autowired
    private NlpCalcService nlpCalcService;

    public double run(String expression, PrintStream out) {
        if (expression == null) {
            throw new IllegalStateException("Expression should not be null");
        }
        double value = nlpCalcService.calculate(expression);

        printResult(out, value);

        return value;
    }

    private void printResult(PrintStream out, double value) {
        if (out == null) {
            logger.warn("PrintStream is null, couldn't print the calc output");
            return;
        }
        try (OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8")) {
            writer.write("Result: "+ String.format("%.2f", value) + System.lineSeparator());
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
