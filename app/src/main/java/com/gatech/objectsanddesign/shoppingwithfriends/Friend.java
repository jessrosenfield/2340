package com.gatech.objectsanddesign.shoppingwithfriends;

public class Friend extends ConcreteUser{
    private long rating;

    Friend(String first, String last, String uid, String email, long rating) {
        super(first, last, email, uid);
        this.rating = rating;
    }

    Friend(ConcreteUser user, long rating){
        super(user.getFirst(), user.getLast(), user.getEmail(), user.getUid());
        this.rating = rating;
    }

    @Override
    public String toString(){
        return super.toString() + " " + rating;
    }
}
