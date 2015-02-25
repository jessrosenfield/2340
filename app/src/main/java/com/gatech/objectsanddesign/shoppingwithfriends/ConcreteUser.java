package com.gatech.objectsanddesign.shoppingwithfriends;

import android.os.Parcel;
import android.os.Parcelable;

public class ConcreteUser implements User, Parcelable {
    private String first;
    private String last;
    private String uid;
    private String email;

    ConcreteUser(String first, String last, String uid, String email){
        this.first = first;
        this.last = last;
        this.uid = uid;
        this.email = email;
    }

    protected ConcreteUser(Parcel in) {
        first = in.readString();
        last = in.readString();
        uid = in.readString();
        email = in.readString();
    }

    @Override
    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    @Override
    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString(){
        return first + " " + last;
    }

    public String getUid(){
        return uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(first);
        dest.writeString(last);
        dest.writeString(uid);
        dest.writeString(email);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ConcreteUser> CREATOR = new Parcelable.Creator<ConcreteUser>() {
        @Override
        public ConcreteUser createFromParcel(Parcel in) {
            return new ConcreteUser(in);
        }

        @Override
        public ConcreteUser[] newArray(int size) {
            return new ConcreteUser[size];
        }
    };
}