package com.gatech.objectsanddesign.shoppingwithfriends;

import android.os.Parcel;
import android.os.Parcelable;

public class Friend extends ConcreteUser{
    private long rating;

    Friend(String first, String last, String uid, String email, long rating) {
        super(first, last, uid, email);
        this.rating = rating;
    }

    Friend(ConcreteUser user, long rating){
        super(user.getFirst(), user.getLast(), user.getEmail(), user.getUid());
        this.rating = rating;
    }

    protected Friend(Parcel in) {
        super(in);
        rating = in.readLong();
    }

    public long getRating() {
        return rating;
    }

    @Override
    public String toString(){
        return super.toString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(rating);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Friend> CREATOR = new Parcelable.Creator<Friend>() {
        @Override
        public Friend createFromParcel(Parcel in) {
            return new Friend(in);
        }

        @Override
        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };

}
