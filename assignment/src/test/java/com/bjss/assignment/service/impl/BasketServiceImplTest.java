package com.bjss.assignment.service.impl;

import com.bjss.assignment.PriceBasket;
import com.bjss.assignment.PriceBasketException;
import com.bjss.assignment.data.Cart;
import com.bjss.assignment.data.CartTotals;
import com.bjss.assignment.data.Item;
import com.bjss.assignment.data.Offer;
import com.bjss.assignment.service.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

@ContextConfiguration(classes = {PriceBasket.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class BasketServiceImplTest {
    @Autowired
    private BasketServiceImpl basketService;
    @Autowired
    private ItemService itemService;

    @Test
    public void isOfferApplicableTest() {
        Set<Item> cartItem = new HashSet<>();
        Item item = new Item();
        item.setQuantity(1);
        item.setId("Apples");
        item.setPrice(new BigDecimal(1.00));

        cartItem.add(item);

        boolean isOfferValid = basketService.isOfferApplicable(item, cartItem);
        assertTrue(isOfferValid);
    }

    @Test
    public void hasApplicableRequiredItemsTest() throws PriceBasketException {
        Set<Item> cartItem = new HashSet<>();
        Item item = new Item();
        item.setQuantity(1);
        item.setId("Apples");
        item.setPrice(new BigDecimal(1.00));

        cartItem.add(item);
        Offer offer = itemService.getOffer(item.getId());
        boolean hasRequiredItems = basketService.hasApplicableRequiredItems(item, offer, cartItem);

        assertNotNull(item.getDiscount());
        assertTrue(hasRequiredItems);
    }

    @Test
    public void hasApplicableRequiredItems_MultibuyTest() throws PriceBasketException {
        Set<Item> cartItem = new HashSet<>();
        Item item = new Item();
        item.setQuantity(1);
        item.setId("Bread");
        item.setPrice(new BigDecimal(0.80));

        Item soup = new Item();
        soup.setQuantity(2);
        soup.setId("Soup");
        soup.setPrice(new BigDecimal(0.65));

        cartItem.add(item);
        cartItem.add(soup);
        Offer offer = itemService.getOffer(item.getId());
        boolean hasRequiredItems = basketService.hasApplicableRequiredItems(item, offer, cartItem);

        assertNotNull(item.getDiscount());
        assertTrue(hasRequiredItems);
        assertEquals(1, item.getDiscountTimes());

        //Test with bread=1 and soup=3
        soup.setQuantity(3);
        basketService.hasApplicableRequiredItems(item, offer, cartItem);

        assertNotNull(item.getDiscount());
        assertEquals(1, item.getDiscountTimes());

        //Test with bread=1 and soup=4
        soup.setQuantity(4);
        basketService.hasApplicableRequiredItems(item, offer, cartItem);

        assertNotNull(item.getDiscount());
        assertEquals(1, item.getDiscountTimes());

        //Test with bread=2 and soup=4
        soup.setQuantity(4);
        item.setQuantity(2);
        basketService.hasApplicableRequiredItems(item, offer, cartItem);

        assertNotNull(item.getDiscount());
        assertEquals(2, item.getDiscountTimes());
    }

    @Test
    public void calculateOfferTotalsTest() {
        Set<Item> items = new HashSet<>();
        Item item = new Item();
        item.setQuantity(1);
        item.setId("Apples");
        item.setPrice(new BigDecimal(1.00));

        items.add(item);

        Map<String, Item> stringItemMap = basketService.calculateOfferTotals(items);
        assertNotNull(stringItemMap);
        assertEquals(1, stringItemMap.size());

        Collection<Item> values = stringItemMap.values();

        long count = values.stream().peek(i -> {
            assertNotNull(i.getDiscount());
            assertNotNull(i.getDiscountPrice());
        }).count();
        assertEquals(1, count);
    }

    @Test
    public void isBeforeExpiryDateTest() {
        Date expiryDate = new GregorianCalendar(2019, Calendar.FEBRUARY, 8).getTime();

        Date currentDate = new GregorianCalendar(2019, Calendar.FEBRUARY, 5).getTime();
        boolean beforeExpiry = basketService.isBeforeExpiryDate(expiryDate, currentDate);
        assertTrue(beforeExpiry);

        currentDate = new GregorianCalendar(2019, Calendar.FEBRUARY, 11).getTime();
        boolean afterExpiry = basketService.isBeforeExpiryDate(expiryDate, currentDate);
        assertTrue(!afterExpiry);
    }

    @Test
    public void calculateSubTotalTest() {
        Set<Item> cartItem = new HashSet<>();
        Item item = new Item();
        item.setQuantity(1);
        item.setId("Bread");
        item.setPrice(new BigDecimal(0.80));

        Item soup = new Item();
        soup.setQuantity(2);
        soup.setId("Soup");
        soup.setPrice(new BigDecimal(0.65));

        cartItem.add(item);
        cartItem.add(soup);

        Cart cart = new Cart();
        cart.setItems(cartItem);

        BigDecimal subTotal = basketService.calculateSubTotal(cart);
        assertNotNull(subTotal);

        assertTrue(new BigDecimal(2.10).compareTo(subTotal) == 0);
    }

    @Test
    public void calculateCartTotalTest() {
        Set<Item> cartItem = new HashSet<>();
        Item item = new Item();
        item.setQuantity(1);
        item.setId("Bread");
        item.setPrice(new BigDecimal(0.80));

        Item soup = new Item();
        soup.setQuantity(2);
        soup.setId("Soup");
        soup.setPrice(new BigDecimal(0.65));

        cartItem.add(item);
        cartItem.add(soup);

        Cart cart = new Cart();
        cart.setItems(cartItem);

        CartTotals cartTotals = basketService.calculateCartTotal(cart);
        assertNotNull(cartTotals);

        assertTrue(new BigDecimal(2.10).compareTo(cartTotals.getSubTotal()) == 0);

        Map<String, Item> offerTotals = cartTotals.getOfferTotals();
        Item breadOffer = offerTotals.get("bread");
        assertNotNull(breadOffer);
    }
}
