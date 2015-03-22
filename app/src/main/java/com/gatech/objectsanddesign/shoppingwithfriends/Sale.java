package com.gatech.objectsanddesign.shoppingwithfriends;

import android.location.Location;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Class to store sales reported by users
 */
public class Sale {
    private String name;
    private double price;
    private Location location;

    public Sale(String name, double price, Location location) {
        this.name = name;
        this.price = price;
        this.location = location;
    }

    /**
     * Get the name of the reported item
     * @return the name of the reported item
     */
    public String getName() {
        return name;
    }

    /**
     * Get the price of the reported item
     *
     * @return the item's price
     */
    public double getPrice() {
        return price;
    }

    public Location getLocation() {
        return location;
    }

    public Map toMap() {
        Map<String, Object> item = new HashMap<>();
        item.put(FirebaseInterfacer.SALE_NAME, getName());
        item.put(FirebaseInterfacer.SALE_PRICE, getPrice());
        return item;
    }

    /**
     * Get a string representation of the sale in the form name: price
     *
     * @return string representation of the sale
     */
    @Override
    public String toString() {
        DecimalFormat fmt = new DecimalFormat("$ #######.00");
        fmt.setCurrency(Currency.getInstance(Locale.US));
        return name + ": " + fmt.format(price);
    }
}
