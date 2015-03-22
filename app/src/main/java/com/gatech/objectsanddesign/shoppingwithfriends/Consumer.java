package com.gatech.objectsanddesign.shoppingwithfriends;

public interface Consumer<T> {
    void preconsume();
    void consume(T t);
}
