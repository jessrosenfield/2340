package com.gatech.objectsanddesign.shoppingwithfriends;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides an interface to query the database
 */

public class FirebaseInterfacer {
    Firebase ref;
    String curID;

    public FirebaseInterfacer() {
        ref = new Firebase("https://2340.firebaseio.com");
        ref = ref.child("users");
        curID = ref.getAuth().getUid();
    }

    /**
     * Adds a friend to the user entry of a database
     * @param friend user to make a friend
     * @param context
     */
    public void addFriend(final User friend, final Context context) {
        Query query = ref.child(curID).child("friends").orderByKey()
                .equalTo(friend.getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Map<String, Object> f = new HashMap<>(), u = new HashMap();
                    f.put(friend.getUid(), 0);
                    u.put(curID, 0);
                    ref.child(ref.getAuth().getUid()).child("friends").updateChildren(f);
                    ref.child(friend.getUid()).child("friends").updateChildren(u);

                    Toast.makeText(context,
                            "You are now friends with " + friend.toString(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context,
                            "You are already friends with " + friend.toString(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /**
     * Remove friend from the user
     * @param friend friend to remove
     * @param context
     */
    public void removeFriend(final User friend, final Context context) {
        ref.child(curID).child("friends").child(friend.getUid()).removeValue();
        ref.child(friend.getUid()).child("friends").child(curID).removeValue();

        Toast.makeText(context,
                "You are no longer friends with " + friend.toString(),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Populates a list with the user's friends
     * @param adapter
     */

    public void getFriends(final ArrayAdapter<Friend> adapter) {
        adapter.clear();
        Query query = ref.child(curID).child("friends");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> friendsMap = (Map<String, Object>) dataSnapshot.getValue();
                if (friendsMap != null) {
                    for (final Map.Entry<String, Object> entry : friendsMap.entrySet()) {

                        Query findFriend = ref.child(entry.getKey());
                        findFriend.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map<String, String> friend = (Map<String, String>) dataSnapshot.getValue();
                                adapter.add(
                                        new Friend(
                                                friend.get("firstName"),
                                                friend.get("lastName"),
                                                entry.getKey(),
                                                friend.get("email"),
                                                (long) entry.getValue()
                                        )
                                );
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /**
     * matches friends ordered by first name
     * @param first first name to match
     * @param friends map of friends
     */
    public void matchFirstName(String first, ArrayAdapter<User> friends) {
        findFriendsBy(first, "firstName", friends);
    }

    /**
     * matches friends ordered by last name
     * @param last last name to match
     * @param friends map of friends
     */

    public void matchLastName(String last, ArrayAdapter<User> friends) {
        findFriendsBy(last, "lastName", friends);
    }

    /**
     * matches friends ordered by email
     * @param email email to match
     * @param friends map of friends
     */
    public void matchEmail(String email, ArrayAdapter<User> friends) {
        findFriendsBy(email, "email", friends);
    }

    /**
     * Orders friends by desired attribute
     * @param value value to search for
     * @param attribute value to order by
     * @param adapter Map of friends
     */
    private void findFriendsBy(final String value, String attribute, final ArrayAdapter<User> adapter) {
        Query query = ref.orderByChild(attribute)
                .equalTo(value);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, HashMap> user = (HashMap) dataSnapshot.getValue();
                if (user != null) {
                    for (Map.Entry pair : user.entrySet()) {
                        String friendID = (String) pair.getKey();
                        HashMap<String, Object> friend = (HashMap) pair.getValue();

                        if (!friendID.equals(curID)) {
                            adapter.add(new ConcreteUser((String) friend.get("firstName"),
                                    (String) friend.get("lastName"),
                                    friendID,
                                    (String) friend.get("email")
                            ));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
