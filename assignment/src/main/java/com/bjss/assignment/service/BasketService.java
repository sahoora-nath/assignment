package com.bjss.assignment.service;

import com.bjss.assignment.data.Cart;
import com.bjss.assignment.data.CartTotals;

/**
 * Calculates totals of all items to be printed.
 */
public interface BasketService {

    CartTotals calculateCartTotal(Cart cart);
}
