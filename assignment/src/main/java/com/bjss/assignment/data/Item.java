package com.bjss.assignment.data;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Objects;

@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Item {
    private String id;
    private BigDecimal price;

    @XmlTransient
    private int quantity;

    @XmlTransient
    private int discountTimes;

    @XmlTransient
    private BigDecimal discount;

    @XmlTransient
    private BigDecimal discountPrice;

    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDiscountTimes() {
        return discountTimes;
    }

    public void setDiscountTimes(int discountTimes) {
        this.discountTimes = discountTimes;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Item calculateDiscount() {
        if (this.getDiscountTimes() != 0 && this.getDiscount() != null) {
            this.setDiscountPrice(new BigDecimal(this.getDiscountTimes())
                    .multiply(this.getDiscount())
                    .multiply(getPrice()));
        } else {
            this.setDiscountPrice(BigDecimal.ZERO);
        }
        return this;
    }

    public Item increaseQuantity() {
        setQuantity(getQuantity() + 1);
        return this;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", discountTimes=" + discountTimes +
                ", discount=" + discount +
                ", discountPrice=" + discountPrice +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return Objects.equals(getId(), item.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }
}
