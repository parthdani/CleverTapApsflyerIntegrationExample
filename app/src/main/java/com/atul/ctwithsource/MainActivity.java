package com.atul.ctwithsource;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.appsflyer.AppsFlyerLib;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.SyncListener;

import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private CleverTapAPI cleverTapDefaultInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        Button getCTIDbutton;
        Button login;

        final EditText name;
        final EditText email;
        final EditText phone;
        final EditText id;

        setContentView(R.layout.activity_main);

        getCTIDbutton = findViewById(R.id.getctidbutton);
        final TextView ctid = findViewById(R.id.ctidtext);

        login = findViewById(R.id.loginbutton);
        name = findViewById(R.id.uname);
        email = findViewById(R.id.emailtxt);
        phone = findViewById(R.id.phonetxt);
        id = findViewById(R.id.identity);

        //Clevertap
        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());


        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG);   //Set Log level to DEBUG log warnings or other important messages

        getCTIDbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                // event with properties
                ctid.setText(cleverTapDefaultInstance.getCleverTapID());

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
                profileUpdate.put("Name", name.getText().toString());                  // String
                profileUpdate.put("Identity", id.getText().toString());                    // String or number
                profileUpdate.put("Email", email.getText().toString());               // Email address of the user
                profileUpdate.put("Phone", phone.getText().toString());

                SyncListener listener1 = new SyncListener() {
                    @Override
                    public void profileDataUpdated(JSONObject updates) {

                    }

                    @Override
                    public void profileDidInitialize(String CleverTapID) {
                        String attributionID = CleverTapAPI.getDefaultInstance(getApplicationContext()).getCleverTapAttributionIdentifier();
                        AppsFlyerLib.getInstance().setCustomerUserId(attributionID);
                        AppsFlyerLib.getInstance().trackEvent(MainActivity.this, "inApp_login", profileUpdate);

                    }
                };

                CleverTapAPI.getDefaultInstance(getApplicationContext()).setSyncListener(listener1);
                cleverTapDefaultInstance.onUserLogin(profileUpdate);

            }
        });

    }

}



