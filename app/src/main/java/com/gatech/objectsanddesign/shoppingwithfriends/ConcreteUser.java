package com.gatech.objectsanddesign.shoppingwithfriends;

public class ConcreteUser implements  User{
    private String first;
    private String last;
    private String uid;
    private String email;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    ConcreteUser(String first, String last, String uid, String email){
        this.first = first;
        this.last = last;
        this.uid = uid;
        this.email = email;
    }

    @Override
    public String toString(){
        return first + " " + last;
    }

    public String getUid(){
        return uid;
    }
}
