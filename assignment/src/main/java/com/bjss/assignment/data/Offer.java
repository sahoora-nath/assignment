package com.bjss.assignment.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Date;

@XmlRootElement(name = "percentageOffer")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Offer {
    private String id;
    private String itemRef;
    private BigDecimal discount;
    private Date expiryDate;
    private RequiredItems requiredItems;

    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute
    public String getItemRef() {
        return itemRef;
    }

    public void setItemRef(String itemRef) {
        this.itemRef = itemRef;
    }

    @XmlAttribute
    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    @XmlAttribute
    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public RequiredItems getRequiredItems() {
        return requiredItems;
    }

    public void setRequiredItems(RequiredItems requiredItems) {
        this.requiredItems = requiredItems;
    }
}
