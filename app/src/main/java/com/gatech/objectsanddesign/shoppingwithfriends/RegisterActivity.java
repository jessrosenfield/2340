package com.gatech.objectsanddesign.shoppingwithfriends;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class RegisterActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
        private Firebase ref;
        private EditText mEmailView;
        private EditText mPasswordView;
        private Button mRegister;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_register, container, false);
            mEmailView = (EditText) rootView.findViewById(R.id.email_input);
            mPasswordView = (EditText) rootView.findViewById(R.id.password_input);
            mRegister = (Button) rootView.findViewById(R.id.register_button);

            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptRegister();
                        return true;
                    }
                    return false;
                }
            });

            Firebase.setAndroidContext(getActivity());
            ref = new Firebase("https://2340.firebaseio.com");

            mRegister.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    attemptRegister();
                }
            });
            return rootView;
        }

        public void attemptRegister() {
            final String email = mEmailView.getText().toString();
            final String password = mPasswordView.getText().toString();

            ref.createUser(email, password, new Firebase.ResultHandler() {

                @Override
                public void onSuccess() {
                    Intent i = new Intent(getActivity(), ApplicationScreen.class);
                    startActivity(i);
                    getActivity().finish();
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    if (isAdded()) {
                        if (firebaseError.getCode() == FirebaseError.EMAIL_TAKEN) {
                            mEmailView.setError(getString(R.string.error_account_exists));
                        } else {
                            mEmailView.setError(firebaseError.getMessage());
                        }
                    }
                }
            });
        }
    }
}
