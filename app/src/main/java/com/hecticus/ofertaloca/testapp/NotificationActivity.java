package com.hecticus.ofertaloca.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by sansagara on 20/05/16.
 */

public class NotificationActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        textView = (TextView) findViewById(R.id.textView);
        Intent intent = NotificationActivity.this.getIntent();
        String data = intent.getStringExtra("data");
        textView.setText("Your GCM data is: "+data);
    }
}