package com.bjss.assignment.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@XmlRootElement(name="product")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Product implements Serializable {

    private Integer id;
    private String name;
    private Double price;
    private Discount discount;

    @XmlTransient
    private int numberOfUnits;

    @XmlTransient
    private double discountPrice;

    public Product(){
        //default constructor
    }

    public Product(Integer id, String name, Double price, Discount discount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discount = discount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(getId(), product.getId()) &&
                Objects.equals(getName().toLowerCase(), product.getName().toLowerCase());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getName());
    }

    public void increaseQuantity() {
        this.setNumberOfUnits(this.getNumberOfUnits()+1);
    }

    /**
     * Apply discount if current date is between start date and end date.
     */
    public void updateDiscountPrice() {
        if (this.getDiscount()!=null && this.getDiscount().getStartDate() != null) {
            Date currentDate = new Date();
            if(this.getDiscount().getStartDate().compareTo(currentDate) <=0
                    && (this.getDiscount().getEndDate() == null ||
                            this.getDiscount().getEndDate().compareTo(currentDate) > 0)) {
                //Apply discount
                double percentagePrice = (this.getPrice() * this.getDiscount().getPercentage()/100);
                this.setDiscountPrice(this.getDiscountPrice() + percentagePrice);
            }
        }
    }
}
