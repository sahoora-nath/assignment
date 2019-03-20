package com.bjss.assignment.service.impl;

import com.bjss.assignment.PriceBasket;
import com.bjss.assignment.data.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(classes = {PriceBasket.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class BasketPrinterServiceImplTest {

    @Autowired
    private BasketPrinterServiceImpl basketPrinterService;

    @Test
    public void getSubTotalMsgTest() {
        String subTotalMsg = basketPrinterService.getSubTotalMsg(new BigDecimal(12.00));
        assertNotNull(subTotalMsg);
        assertEquals("Subtotal: £12.00", subTotalMsg);
    }

    @Test
    public void getOffersMsgTest() {
        Map<String, Item> offerTotals = new HashMap<>();
        String emptyOffersMsg = basketPrinterService.getOffersMsg(offerTotals);
        assertNotNull(emptyOffersMsg);
        assertEquals("(no offers available)", emptyOffersMsg);

        Item apples = new Item();
        apples.setQuantity(1);
        apples.setId("Apples");
        apples.setPrice(new BigDecimal(1.00));
        apples.setDiscountTimes(1);

        //set Discount percentage
        apples.setDiscount(new BigDecimal(0.10).setScale(1, RoundingMode.DOWN));
        apples.calculateDiscount();

        offerTotals.put("apples", apples);
        String offersMsg = basketPrinterService.getOffersMsg(offerTotals);

        assertEquals("Apples 10.0% off: -10p", offersMsg);
    }

    @Test
    public void getTotalMsgTest() {
        String totalMsg = basketPrinterService.getTotalMsg(new BigDecimal(12.00));
        assertNotNull(totalMsg);
        assertEquals("Total: £12.00", totalMsg);
    }
}
