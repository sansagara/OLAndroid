package com.hecticus.ofertaloca.testapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fragments.AllAuctions;
import fragments.ComingAuctions;
import fragments.OpenAuctions;


public class OfertalocaActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Client info from Shared Prefs.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(OfertalocaActivity.this);
        final int userID =  prefs.getInt(getString(R.string.prefs_userid_key), 0);
        final String nickname = prefs.getString(getString(R.string.prefs_nickname_key), "");
        final String regID = prefs.getString(getString(R.string.prefs_registration_id_key), "");
        Toast.makeText(getApplicationContext(), "userID: " + userID + " nickname: " + nickname + " regID: " + regID, Toast.LENGTH_LONG).show();

        if (userID == 0) {
            //If no userID stored, get the client to the home screen so they can signin/signup.
            Intent myIntent = new Intent(OfertalocaActivity.this, HomeActivity.class);
            OfertalocaActivity.this.startActivity(myIntent);
            finish();
        }

        //Fill layout
        setContentView(R.layout.activity_ofertaloca);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //Pager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new AllAuctions(), getString(R.string.tab1));
        adapter.addFragment(new OpenAuctions(), getString(R.string.tab2));
        adapter.addFragment(new ComingAuctions(), getString(R.string.tab3));
        /*
        adapter.addFragment(new WinnersAuctions(), getString(R.string.tab4));
        */
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            startActivity(new Intent(OfertalocaActivity.this, PreferencesActivity.class));
            return true;
        } else if (id == R.id.action_search) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_coming_soon), Toast.LENGTH_LONG).show();
            //handleMenuSearch();
        } else if (id == R.id.action_user) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_taking_to_login), Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(OfertalocaActivity.this, HomeActivity.class);
            OfertalocaActivity.this.startActivity(myIntent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }




}
