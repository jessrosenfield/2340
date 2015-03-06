package com.gatech.objectsanddesign.shoppingwithfriends;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Class to store sales reported by users
 */
public class Sale {
    private String name;
    private double price;

    /**
     * Get the name of the reported item
     * @return the name of the reported item
     */
    public String getName() {
        return name;
    }

    /**
     * Get the price of the reported item
     * @return the item's price
     */
    public double getPrice() {
        return price;
    }

    public Sale(String name, double price) {
        this.name = name;
        this.price = price;
    }

    /**
     * Get a string representation of the sale in the form name: price
     * @return string representation of the sale
     */
    @Override
    public String toString(){
        DecimalFormat fmt = new DecimalFormat("$ #######.00");
        fmt.setCurrency(Currency.getInstance(Locale.US));
        return name + ": " + fmt.format(price);
    }
}
