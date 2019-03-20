package com.ncr.nlp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = {CalculatorApp.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class CalculatorRunnerTest {

    @Autowired
    private CalculatorRunner calculatorRunner;

    @Test
    public void runTest() {
        String calc1 = "nine over eight plus four times two divided-by three";
        double value = calculatorRunner.run(calc1, null);
        assertEquals(3.79, value, 0.01);
    }
}
