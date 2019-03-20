package com.bjss.assignment.data;

import java.util.HashSet;
import java.util.Set;

public class Cart {
    private Set<Item> items;

    public Set<Item> getItems() {
        if (items == null) {
            items = new HashSet<>();
        }
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}
