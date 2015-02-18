package com.gatech.objectsanddesign.shoppingwithfriends;

public class ConcreteUser implements  User{
    private String first;
    private String last;
    private String uid;
//    private String email;
    //TODO: friends list as field for user??

    ConcreteUser(String first, String last, String uid){
        this.first = first;
        this.last = last;
        this.uid = uid;
    }

    @Override
    public String toString(){
        return first + " " + last;
    }

    public String getUid(){
        return uid;
    }
}
