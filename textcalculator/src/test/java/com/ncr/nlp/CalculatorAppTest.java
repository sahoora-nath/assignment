package com.ncr.nlp;

import com.ncr.nlp.service.impl.NlpCalcServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Integration test for Calculator App.
 */
@ContextConfiguration(classes = {CalculatorApp.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class CalculatorAppTest
{
    @Autowired
    private NlpCalcServiceImpl nlpCalcService;

    /**
     * Integration Test
     */
    @Test
    public void performCalculation() {

        assertNotNull(nlpCalcService);

        String calc1 = "one plus two times five";
        double value1 = nlpCalcService.calculate(calc1);
        assertEquals(11.0, value1, 0);

        String calc2 = "NINE minus THREE Times SEVEN";
        double value2 = nlpCalcService.calculate(calc2);
        assertEquals(-12.0, value2, 0);

        String calc3 = "seven divided-by nine plus two";
        double value3 = nlpCalcService.calculate(calc3);
        assertEquals(2.77, value3, 0.01);

        String calc4 = "four minus eight plus six multiplied-by nine";
        double value4 = nlpCalcService.calculate(calc4);
        assertEquals(50.0, value4, 0);

        String calc5="ten minus one plus three times six over five";
        double value5 = nlpCalcService.calculate(calc5);
        assertEquals(12.6, value5, 0);

        String calc6="ten times seven over four";
        double value6 = nlpCalcService.calculate(calc6);
        assertEquals(17.5, value6, 0);

    }
}
