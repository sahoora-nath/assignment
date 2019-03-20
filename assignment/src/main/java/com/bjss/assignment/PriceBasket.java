package com.bjss.assignment;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;


/**
 * Main class for PriceBasket program
 */
@ComponentScan(basePackages = "com.bjss.assignment")
public class PriceBasket {
    private static final Logger logger = Logger.getLogger(PriceBasket.class);

    @Autowired
    private PriceBasketRunner runner;

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            printUsage();
            System.exit(0);
        }

        ApplicationContext context
                = new AnnotationConfigApplicationContext(PriceBasket.class);

        PriceBasket priceBasket = context.getBean(PriceBasket.class);
        try {
            priceBasket.start(args);
        } catch (PriceBasketException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void printUsage() {
        logger.error("Usage: java PriceBasket [items]\n"
                + "i.g. java PriceBasket Apple Milk Bread");

    }

    private void start(String[] args) throws PriceBasketException {
        runner.run(args, System.out);
    }
}
