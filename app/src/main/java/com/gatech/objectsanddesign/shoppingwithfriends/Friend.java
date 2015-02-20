package com.gatech.objectsanddesign.shoppingwithfriends;

public class Friend extends ConcreteUser{
    private long rating;

    Friend(String first, String last, String uid, long rating) {
        super(first, last, uid);
        this.rating = rating;
    }

    @Override
    public String toString(){
        return super.toString() + " " + rating;
    }
}
