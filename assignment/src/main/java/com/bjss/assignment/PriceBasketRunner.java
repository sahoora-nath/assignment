package com.bjss.assignment;

import com.bjss.assignment.data.Cart;
import com.bjss.assignment.data.CartTotals;
import com.bjss.assignment.data.Item;
import com.bjss.assignment.service.BasketPrinterService;
import com.bjss.assignment.service.BasketService;
import com.bjss.assignment.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PriceBasketRunner {

    @Autowired
    private BasketService basketService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BasketPrinterService basketPrinterService;


    public CartTotals run(String[] args, PrintStream out) throws PriceBasketException {
        if (args == null) {
            throw new IllegalStateException("items should not be null");
        }

        Set<Item> itemSet = getItemsFromCart(args);

        Cart cart = new Cart();
        cart.setItems(itemSet);
        CartTotals cartTotals = basketService.calculateCartTotal(cart);
        basketPrinterService.write(out, cartTotals);
        return cartTotals;
    }

    /**
     * creates a set of items based on the command line argument values.
     * removes duplicate and increase the quantity in duplicate items
     */
    public Set<Item> getItemsFromCart(String[] args) throws PriceBasketException {
        //Get list of available products.
        List<Item> items = itemService.getAvailableItems();

        //create a Map of products with Key as product name.
        Map<String, Item> productByNameMap =
                items.stream().collect(Collectors.toMap(p -> p.getId().toLowerCase(),
                        Function.identity()));

        return Arrays.stream(args)
                .map(arg -> productByNameMap.get(arg.toLowerCase()))
                .filter(Objects::nonNull)
                .map(Item::increaseQuantity)
                .collect(Collectors.toSet());
    }
}
