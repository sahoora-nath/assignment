package com.bjss.assignment;

import com.bjss.assignment.data.CartTotals;
import com.bjss.assignment.data.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Integration test for Price Basket.
 */
@ContextConfiguration(classes = {PriceBasket.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class PriceBasketRunnerTest {
    @Autowired
    private PriceBasketRunner priceBasketRunner;

    @Test
    public void getItemsFromCartTest() throws PriceBasketException {
        String[] args = {"Apples", "Apples", "Bread"};
        Set<Item> itemsFromCart = priceBasketRunner.getItemsFromCart(args);

        assertNotNull(itemsFromCart);

        //duplicate item removal and quantity size check
        assertEquals(2, itemsFromCart.size());
        List<Item> apples = itemsFromCart.stream()
                .filter(item -> item.getId().equalsIgnoreCase("Apples"))
                .collect(Collectors.toList());
        assertEquals(1, apples.size());
        assertEquals(2, apples.get(0).getQuantity());
    }

    //Apples Apples
    @Test
    public void run_test_Apples_Apples() throws PriceBasketException {
        CartTotals cartTotals = priceBasketRunner.run(new String[]{"apples", "Apples"}, null);

        assertTrue(new BigDecimal("2.00").compareTo(cartTotals.getSubTotal()) == 0);
        assertTrue(new BigDecimal("1.80").compareTo(cartTotals.getTotal()) == 0);

        Item apples = cartTotals.getOfferTotals().get("apples");
        assertNotNull(apples);

        assertTrue(new BigDecimal("0.20").compareTo(apples.getDiscountPrice()) ==0 );
    }

    //Apples Bread
    @Test
    public void run_test_Apples_Bread() throws PriceBasketException {
        CartTotals cartTotals = priceBasketRunner.run(new String[]{"Bread", "Apples"}, null);

        assertTrue(new BigDecimal("1.80").compareTo(cartTotals.getSubTotal()) == 0);
        assertTrue(new BigDecimal("1.70").compareTo(cartTotals.getTotal()) == 0);

        Item apples = cartTotals.getOfferTotals().get("apples");
        assertNotNull(apples);
        assertTrue(new BigDecimal("0.10").compareTo(apples.getDiscountPrice()) ==0 );

        //No offer on Bread
        Item bread = cartTotals.getOfferTotals().get("bread");
        assertNull(bread);
    }

    //bread, soup
    @Test
    public void run_test_Bread_soup() throws PriceBasketException {
        CartTotals cartTotals = priceBasketRunner.run(new String[]{"Bread", "soup"}, null);

        assertTrue(new BigDecimal("1.45").compareTo(cartTotals.getSubTotal()) == 0);
        assertTrue(new BigDecimal("1.45").compareTo(cartTotals.getTotal()) == 0);

        //No offers on bread and soup (minimum 2 soups required.)
        Item soup = cartTotals.getOfferTotals().get("soup");
        assertNull(soup);

        Item bread = cartTotals.getOfferTotals().get("bread");
        assertNull(bread);
    }

    //bread, soup, soup
    @Test
    public void run_test_Bread_soup_soup() throws PriceBasketException {
        CartTotals cartTotals = priceBasketRunner.run(new String[]{"Bread", "soup", "soup"}, null);

        assertTrue(new BigDecimal("2.10").compareTo(cartTotals.getSubTotal()) == 0);
        assertTrue(new BigDecimal("1.70").compareTo(cartTotals.getTotal()) == 0);

        //offers on bread as 2 soup are available
        Item soup = cartTotals.getOfferTotals().get("soup");
        assertNull(soup);

        Item bread = cartTotals.getOfferTotals().get("bread");
        assertNotNull(bread);

        assertTrue(new BigDecimal("0.40").compareTo(bread.getDiscountPrice()) == 0);
    }

    //bread bread soup soup
    @Test
    public void run_test_bread_bread_soup_soup() throws PriceBasketException {
        CartTotals cartTotals = priceBasketRunner.run(new String[]{"bread", "Bread", "soup", "soup"}, null);

        assertTrue(new BigDecimal("2.90").compareTo(cartTotals.getSubTotal()) == 0);
        assertTrue(new BigDecimal("2.50").compareTo(cartTotals.getTotal()) == 0);

        //only offer on 1 bread as only 2 soups are available
        Item soup = cartTotals.getOfferTotals().get("soup");
        assertNull(soup);

        Item bread = cartTotals.getOfferTotals().get("bread");
        assertNotNull(bread);

        assertTrue(new BigDecimal("0.40").compareTo(bread.getDiscountPrice()) == 0);
    }

    //bread bread soup soup soup soup
    @Test
    public void run_test_2bread_4soup() throws PriceBasketException {
        CartTotals cartTotals = priceBasketRunner.run(new String[]{"bread", "bread", "soup", "soup", "soup", "soup"}, null);

        assertTrue(new BigDecimal("4.20").compareTo(cartTotals.getSubTotal()) == 0);
        assertTrue(new BigDecimal("3.40").compareTo(cartTotals.getTotal()) == 0);

        //offer on 2 bread as 4 soups are available
        Item soup = cartTotals.getOfferTotals().get("soup");
        assertNull(soup);

        Item bread = cartTotals.getOfferTotals().get("bread");
        assertNotNull(bread);

        assertTrue(new BigDecimal("0.80").compareTo(bread.getDiscountPrice()) == 0);
    }

    //apples milk soup bread
    @Test
    public void run_test_apples_milk_soup_bread() throws PriceBasketException {
        CartTotals cartTotals = priceBasketRunner.run(new String[]{"apples", "milk", "soup", "bread"}, null);

        assertTrue(new BigDecimal("3.75").compareTo(cartTotals.getSubTotal()) == 0);
        assertTrue(new BigDecimal("3.65").compareTo(cartTotals.getTotal()) == 0);

        //offer only on apples
        Item apples = cartTotals.getOfferTotals().get("apples");
        assertNotNull(apples);
        assertTrue(new BigDecimal("0.10").compareTo(apples.getDiscountPrice()) == 0);

        Item milk = cartTotals.getOfferTotals().get("milk");
        assertNull(milk);

        Item soup = cartTotals.getOfferTotals().get("soup");
        assertNull(soup);

        Item bread = cartTotals.getOfferTotals().get("bread");
        assertNull(bread);
    }
}
