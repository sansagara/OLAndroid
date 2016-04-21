package com.example.sansagara.testapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_home);

        //Set Typeface
        Typeface typeFace = Typeface.createFromAsset(getAssets(),"Futura-Book.ttf");
        //Get TextViews
        TextView TextView1 = (TextView)findViewById(R.id.home_welcome_text);
        TextView TextView2 = (TextView)findViewById(R.id.home_welcome_text2);

        //Set Font
        TextView1.setTypeface(typeFace);
        TextView2.setTypeface(typeFace);

        Button newClientButton = (Button) findViewById(R.id.new_client);
        Button returningClientButton = (Button) findViewById(R.id.returning_client);

        //New Client Button
        newClientButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(HomeActivity.this, SignupActivity.class);
                HomeActivity.this.startActivity(myIntent);

            }
        });

        //Returning Client Button
        returningClientButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(HomeActivity.this, MegapopActivity.class);
                HomeActivity.this.startActivity(myIntent);

            }
        });


    }

}
