package com.bjss.assignment.bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="products")
public class Products implements Serializable {

    private List<Product> product;

    public List<Product> getProduct() {
        if (product == null) {
            product = new ArrayList<>();
        }
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }
}
