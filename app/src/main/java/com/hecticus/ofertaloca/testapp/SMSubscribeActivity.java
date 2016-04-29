package com.hecticus.ofertaloca.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SMSubscribeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsubscribe);

        TextView TextView1 = (TextView)findViewById(R.id.smsubscribe_country_text);
        String country = tools.Location.getSIMCountry(getApplicationContext());
        if (country == null) {
            country = tools.Location.getConfigCountry(getApplicationContext());
        }
        TextView1.setText(country);

    }
}
