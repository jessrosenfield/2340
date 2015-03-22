package com.gatech.objectsanddesign.shoppingwithfriends;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity display to allow the user to search for friends
 */

public class FriendSearch extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_friend_search);
        super.onCreateDrawer();
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
        EditText mLast;
        EditText mFirst;
        EditText mEmail;
        Button mFind;
        ListView mFriendsList;
        ArrayAdapter<User> mFriendsAdaptor;
        List<User> friends;

        public PlaceholderFragment() {
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
                    ConcreteUser friend = (ConcreteUser) parent.getItemAtPosition(position);
                    FirebaseInterfacer.interfacer.addFriend(friend, getActivity());
                    mFirst.getText().clear();
                    mLast.getText().clear();
                    mEmail.getText().clear();
                    mFriendsAdaptor.clear();
                }
            });

            mFriendsAdaptor = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1, friends);
            mFriendsList.setAdapter(mFriendsAdaptor);
            return rootView;
        }

        public void updateList(){
            FirebaseInterfacer.interfacer.matchFirstName(mFirst.getText().toString(), mFriendsAdaptor);
            FirebaseInterfacer.interfacer.matchLastName(mLast.getText().toString(), mFriendsAdaptor);
            FirebaseInterfacer.interfacer.matchEmail(mEmail.getText().toString(), mFriendsAdaptor);
        }

        public static void hide_keyboard_from(Context context, View view) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
