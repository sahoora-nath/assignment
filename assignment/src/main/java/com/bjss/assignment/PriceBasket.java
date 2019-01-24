package com.bjss.assignment;

import com.bjss.assignment.bean.Discount;
import com.bjss.assignment.bean.Product;
import com.bjss.assignment.bean.Products;
import com.bjss.assignment.util.XmlProcessor;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class PriceBasket
{
    public static void main( String[] args) throws PriceBasketException {
        PriceBasket priceBasket = new PriceBasket();

        //Get list of available products.
        Products products = null;
        try {
            products = priceBasket.getAvailableProducts();
        } catch (IOException e) {
            throw new PriceBasketException(e.getMessage(), e);
        } catch (JAXBException e) {
            throw new PriceBasketException(e.getMessage(), e);
        }

        String output = priceBasket.calculate(args, products);

        System.out.println(output);
    }

    public String calculate(String args[], Products products) throws PriceBasketException {
        StringBuilder output = new StringBuilder();

        //create a Map of products with Key as product name.
        Map<String, Product> productByNameMap =
                products.getProduct().stream().collect(Collectors.toMap(p->p.getName().toLowerCase(),
                        Function.identity()));


        //update the Cart with product quantities based on the items in the argument provided.
        Set<Product> cart =
                Arrays.stream(args)
                        .map(arg -> productByNameMap.get(arg.toLowerCase()))
                        .filter(product->product!=null)
                        .map(this::increaseQuantityAndApplyDiscountPrice)
                        .collect(Collectors.toSet());


        //update the discount price for multi-buy items(quantities are updated in the above setp) and calculates the sum.
        double subTotal =
                cart.stream()
                        .map(product->this.applyDiscountForMultiBuy(product, cart))
                        .mapToDouble(t->t.getPrice()*t.getNumberOfUnits())
                        .sum();
        subTotal = getRoundedUptoTwoDecimal(subTotal);
        output.append("SubTotal: "+ subTotal).append(System.lineSeparator());


        //get discount total.
        double discountTotal =
                cart.stream()
                        .mapToDouble(t->t.getDiscountPrice())
                        .sum();

        discountTotal = getRoundedUptoTwoDecimal(discountTotal);
        double total = getRoundedUptoTwoDecimal(subTotal - discountTotal);


        //build the output of discount applied in the cart.
        String outputOffer =
                cart.stream()
                        .filter(p->p.getDiscountPrice() != 0.00)
                        .map(p-> {
                            if(p.getDiscount() != null) {
                                StringBuilder builder = new StringBuilder();
                                return builder.append(p.getName())
                                        .append(" ")
                                        .append(p.getDiscount().getPercentage())
                                        .append("% off: -")
                                        .append(Math.round(p.getDiscountPrice()*100))
                                        .append("p")
                                        .append(System.lineSeparator());
                            } else {
                                return "";
                            }
                        })
                        .collect(Collectors.joining());

        if("".equals(outputOffer)) {
            outputOffer = "(no offers available)";
        }
        output.append(outputOffer.trim()).append(System.lineSeparator());
        output.append("Total: "+ total);

        return output.toString();
    }

    /**
     * Increase the quantity of product and update discount price for all items except multibuy products.
     * @param product
     * @return
     */
    public Product increaseQuantityAndApplyDiscountPrice(Product product) {

        //increase the quantity
        product.increaseQuantity();

        //update discount price for all items except multibuy products.
        if (product.getDiscount() != null) {
            if (product.getDiscount().getMultibuy() == null) {
                product.updateDiscountPrice();
            }
        }

        return product;
    }

    /**
     * updates the discount price for multi-buy items.
     * @param product
     * @param cart
     * @return
     */
    public Product applyDiscountForMultiBuy(Product product, Set<Product> cart) {
        if(product.getDiscount() !=null && product.getDiscount().getMultibuy()!=null) {
            String productName = product.getDiscount().getMultibuy().getProductName();

            //find quantities of multibuy product before apply discount
            int quantitiesOfMultiBuy = getQuantitiesOfMultiBuy(cart, productName);
            if(quantitiesOfMultiBuy >= product.getDiscount().getMultibuy().getQuantity().intValue()) {
                int discountApplyTimes = quantitiesOfMultiBuy/product.getDiscount().getMultibuy().getQuantity().intValue();

                if(discountApplyTimes > product.getNumberOfUnits()) {
                    discountApplyTimes = product.getNumberOfUnits();
                }
                if (product.getDiscountPrice() == 0.00) {
                    for (int i = 1; i <= discountApplyTimes; i++) {
                        product.updateDiscountPrice();
                    }
                }
            }
        }
        return product;
    }

    public int getQuantitiesOfMultiBuy(Set<Product> cart, String productName) {
        List<Integer> numberOfUnitsList = cart.stream().filter(c -> c.getName().equals(productName))
                .map(p -> p.getNumberOfUnits())
                .collect(Collectors.toList());
        if(numberOfUnitsList.size() > 0) {
            return numberOfUnitsList.get(0);
        }
        return 0;
    }

    public double getRoundedUptoTwoDecimal(double input) {
        BigDecimal inputBigDecimal = new BigDecimal(input);
        BigDecimal inputRounded = inputBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return inputRounded.doubleValue();
    }

    public Products getAvailableProducts() throws IOException, JAXBException {
        String fileDB = readProductsFromFile("products.xml");
        return (Products)XmlProcessor.unmarshal(Products.class, fileDB);
    }

    private String readProductsFromFile(String fileName) throws IOException {
        Path filePath = Paths.get(fileName);
        try(Stream<String> streamStr = Files.lines(filePath)){
            return streamStr.collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
