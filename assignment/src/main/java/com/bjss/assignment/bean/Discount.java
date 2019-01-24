package com.bjss.assignment.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement(name="discount")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Discount implements Serializable {
    private Date startDate;
    private Date endDate;
    private Double percentage;
    private Multibuy multibuy;

    public Discount(){
        //default constructor
    }
    public Discount(Date startDate, Date endDate, Double percentage) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.percentage = percentage;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Multibuy getMultibuy() {
        return multibuy;
    }

    public void setMultibuy(Multibuy multibuy) {
        this.multibuy = multibuy;
    }
}
