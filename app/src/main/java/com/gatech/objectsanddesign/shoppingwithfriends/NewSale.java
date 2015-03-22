package com.gatech.objectsanddesign.shoppingwithfriends;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Activity for user to report new sales
 */

public class NewSale extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sale);
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
        getMenuInflater().inflate(R.menu.menu_new_sale, menu);
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
    public static class PlaceholderFragment extends Fragment
            implements GooglePlayServicesClient.ConnectionCallbacks,
            GooglePlayServicesClient.OnConnectionFailedListener,
            GoogleApiClient.OnConnectionFailedListener,
            GoogleApiClient.ConnectionCallbacks {

        private Button mAddSale;
        private EditText mName;
        private EditText mPrice;
        private Location mLastLocation;
        private GoogleApiClient mGoogleApiClient;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_new_sale, container, false);

            mAddSale = (Button) rootView.findViewById(R.id.add_sale);
            mName = (EditText) rootView.findViewById(R.id.add_sale_name);
            mPrice = (EditText) rootView.findViewById(R.id.add_sale_price);

            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mGoogleApiClient.connect();

            mAddSale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateInput()) {
                        FirebaseInterfacer.interfacer.addSale(new Sale(
                                mName.getText().toString(),
                                new Double(mPrice.getText().toString()),
                                mLastLocation
                        ));
                        mName.getText().clear();
                        mPrice.getText().clear();
                        Toast.makeText(getActivity(),
                                "Sale successfully added",
                                Toast.LENGTH_SHORT).show();
                        mGoogleApiClient.disconnect();
                    }
                }
            });
            return rootView;
        }

        private boolean validateInput() {
            try {
                Double.parseDouble(mPrice.getText().toString());
                return true;
            } catch (NumberFormatException ex) {
                mPrice.setError("Number not a valid price.");
                return false;
            }
        }

        @Override
        public void onConnected(Bundle bundle) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }

        @Override
        public void onDisconnected() {

        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }
    }

}
