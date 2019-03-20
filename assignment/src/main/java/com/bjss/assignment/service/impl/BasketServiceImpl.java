package com.bjss.assignment.service.impl;

import com.bjss.assignment.PriceBasketException;
import com.bjss.assignment.data.*;
import com.bjss.assignment.service.BasketService;
import com.bjss.assignment.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BasketServiceImpl implements BasketService {

    @Autowired
    private ItemService itemService;

    public CartTotals calculateCartTotal(Cart cart) {
        CartTotals totals = new CartTotals();

        BigDecimal subTotal = calculateSubTotal(cart);
        Map<String, Item> offerTotals = calculateOfferTotals(cart.getItems());
        BigDecimal total = calculateTotal(subTotal, offerTotals);

        totals.setSubTotal(subTotal);
        totals.setOfferTotals(offerTotals);
        totals.setTotal(total);

        return totals;
    }

    public Map<String, Item> calculateOfferTotals(Set<Item> cartItem) {
        return cartItem.stream()
                .filter(item -> isOfferApplicable(item, cartItem))
                .map(Item::calculateDiscount)
                .collect(Collectors.toMap(item -> item.getId().toLowerCase(),
                        Function.identity()));
    }

    /**
     * Iterates through all items and calculates the sum of all items.
     *
     * @param cart items in the cart
     * @return subtotal amount from cart
     */
    public BigDecimal calculateSubTotal(Cart cart) {
        return cart.getItems().stream()
                .map(t -> t.getPrice().multiply(new BigDecimal(t.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotal(BigDecimal subTotal,
                                      Map<String, Item> offerTotals) {
        BigDecimal discountTotals = offerTotals.entrySet().stream()
                .map(entrySet -> entrySet.getValue().getDiscountPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return subTotal.subtract(discountTotals);
    }


    public boolean isOfferApplicable(Item item, Set<Item> cartItem) {
        try {
            Offer offer = itemService.getOffer(item.getId());
            if (offer == null) {
                return false;
            }
            boolean isBeforeExpired = isBeforeExpiryDate(offer.getExpiryDate(), new Date());
            boolean hasApplicableRequiredItems = hasApplicableRequiredItems(item, offer, cartItem);
            return isBeforeExpired && hasApplicableRequiredItems;

        } catch (PriceBasketException e) {
            return false;
        }
    }

    public boolean isBeforeExpiryDate(Date expiryDate, Date currentDate) {
        return expiryDate == null || expiryDate.compareTo(currentDate) >= 0;
    }

    public boolean hasApplicableRequiredItems(Item item, Offer offer, Set<Item> cartItem) {
        if (offer == null) {
            return false;
        } else if (offer.getRequiredItems() == null || offer.getRequiredItems().getItemRef().isEmpty()) {
            item.setDiscount(offer.getDiscount());
            item.setDiscountTimes(item.getQuantity());
            return true;
        }

        List<ItemRef> validOfferList = offer.getRequiredItems().getItemRef().stream()
                .filter(itemRef -> {
                    Item itemInCart = getElementFromCartCollection(itemRef.getId(), cartItem); //soup

                    /*
                     * checks how many times discount can be applied.
                     * i.e. if you buy 4 soup and 2 Bread, then 2 times discount will be applied
                     * similarly if you buy 3 Soup and 1 Bread, only 1 discount is applied.
                     */
                    if (itemInCart != null) {
                        if (itemInCart.getQuantity() >= itemRef.getQuantity()) {
                            int discountApplyTimes = itemInCart.getQuantity() / itemRef.getQuantity();
                            if (discountApplyTimes > item.getQuantity()) {
                                discountApplyTimes = item.getQuantity();
                            }
                            item.setDiscountTimes(discountApplyTimes);
                            item.setDiscount(offer.getDiscount());
                            return true;
                        }
                        return false;
                    } else {
                        return false;
                    }
                })
                .collect(Collectors.toList());

        return !validOfferList.isEmpty();
    }

    private Item getElementFromCartCollection(String itemId, Set<Item> cartItem) {
        List<Item> itemList = cartItem.stream().filter(item1 -> item1.getId().equalsIgnoreCase(itemId))
                .collect(Collectors.toList());
        if (!itemList.isEmpty()) {
            return itemList.get(0);
        }
        return null;
    }
}
