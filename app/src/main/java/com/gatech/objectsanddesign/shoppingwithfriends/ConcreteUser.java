package com.gatech.objectsanddesign.shoppingwithfriends;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Provides a concrete implementation for the User class to hold data about a user during runtime
 */

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

    /**
     * Initialize the user from a parcel
     * @param in initialization
     */
    protected ConcreteUser(Parcel in) {
        first = in.readString();
        last = in.readString();
        uid = in.readString();
        email = in.readString();
    }

    /**
     * Get the first name of a user
     * @return first name
     */
    @Override
    public String getFirst() {
        return first;
    }

    /**
     * Set the first name of a user
     * @param first first name
     */
    public void setFirst(String first) {
        this.first = first;
    }

    /**
     * Get the last name of a user
     * @return last name
     */
    @Override
    public String getLast() {
        return last;
    }

    /**
     * Set the last name of a user
     * @param last last name
     */
    public void setLast(String last) {
        this.last = last;
    }

    /**
     * set the uid for a user
     * @param uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * get the user's email
     * @return email
     */
    @Override
    public String getEmail() {
        return email;
    }

    /**
     * set the user's email
     * @param email
     */

    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * Get the string representation of a user
     * @return string representation
     */
    @Override
    public String toString(){
        return first + " " + last;
    }

    /**
     * Get the uid
     * @return uid
     */
    public String getUid(){
        return uid;
    }

    /**
     * method to deal with parcelling
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes a parcel representation of a user
     * @param dest destination parcel
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(first);
        dest.writeString(last);
        dest.writeString(uid);
        dest.writeString(email);
    }

    /**
     * Parcel the user
     */
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