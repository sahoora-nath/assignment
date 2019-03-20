package com.bjss.assignment.data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "products")
public class Products {

    private List<Item> item;
    private List<Offer> percentageOffer;

    public List<Item> getItem() {
        if (item == null) {
            item = new ArrayList<>();
        }
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    public List<Offer> getPercentageOffer() {
        if (percentageOffer == null) {
            percentageOffer = new ArrayList<>();
        }
        return percentageOffer;
    }

    public void setPercentageOffer(List<Offer> percentageOffer) {
        this.percentageOffer = percentageOffer;
    }
}
