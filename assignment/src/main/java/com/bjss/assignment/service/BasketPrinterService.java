package com.bjss.assignment.service;

import com.bjss.assignment.PriceBasketException;
import com.bjss.assignment.data.CartTotals;

import java.io.PrintStream;

public interface BasketPrinterService {
    /**
     * Writes the CartTotals to the given PrintStream
     *
     * @param out    PrintStream to be written to.
     * @param totals a populated to be printed
     */
    void write(PrintStream out, CartTotals totals) throws PriceBasketException;
}
