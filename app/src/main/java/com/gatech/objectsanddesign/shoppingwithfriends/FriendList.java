package com.gatech.objectsanddesign.shoppingwithfriends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;


public class FriendList extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_friend_list);
        super.onCreateDrawer();
        Firebase.setAndroidContext(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_list, menu);
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

        FirebaseInterfacer ref;

        private ListView mFriendsList;
        private ArrayAdapter<Friend> friendsAdapter;

        public PlaceholderFragment() {
            ref = new FirebaseInterfacer();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_friend_list, container, false);
            mFriendsList = (ListView) rootView.findViewById(R.id.friend_list);
            friendsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
            ref.getFriends(friendsAdapter);
            mFriendsList.setAdapter(friendsAdapter);

            mFriendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final ConcreteUser friend = (ConcreteUser) parent.getItemAtPosition(position);
                    Intent i = new Intent(getActivity(), FriendDetails.class);
                    i.putExtra("EXTRA_Friend", friend);
                    startActivity(i);
                    getActivity().finish();
                }
            });

            return rootView;
        }
    }
}
