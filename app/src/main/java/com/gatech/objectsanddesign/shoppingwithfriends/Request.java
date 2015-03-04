package com.gatech.objectsanddesign.shoppingwithfriends;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Class to store requests made by users
 */
public class Request {
    private String name;
    private double price;

    /**
     * Get the name of the requested item
     * @return the name of the requested item
     */
    public String getName() {
        return name;
    }

    /**
     * Get the price of the requested item
     * @return the item's price
     */
    public double getPrice() {
        return price;
    }

    public Request(String name, double price) {
        this.name = name;
        this.price = price;
    }

    /**
     * Get a string representation of the request in the form name: price
     * @return string representation of the request
     */
    @Override
    public String toString(){
        DecimalFormat fmt = new DecimalFormat("$ #######.00");
        fmt.setCurrency(Currency.getInstance(Locale.US));
        return name + ": " + fmt.format(price);
    }
}
