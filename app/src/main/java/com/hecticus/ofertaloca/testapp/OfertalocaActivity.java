package com.hecticus.ofertaloca.testapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fragments.AllAuctions;
import fragments.ComingAuctions;
import fragments.OpenAuctions;


public class OfertalocaActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Client info from Shared Prefs.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(OfertalocaActivity.this);
        final int userID =  prefs.getInt(getString(R.string.prefs_userid_key), 0);
        final String nickname = prefs.getString(getString(R.string.prefs_nickname_key), "");
        final String regID = prefs.getString(getString(R.string.prefs_registration_id_key), "");
        final String email = prefs.getString(getString(R.string.prefs_email_key), "cliente@ofertaloca.com");
        final int remainingBids = prefs.getInt(getString(R.string.prefs_remaining_bids_key), 0);
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initNavigationDrawer(email, remainingBids);

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

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
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
            OfertalocaActivity.this.startActivity(new Intent(OfertalocaActivity.this, HomeActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void initNavigationDrawer(String email, int remainingBids) {

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.auctions:
                        Toast.makeText(getApplicationContext(),"Auctions",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.settings:
                        Toast.makeText(getApplicationContext(),"Settings",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        startActivity(new Intent(OfertalocaActivity.this, PreferencesActivity.class));
                        break;
                    case R.id.help:
                        Toast.makeText(getApplicationContext(),"Help",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logout:
                        OfertalocaActivity.this.startActivity(new Intent(OfertalocaActivity.this, HomeActivity.class));
                        break;

                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        TextView dr_email = (TextView)header.findViewById(R.id.drawer_email);
        TextView dr_bids = (TextView)header.findViewById(R.id.drawer_remaining_bids);
        dr_email.setText(email);
        dr_bids.setText( String.format(getString(R.string.drawer_remaining_bids), remainingBids) );
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }



}
