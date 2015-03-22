package com.gatech.objectsanddesign.shoppingwithfriends;

import android.content.Context;
import android.location.Location;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides an interface to query the database
 */

public class FirebaseInterfacer {

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
    public static final String FRIENDS_SALES = "friendsales";
    public static FirebaseInterfacer interfacer = new FirebaseInterfacer();
    private Firebase ref;
    private String curID;

    public FirebaseInterfacer() {
        ref = new Firebase(BASE);
        curID = ref.getAuth().getUid();

        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    curID = ref.getAuth().getUid();
                }
            }
        });
    }

    /**
     * Adds a friend to the user entry of a database
     *
     * @param friend  user to make a friend
     * @param context the activity in which to display the toast
     */
    public void addFriend(final User friend, final Context context) {
        Query query = ref.child(USERS).child(curID).child(FRIENDS).orderByKey()
                .equalTo(friend.getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Map<String, Object> f = new HashMap<>(), u = new HashMap<>();
                    f.put(friend.getUid(), 0);
                    u.put(curID, 0);
                    ref.child(USERS).child(ref.getAuth().getUid()).child(FRIENDS).updateChildren(f);
                    ref.child(USERS).child(friend.getUid()).child(FRIENDS).updateChildren(u);

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
     *
     * @param friend  friend to remove
     * @param context activity to display toast in
     */
    public void removeFriend(final User friend, final Context context) {
        ref.child(USERS).child(curID).child(FRIENDS).child(friend.getUid()).removeValue();
        ref.child(USERS).child(friend.getUid()).child(FRIENDS).child(curID).removeValue();

        Toast.makeText(context,
                "You are no longer friends with " + friend.toString(),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Populates a list with the user's friends
     *
     * @param adapter the list adapter to populate with friends
     */

    public void getFriends(final ArrayAdapter<Friend> adapter) {
        iterateOverFriends(new Consumer<Friend>() {
            @Override
            public void preconsume() {
                adapter.clear();
            }

            @Override
            public void consume(Friend friend) {
                adapter.add(friend);
            }
        });
    }

    private void iterateOverFriends(final Consumer<Friend> consumer) {
        Query query = ref.child(USERS).child(curID).child(FRIENDS);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                consumer.preconsume();
                Map<String, Object> friendsMap = (Map<String, Object>) dataSnapshot.getValue();
                if (friendsMap != null) {
                    for (final Map.Entry<String, Object> entry : friendsMap.entrySet()) {

                        Query findFriend = ref.child(USERS).child(entry.getKey());
                        findFriend.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map<String, String> friend = (Map<String, String>) dataSnapshot.getValue();
                                consumer.consume(
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
     *
     * @param first   first name to match
     * @param friends map of friends
     */
    public void matchFirstName(String first, ArrayAdapter<User> friends) {
        findFriendsBy(first, FIRSTNAME, friends);
    }

    /**
     * matches friends ordered by last name
     *
     * @param last    last name to match
     * @param friends map of friends
     */

    public void matchLastName(String last, ArrayAdapter<User> friends) {
        findFriendsBy(last, LASTNAME, friends);
    }

    /**
     * matches friends ordered by email
     *
     * @param email   email to match
     * @param friends map of friends
     */
    public void matchEmail(String email, ArrayAdapter<User> friends) {
        findFriendsBy(email, EMAIL, friends);
    }

    /**
     * Add user's request to database for persistence
     *
     * @param request the request to be added
     */

    public void addRequest(Request request) {
        ref.child(USERS).child(curID).child(REQUESTS).push().setValue(request.toMap());
    }

    /**
     * Add user's sale to database for persistence
     *
     * @param sale the sale to be added
     */

    public void addSale(final Sale sale) {
        final Firebase saleRef = ref.child(SALES).push();
        final Firebase userSaleRef = ref.child(USERS).child(curID).child(SALES);

        //Add to sales database
        saleRef.setValue(sale.toMap());

        //Add reference to user database, with value being the location
        new GeoFire(userSaleRef).setLocation(saleRef.getKey(), new GeoLocation(
                sale.getLocation().getLatitude(),
                sale.getLocation().getLongitude()
        ));

        //Add references to friends, with value being the location
        iterateOverFriends(new Consumer<Friend>() {
            @Override
            public void preconsume() {

            }

            @Override
            public void consume(Friend friend) {
                Firebase friendRef = ref.child(USERS).child(friend.getUid()).child(FRIENDS_SALES);
                new GeoFire(friendRef).setLocation(saleRef.getKey(), new GeoLocation(
                        sale.getLocation().getLatitude(),
                        sale.getLocation().getLongitude()
                ));
            }
        });

        findMatches(sale);
    }

    /**
     * Populate a list with the user's current requests
     *
     * @param adapter object to contain the user's requests
     */
    public void getRequests(final ArrayAdapter<Request> adapter) {
        iterateOverRequests(curID, new Consumer<Request>() {
            @Override
            public void preconsume() {
                adapter.clear();
            }

            @Override
            public void consume(Request request) {
                adapter.add(request);
            }
        });
    }

    private void iterateOverRequests(String id, final Consumer<Request> consumer) {
        Query query = ref.child(USERS).child(id).child(REQUESTS);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //dataSnapshot.getValue() is a map from strings (random id) to hashmaps (that represents requests)
                consumer.preconsume();
                Map<String, Map<String, Object>> requests = (HashMap) dataSnapshot.getValue();
                if (requests != null) {
                    for (Map.Entry<String, Map<String, Object>> entry : requests.entrySet()) {
                        Map request = entry.getValue();
                        consumer.consume(
                                new Request(
                                        (String) request.get(REQUEST_NAME),
                                        (double) request.get(REQUEST_PRICE),
                                        (boolean) request.get(REQUEST_MATCHED),
                                        entry.getKey()
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

    private void findMatches(final Sale sale) {
        iterateOverFriends(new Consumer<Friend>() {
            @Override
            public void preconsume() {

            }

            @Override
            public void consume(final Friend friend) {
                iterateOverRequests(friend.getUid(), new Consumer<Request>() {
                    @Override
                    public void preconsume() {

                    }

                    @Override
                    public void consume(Request request) {
                        if (isMatched(sale, request)) {
                            request.setMatched(true);
                            ref.child(USERS).child(friend.getUid()).child(REQUESTS).child(request.getId()).updateChildren(
                                    request.toMap()
                            );
                        }
                    }
                });
            }
        });
    }

    private boolean isMatched(Sale sale, Request request) {
        return (sale.getName().toLowerCase().trim().equals(
                request.getName().toLowerCase().trim())
                && sale.getPrice() <= request.getPrice());
    }


    /**
     * set the name of a user
     *
     * @param view the view that needs to change corresponding to a change in the name
     */
    public void setName(final TextView view) {
        ref.child(USERS).child(curID).addListenerForSingleValueEvent(new ValueEventListener() {
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
     *
     * @param value     value to search for
     * @param attribute value to order by
     * @param adapter   Map of friends
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

    public void addNearbySalesMarkers(final GoogleMap map, final Location loc) {
        GeoFire geoFire = new GeoFire(ref.child(USERS).child(curID).child(FRIENDS_SALES));
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(loc.getLatitude(), loc.getLongitude()), NearbySales.RADIUS);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, final GeoLocation location) {
                Query nameQuery = ref.child(SALES).child(key).child(SALE_NAME);
                nameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(location.latitude, location.longitude))
                                .title((String) dataSnapshot.getValue()));
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(FirebaseError error) {

            }
        });
    }
}
