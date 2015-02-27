package com.gatech.objectsanddesign.shoppingwithfriends;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;

import com.firebase.client.Firebase;

/**
 * Activity display for the friend details
 */

public class FriendDetails extends NavigationActivity {

    private FirebaseInterfacer interfacer;
    private Friend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_friend_details);
        super.onCreateDrawer();

        Bundle data = getIntent().getExtras();
        if( data.containsKey("EXTRA_Friend")) {
            if (savedInstanceState == null) {
                PlaceholderFragment placeholder = new PlaceholderFragment();
                placeholder.setArguments(data);
                getSupportFragmentManager().beginTransaction().add(
                        R.id.container, placeholder).commit();
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

    /**
     * implement the remove friend functionality in addition to the default menu
     * @param item
     * @return
     */
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
            interfacer.removeFriend(friend, this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view of a friend's information
     */
    public static class PlaceholderFragment extends Fragment {

        private Friend mFriend;
        TextView mNameText;
        TextView mEmailText;
        TextView mRatingText;
        TextView mReportsGiven;

        public PlaceholderFragment() {
        }

        /**
         * Create the view for the fragment
         * @param inflater
         * @param container
         * @param savedInstanceState
         * @return
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_friend_details, container, false);
            mFriend = getActivity().getIntent().getExtras().getParcelable("EXTRA_Friend");
            mNameText = (TextView) rootView.findViewById(R.id.details_friend_name);
            mEmailText = (TextView) rootView.findViewById(R.id.details_friend_email);
            mRatingText = (TextView) rootView.findViewById(R.id.details_friend_rating);
            mReportsGiven = (TextView) rootView.findViewById(R.id.details_friend_reports);
            mNameText.setText(mFriend.getFirst() + " " + mFriend.getLast());
            mEmailText.setText(mFriend.getEmail());
            mRatingText.setText(String.valueOf("Rating: " + mFriend.getRating()));
            mReportsGiven.setText("Reports not yet implemented.");
            //Log.d("FRIEND", mFriend.toString());
            return rootView;
        }

    }
}
