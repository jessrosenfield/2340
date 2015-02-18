package com.gatech.objectsanddesign.shoppingwithfriends;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FriendSearch extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_friend_search);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        Firebase ref;
        EditText mLast;
        EditText mFirst;
        EditText mEmail;
        Button mFind;
        ListView mFriendsList;
        ArrayAdapter<User> mFriendsAdaptor;
        List<User> friends;

        public PlaceholderFragment() {
            ref = new Firebase("http://2340.firebaseio.com");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_friend_search, container, false);
            mLast = (EditText) rootView.findViewById(R.id.last_name_input_friend_search);
            mFirst = (EditText) rootView.findViewById(R.id.first_name_input_friend_search);
            mEmail = (EditText) rootView.findViewById(R.id.email_input_friend_search);
            mFind = (Button) rootView.findViewById(R.id.find_friends);
            mFriendsList = (ListView) rootView.findViewById(R.id.friends_search_results);
            friends = new ArrayList<>();

            mFind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateList();
                    hide_keyboard_from(getActivity(), getView());
                }
            });

            mFriendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final ConcreteUser friend = (ConcreteUser) parent.getItemAtPosition(position);
                    String uid = ref.getAuth().getUid();

                    Query query = ref.child("users").child(uid).child("friends").orderByValue()
                            .equalTo(friend.getUid());

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() == null){
                                ref.child("users").child(ref.getAuth().getUid()).child("friends").push().setValue(friend.getUid());
                                ref.child("users").child(friend.getUid()).child("friends").push().setValue(ref.getAuth().getUid());
                                Toast.makeText(getActivity(),
                                        "You are now friends with " + friend.toString(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(),
                                        "You are already friends with " + friend.toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                            mFirst.getText().clear();
                            mLast.getText().clear();
                            mEmail.getText().clear();
                            mFriendsAdaptor.clear();
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            });

            mFriendsAdaptor = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1, friends);
            mFriendsList.setAdapter(mFriendsAdaptor);
            return rootView;
        }

        public void updateList(){
            friends = new ArrayList<>();
            AuthData auth = ref.getAuth();
            if(auth != null){
                Query query = ref.child("users")
                        .orderByChild("firstName")
                        .equalTo(mFirst.getText().toString());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, HashMap> user = (HashMap) dataSnapshot.getValue();

                        for (Map.Entry pair : user.entrySet()) {
                            String friendID = (String) pair.getKey();
                            HashMap<String, String> friend = (HashMap) pair.getValue();

                            if (friend.get("lastName").equals(mLast.getText().toString())) {
                                friends.add(new ConcreteUser(mFirst.getText().toString(),
                                        mLast.getText().toString(),
                                        friendID));
                            }
                        }

                        mFriendsAdaptor.clear();
                        mFriendsAdaptor.addAll(friends);
                        mFriendsAdaptor.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        }

        public static void hide_keyboard_from(Context context, View view) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
