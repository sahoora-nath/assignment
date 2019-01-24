package com.bjss.assignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.bjss.assignment.bean.Discount;
import com.bjss.assignment.bean.Multibuy;
import com.bjss.assignment.bean.Product;
import com.bjss.assignment.bean.Products;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Unit test for Price Basket.
 */
public class PriceBasketTest
{

    private PriceBasket priceBasket;

    @Before
    public void init(){
        priceBasket = new PriceBasket();
    }

    /**
     * Rigorous Test.
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        //1. Test if the product list is available
        //2. Marshalling is successful
        //3. If the input arguments are valid
        //4. check if Basket summing is correct
        //5. check if discount is applied properly
        //6. check if Billing logic is fine
        //7. check if expected Excpetion scenario is covered
        assertTrue( true );
    }

    @Test
    public void getAvailableProductsTest() throws IOException, JAXBException {
        Products availableProducts = priceBasket.getAvailableProducts();
        assertNotNull(availableProducts);
        assertTrue(!availableProducts.getProduct().isEmpty());

        assertEquals(4, availableProducts.getProduct().size());
    }

    @Test
    public void increaseQuantityAndApplyDiscountPriceTest() {
        Discount appleDiscount = new Discount(new Date(), new Date(),10.00);
        Product apple = new Product(2,"Apple",1.00, appleDiscount);
        priceBasket.increaseQuantityAndApplyDiscountPrice(apple);

        assertEquals(1, apple.getNumberOfUnits());
        assertNotNull(apple.getDiscount());
        assertTrue(apple.getDiscount().getMultibuy()==null);
        assertEquals(new Double(10.00), apple.getDiscount().getPercentage());
    }

    @Test
    public void applyDiscountForMultiBuyTest(){

        Discount appleDiscount = new Discount(new Date(), new Date(),20.00);
        Multibuy multibuy = new Multibuy("Milk", 2);
        appleDiscount.setMultibuy(multibuy);
        Product apple = new Product(2,"Apple",1.00, appleDiscount);
        Product milk= new Product(1,"Milk",1.20, null);

        Map<String, Product> productByNameMap = new HashMap<>();
        productByNameMap.put("apple", apple);
        productByNameMap.put("milk", milk);

        String[] args = {"Apple", "Milk", "Milk"};
        Set<Product> cart =
                Arrays.stream(args)
                        .map(arg -> productByNameMap.get(arg.toLowerCase()))
                        .map(product->priceBasket.increaseQuantityAndApplyDiscountPrice(product))
                        .collect(Collectors.toSet());

        assertTrue(!cart.isEmpty());
        assertEquals(2, cart.size());

        int quanityOfMilkForDiscount = priceBasket.getQuantitiesOfMultiBuy(cart, "Milk");
        assertEquals(2, quanityOfMilkForDiscount);
    }

    @Test
    public void calculateTest() throws PriceBasketException {
        Discount appleDiscount = new Discount(new Date(), new Date(),20.00);
        Multibuy multibuy = new Multibuy("Milk", 2);
        appleDiscount.setMultibuy(multibuy);
        Product apple = new Product(2,"Apple",1.00, appleDiscount);
        Product milk= new Product(1,"Milk",1.20, null);

        Products products = new Products();
        products.getProduct().add(apple);
        products.getProduct().add(milk);

        String[] args = {"Apple", "Milk", "Milk"};

        String output = priceBasket.calculate(args, products);
        String[] opArr = output.split(System.lineSeparator());

        assertEquals("SubTotal: 3.4", opArr[0]);
        assertEquals("(no offers available)", opArr[1]);
        assertEquals("Total: 3.4", opArr[2]);
    }
}
