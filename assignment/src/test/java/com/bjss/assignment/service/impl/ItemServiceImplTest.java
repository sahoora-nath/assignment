package com.bjss.assignment.service.impl;

import com.bjss.assignment.PriceBasket;
import com.bjss.assignment.PriceBasketException;
import com.bjss.assignment.data.Item;
import com.bjss.assignment.data.Offer;
import com.bjss.assignment.service.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@ContextConfiguration(classes = {PriceBasket.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ItemServiceImplTest {

    @Autowired
    private ItemService itemService;

    @Test
    public void getAvailableItemsTest() throws PriceBasketException {
        List<Item> availableItems = itemService.getAvailableItems();
        assertTrue(!availableItems.isEmpty());

        List<Item> nonNullItems = availableItems.stream()
                .filter(item -> item.getId() != null)
                .collect(Collectors.toList());
        assertTrue(!nonNullItems.isEmpty());
    }

    @Test
    public void getPercentageOffersTest() throws PriceBasketException {
        List<Offer> availableOffers = itemService.getOffers();
        assertTrue(!availableOffers.isEmpty());

        //check if ItemRef attribute is set in the object
        List<Offer> nonNullOffers = availableOffers.stream()
                .filter(item -> item.getItemRef() != null)
                .collect(Collectors.toList());
        assertTrue(!nonNullOffers.isEmpty());

        //check if <requiredItems> values are set.
        List<Offer> requiredItems = availableOffers.stream()
                .filter(item -> item.getRequiredItems() != null)
                .collect(Collectors.toList());
        assertTrue(!requiredItems.isEmpty());
    }

    @Test
    public void getOfferTest() throws PriceBasketException {
        Offer apple = itemService.getOffer("Apples");
        assertNotNull(apple);

        Offer milk = itemService.getOffer("Milk");
        assertNull(milk);
    }

    @Test
    public void testGetItemNull() throws PriceBasketException {
        ItemServiceImpl itemService = new ItemServiceImpl();
        Item item = itemService.getItem(null);
        assertNull(item);
    }

    @Test(expected = PriceBasketException.class)
    public void testGetItemEmpty() throws PriceBasketException {
        ItemServiceImpl itemService = new ItemServiceImpl();
        assertEquals(null, itemService.getItem("apple"));
    }
}
