package com.hecticus.ofertaloca.testapp;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import fragments.FragmentPreferences;

/**
 * Created by sansagara on 06/05/16.
 */
public class PreferencesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(
                android.R.id.content, new FragmentPreferences()).commit();
    }
}
