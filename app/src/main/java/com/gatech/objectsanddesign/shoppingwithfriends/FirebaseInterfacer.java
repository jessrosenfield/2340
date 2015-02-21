package com.gatech.objectsanddesign.shoppingwithfriends;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseInterfacer {
    Firebase ref;
    String curID;

    public FirebaseInterfacer() {
        ref = new Firebase("https://2340.firebaseio.com/users");
        curID = ref.getAuth().getUid();
    }

    public Friend getFriend(String friendID) {
        return new Friend(getUser(friendID), getRating(friendID));
    }

    public ConcreteUser getUser(String uid) {
        final ConcreteUser[] user = new ConcreteUser[1];
        Query query = ref.child(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> hashUser = (Map<String, Object>) dataSnapshot.getValue();
                if(hashUser != null){
                    user[0] = new ConcreteUser(
                            (String) hashUser.get("firstName"),
                            (String) hashUser.get("lastName"),
                            curID,
                            (String) hashUser.get("email")
                    );
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return user[0];
    }

    public boolean addFriend(final String friendID) {
        final boolean[] status = {false};
        Query query = ref.child(curID).child("friends").orderByValue()
                .equalTo(friendID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Map<String, Object> f = new HashMap<>(), u = new HashMap();
                    f.put(friendID, 0);
                    u.put(curID, 0);
                    ref.child(ref.getAuth().getUid()).child("friends").updateChildren(f);
                    ref.child(friendID).child("friends").updateChildren(u);
                    status[0] = true;
                } else {
                    status[0] = false;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return status[0];
    }

    public void removeFriend(String friendID) {

    }

    public long getRating(String friendID) {
        Query query = ref.child(curID).child("friends").child(friendID);
        final long[] ret = new long[1];
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ret[0] = (long) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return ret[0];
    }

    public List<Friend> getFriends() {
        final List<Friend> friends = new ArrayList<>();
        Query query = ref.child(curID).child("friends");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> friendsMap = (Map<String, Object>) dataSnapshot.getValue();
                if (friendsMap != null) {
                    for (Map.Entry<String, Object> entry : friendsMap.entrySet()) {
                        friends.add(getFriend(entry.getKey()));
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return friends;
    }

    public List<User> matchFirstName(String first) {
        return findFriendsBy(first, "firstName");
    }

    public List<User> matchLastName(String last) {
        return findFriendsBy(last, "lastName");
    }

    public List<User> matchEmail(String email) {
        return findFriendsBy(email, "email");
    }

    private List<User> findFriendsBy(String value, String attribute) {
        final List<User> friends = new ArrayList<>();
        Query query = ref.orderByChild(attribute)
                .equalTo(value);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, HashMap> user = (HashMap) dataSnapshot.getValue();
//                if (user != null) {
                    for (Map.Entry pair : user.entrySet()) {
                        String friendID = (String) pair.getKey();
                        HashMap<String, String> friend = (HashMap) pair.getValue();

                        if (!friendID.equals(curID)) {
                            friends.add(new ConcreteUser(friend.get("firstName"),
                                    friend.get("lastName"),
                                    friend.get("email"),
                                    friendID));
                        }
//                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return friends;
    }
}
