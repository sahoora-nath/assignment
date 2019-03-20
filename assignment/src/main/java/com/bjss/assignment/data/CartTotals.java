package com.bjss.assignment.data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CartTotals {
    private BigDecimal subTotal;
    private Map<String, Item> offerTotals;
    private BigDecimal total;

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public Map<String, Item> getOfferTotals() {
        if (offerTotals == null) {
            offerTotals = new HashMap<>();
        }
        return offerTotals;
    }

    public void setOfferTotals(Map<String, Item> offerTotals) {
        this.offerTotals = offerTotals;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
