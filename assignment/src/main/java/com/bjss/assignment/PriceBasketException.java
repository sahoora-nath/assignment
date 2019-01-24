package com.bjss.assignment;

public class PriceBasketException extends Exception {

    public PriceBasketException() {
        super();
    }

    public PriceBasketException(String message) {
        super(message);
    }

    public PriceBasketException(String message, Throwable cause) {
        super(message, cause);
    }
}
