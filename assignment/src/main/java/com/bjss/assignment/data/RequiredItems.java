package com.bjss.assignment.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "itemRef")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class RequiredItems {
    private List<ItemRef> itemRef;

    public List<ItemRef> getItemRef() {
        if (itemRef == null) {
            itemRef = new ArrayList<>();
        }
        return itemRef;
    }

    public void setItemRef(List<ItemRef> itemRef) {
        this.itemRef = itemRef;
    }
}
