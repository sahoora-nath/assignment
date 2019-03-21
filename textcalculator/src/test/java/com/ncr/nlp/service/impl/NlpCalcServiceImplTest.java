package com.ncr.nlp.service.impl;

import com.ncr.nlp.CalculatorApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@ContextConfiguration(classes = {CalculatorApp.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class NlpCalcServiceImplTest {

    @Autowired
    private NlpCalcServiceImpl nlpCalcService;

    @Test
    public void evaluateSymbolTest() {
        String inputExpression = "two minus one";
        List<String> evaluatedList = nlpCalcService.evaluateSymbol(inputExpression);
        assertNotNull(evaluatedList);
        assertFalse(evaluatedList.isEmpty());
        assertEquals(3, evaluatedList.size());

        assertEquals("2", evaluatedList.get(0));
        assertEquals("-", evaluatedList.get(1));
        assertEquals("1", evaluatedList.get(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void evaluateSymbolTestExpectedFailure() {
        String inputExpression = "two xyz";
        nlpCalcService.evaluateSymbol(inputExpression);
    }

    @Test
    public void processPrecendenceOperatorTest() {
        List<String> asList = Arrays.asList("5", "-", "2", "*", "10", "/", "2");
        List<String> outputList = nlpCalcService.processPrecendenceOperator(asList);

        assertNotNull(outputList);
        assertFalse(outputList.isEmpty());
        assertEquals(7, outputList.size());

        assertEquals("5", outputList.get(0));
        assertEquals("2", outputList.get(1));
        assertEquals("10", outputList.get(2));
        assertEquals("2", outputList.get(3));
        assertEquals("/", outputList.get(4));
        assertEquals("*", outputList.get(5));
        assertEquals("-", outputList.get(6));

        List<String> asList1 = Arrays.asList("5", "-", "2", "*", "10", "+", "2");
        List<String> outputList1 = nlpCalcService.processPrecendenceOperator(asList1);

        //5, 2, 10, *, -, 2, +
        assertEquals("5", outputList1.get(0));
        assertEquals("2", outputList1.get(1));
        assertEquals("10", outputList1.get(2));
        assertEquals("*", outputList1.get(3));
        assertEquals("-", outputList1.get(4));
        assertEquals("2", outputList1.get(5));
        assertEquals("+", outputList1.get(6));

        List<String> asList2 = Arrays.asList("(", "5", "-", "2", ")", "*", "10", "+", "2");
        List<String> outputList2 = nlpCalcService.processPrecendenceOperator(asList2);

        //[5, 2, -, 10, *, 2, +]
        assertEquals("5", outputList2.get(0));
        assertEquals("2", outputList2.get(1));
        assertEquals("-", outputList2.get(2));
        assertEquals("10", outputList2.get(3));
        assertEquals("*", outputList2.get(4));
        assertEquals("2", outputList2.get(5));
        assertEquals("+", outputList2.get(6));
    }

    @Test
    public void evaluateOperationStackTest() {
        List<String> asList = Arrays.asList("5", "2", "10", "2", "/", "*", "-");
        double v1 = nlpCalcService.evaluateOperationStack(asList);
        assertEquals(-5.0, v1, 0);

        List<String> asList1 = Arrays.asList("5", "2", "10", "*", "-", "2", "+");
        double v2 =nlpCalcService.evaluateOperationStack(asList1);
        assertEquals(-13.0, v2, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void evaluateOperationStackTestExpectedFailure() {
        List<String> asList = Arrays.asList("5", "+", "10", "2", "/", "*");
        nlpCalcService.evaluateOperationStack(asList);
    }
}
