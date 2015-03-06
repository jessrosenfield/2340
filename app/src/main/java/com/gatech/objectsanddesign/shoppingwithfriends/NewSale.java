package com.gatech.objectsanddesign.shoppingwithfriends;

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
    public static class PlaceholderFragment extends Fragment {
        Button mAddSale;
        EditText mName;
        EditText mPrice;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_new_sale, container, false);
            mAddSale = (Button) rootView.findViewById(R.id.add_sale);
            mName = (EditText) rootView.findViewById(R.id.add_sale_name);
            mPrice = (EditText) rootView.findViewById(R.id.add_sale_price);

            mAddSale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(validateInput()){
                        FirebaseInterfacer.interfacer.addSale(new Sale(
                                mName.getText().toString(),
                                new Double(mPrice.getText().toString())
                        ));
                        mName.getText().clear();
                        mPrice.getText().clear();
                        Toast.makeText(getActivity(),
                                "Sale successfully added",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return rootView;
        }

        private boolean validateInput(){
            try{
                Double.parseDouble(mPrice.getText().toString());
                return true;
            } catch (NumberFormatException ex) {
                mPrice.setError("Number not a valid price.");
                return false;
            }
        }
    }

}
