package com.bjss.assignment.service;

import com.bjss.assignment.PriceBasketException;
import com.bjss.assignment.data.Item;
import com.bjss.assignment.data.Offer;

import java.util.List;

public interface ItemService {

    /**
     * Get the items with product quantities based on the items in the argument provided.
     *
     * @return all items.
     */
    List<Item> getAvailableItems() throws PriceBasketException;

    /**
     * Discount details of all Items.
     *
     * @return all offers
     * @throws PriceBasketException while fetching product or offer details.
     */
    List<Offer> getOffers() throws PriceBasketException;

    /**
     * An item from the catalogue that matches the given item name.
     *
     * @param name the id of the item. Name argument is case-insensitive
     * @return the item applied to the given name
     */
    Item getItem(String name);


    /**
     * Return the {@link Offer} applied to the given {@link Item}
     *
     * @param itemName name of item
     * @return the offer applied to the given name.
     */
    Offer getOffer(String itemName) throws PriceBasketException;

}