package com.gatech.objectsanddesign.shoppingwithfriends;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
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

    public static FirebaseInterfacer interfacer = new FirebaseInterfacer();

    public static final String BASE = "https://2340.firebaseio.com";
    public static final String USERS = "users";
    public static final String FRIENDS = "friends";
    public static final String FIRSTNAME = "firstName";
    public static final String LASTNAME = "lastName";
    public static final String EMAIL = "email";
    public static final String REQUEST_NAME = "name";
    public static final String REQUEST_PRICE = "price";
    public static final String REQUEST_MATCHED = "matched";
    public static final String REQUESTS = "requests";
    public static final String SALE_NAME = "name";
    public static final String SALE_PRICE = "price";
    public static final String SALES = "sales";

    private Firebase ref;
    private String curID;

    public FirebaseInterfacer() {
        ref = new Firebase(BASE);
        ref = ref.child(USERS);
        curID = ref.getAuth().getUid();

        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if(authData != null){
                    curID = ref.getAuth().getUid();
                }
            }
        });
    }

    /**
     * Adds a friend to the user entry of a database
     * @param friend user to make a friend
     * @param context
     */
    public void addFriend(final User friend, final Context context) {
        Query query = ref.child(curID).child(FRIENDS).orderByKey()
                .equalTo(friend.getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Map<String, Object> f = new HashMap<>(), u = new HashMap();
                    f.put(friend.getUid(), 0);
                    u.put(curID, 0);
                    ref.child(ref.getAuth().getUid()).child(FRIENDS).updateChildren(f);
                    ref.child(friend.getUid()).child(FRIENDS).updateChildren(u);

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
        ref.child(curID).child(FRIENDS).child(friend.getUid()).removeValue();
        ref.child(friend.getUid()).child(FRIENDS).child(curID).removeValue();

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
        Query query = ref.child(curID).child(FRIENDS);
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
                                                friend.get(FIRSTNAME),
                                                friend.get(LASTNAME),
                                                entry.getKey(),
                                                friend.get(EMAIL),
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
        findFriendsBy(first, FIRSTNAME, friends);
    }

    /**
     * matches friends ordered by last name
     * @param last last name to match
     * @param friends map of friends
     */

    public void matchLastName(String last, ArrayAdapter<User> friends) {
        findFriendsBy(last, LASTNAME, friends);
    }

    /**
     * matches friends ordered by email
     * @param email email to match
     * @param friends map of friends
     */
    public void matchEmail(String email, ArrayAdapter<User> friends) {
        findFriendsBy(email, EMAIL, friends);
    }

    /**
     * Add user's request to database for persistence
     * @param request the request to be added
     */

    public void addRequest(Request request) {
        Map<String, Object> item = new HashMap<>();
        item.put(REQUEST_NAME, request.getName());
        item.put(REQUEST_PRICE, request.getPrice());
        item.put(REQUEST_MATCHED, request.isMatched());
        ref.child(curID).child(REQUESTS).push().setValue(item);
    }

    /**
     * Add user's sale to database for persistence
     * @param sale the sale to be added
     */

    public void addSale(Sale sale) {
        Map<String, Object> item = new HashMap<>();
        item.put(SALE_NAME, sale.getName());
        item.put(SALE_PRICE, sale.getPrice());
        ref.child(curID).child(SALES).push().setValue(item);
    }

    /**
     * Populate a list with the user's current requests
     * @param adapter object to contain the user's requests
     */
    public void getRequests(final ArrayAdapter<Request> adapter) {
        Query query = ref.child(curID).child(REQUESTS);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //dataSnapshot.getValue() is a map from strings (random id) to hashmaps (that represents requests)
                adapter.clear();
                Map<String, Map<String, Object>> requests = (HashMap) dataSnapshot.getValue();
                if (requests != null) {
                    for (Map request : requests.values()) {
                        adapter.add(
                                new Request(
                                        (String) request.get(REQUEST_NAME),
                                        (double) request.get(REQUEST_PRICE)
                                )
                        );
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /**
     * Populate a list with the user's current sales
     * @param adapter object to contain the user's sales
     */
    public void getSales(final ArrayAdapter<Sale> adapter) {
        Query query = ref.child(curID).child(SALES);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //dataSnapshot.getValue() is a map from strings (random id) to hashmaps (that represents sales)
                adapter.clear();
                Map<String, Map<String, Object>> sales = (HashMap) dataSnapshot.getValue();
                if (sales != null) {
                    for (Map sale : sales.values()) {
                        adapter.add(
                                new Sale(
                                        (String) sale.get(SALE_NAME),
                                        (double) sale.get(SALE_PRICE)
                                )
                        );
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
//
//    private void findMatches(Sale sale) {
//        Object dataSnapshot;
//        Query query = ref.child(USERS);
//        Map<String, Map<String, Object>> requests = (HashMap) dataSnapshot.getValue();
//        if (requests != null) {
//            for (Map.Entry entry : requests.entrySet()) {
//                Map request = (Map) entry.getValue();
//                if (request.get(REQUEST_NAME).equals(sale.getName())) {
//                    if ((Double) request.get(REQUEST_PRICE) >= sale.getPrice()) {
//                        ref.child(USERS).child(curID).child(REQUESTS).child((String) entry.getKey()).child(REQUEST_MATCHED).set(true);
//                    }
//                }
//            }
//        }
//    }


    /**
     * set the name of a user
     * @param view the view that needs to change corresponding to a change in the name
     */
    public void setName(final TextView view){
        ref.child(curID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                String name = map.get(FIRSTNAME) + " " + map.get(LASTNAME);
                view.setText(view.getText() + ", " + name);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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
                            adapter.add(new ConcreteUser((String) friend.get(FIRSTNAME),
                                    (String) friend.get(LASTNAME),
                                    friendID,
                                    (String) friend.get(EMAIL)
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
