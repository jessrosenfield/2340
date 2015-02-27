package com.gatech.objectsanddesign.shoppingwithfriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

/**
 * Main application screen that displays after login
 */

public class ApplicationScreen extends NavigationActivity {

    /**
     * Initialize the database reference to store for the user
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_application_screen);
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
        getMenuInflater().inflate(R.menu.menu_application_screen, menu);
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

        TextView mWelcomeText;
        ListView mRequestsList;
        ArrayAdapter<Request> mRequestsAdapter;
        FirebaseInterfacer ref;

        public PlaceholderFragment() {
        }

        /**
         * Populate the fragment with the appropriate text fields and buttons
         * @param inflater
         * @param container
         * @param savedInstanceState
         * @return
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_application_screen, container, false);
            if((new Firebase("https://2340.firebaseio.com").getAuth() == null)){
                Intent i = new Intent(getActivity(), MainActivity.class);
                getActivity().finish();
                startActivity(i);
            } else {
                ref = new FirebaseInterfacer();
                mWelcomeText = (TextView) rootView.findViewById(R.id.welcome);
                mRequestsList = (ListView) rootView.findViewById(R.id.requests_list);
                mRequestsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
                mRequestsList.setAdapter(mRequestsAdapter);
                ref.setName(mWelcomeText);
                ref.getRequests(mRequestsAdapter);
            }
            return rootView;
        }
    }
}
