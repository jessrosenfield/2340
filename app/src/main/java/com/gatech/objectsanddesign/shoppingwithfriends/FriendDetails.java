package com.gatech.objectsanddesign.shoppingwithfriends;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.firebase.client.Firebase;


public class FriendDetails extends NavigationActivity {

    FirebaseInterfacer interfacer;
    public Friend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_friend_details);
        //super.onCreateDrawer();
        Firebase.setAndroidContext(this);
        Bundle data = getIntent().getExtras();
        if( data.containsKey("EXTRA_Friend")) {
            if (savedInstanceState == null) {
                PlaceholderFragment placeholder = new PlaceholderFragment();
                placeholder.setArguments(data);
                getSupportFragmentManager().beginTransaction().add(
                        android.R.id.content, placeholder).commit();
            }
            interfacer = new FirebaseInterfacer();
            friend = data.getParcelable("EXTRA_Friend");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_details, menu);
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
        if (id == R.id.remove_friend) {
            interfacer.removeFriend(friend.getUid());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private Friend mFriend;

        public PlaceholderFragment() {
            //Bundle data = getArguments();
            //mFriend = data.getParcelable("EXTRA_Friend");
            mFriend = getActivity().getIntent().getExtras().getParcelable("EXTRA_Friend");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_friend_details, container, false);
            return rootView;
        }

    }
}
