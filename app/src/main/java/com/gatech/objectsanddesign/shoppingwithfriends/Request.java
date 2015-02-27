package com.gatech.objectsanddesign.shoppingwithfriends;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

public class Request {
    private String name;
    private double price;

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Request(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString(){
        DecimalFormat fmt = new DecimalFormat();
        fmt.setCurrency(Currency.getInstance(Locale.US));
        return name + ": " + fmt.format(price);
    }
}
