package com.bjss.assignment.service.impl;

import com.bjss.assignment.PriceBasketException;
import com.bjss.assignment.data.Item;
import com.bjss.assignment.data.Offer;
import com.bjss.assignment.data.Products;
import com.bjss.assignment.service.ItemService;
import com.bjss.assignment.util.XmlProcessor;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ItemServiceImpl implements ItemService {

    @Override
    public List<Item> getAvailableItems() throws PriceBasketException {
        try {
            String fileDB = readProductsFromFile();
            Products products = (Products) XmlProcessor.unmarshal(Products.class, fileDB);
            return products.getItem();
        } catch (IOException | JAXBException e) {
            throw new PriceBasketException(e.getMessage(), e);
        }
    }

    public List<Offer> getOffers() throws PriceBasketException {
        try {
            String fileDB = readProductsFromFile();
            Products products = (Products) XmlProcessor.unmarshal(Products.class, fileDB);
            return products.getPercentageOffer();
        } catch (IOException | JAXBException e) {
            throw new PriceBasketException(e.getMessage(), e);
        }
    }

    public Offer getOffer(String itemName) throws PriceBasketException {
        if (itemName == null) {
            throw new PriceBasketException("Illegal argument");
        }
        Optional<Offer> offerOptional = getOffers().stream()
                .filter(item -> item.getItemRef().equalsIgnoreCase(itemName)).findFirst();
        return offerOptional.orElse(null);
    }

    @Override
    public Item getItem(String name) {
        if (name == null) {
            return null;
        }
        try {
            Optional<Item> itemOptional = getAvailableItems().stream()
                    .filter(item -> item.getId().equalsIgnoreCase(name)).findFirst();
            if (itemOptional.isPresent()) {
                return itemOptional.get();
            }
        } catch (PriceBasketException ex) {
            return null;
        }
        return null;
    }

    private String readProductsFromFile() throws IOException {
        Path filePath = Paths.get("products.xml");
        try (Stream<String> streamStr = Files.lines(filePath)) {
            return streamStr.collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
