package com.hecticus.ofertaloca.testapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import fragments.Auctions;
import fragments.BuyBids;
import fragments.Help;
import fragments.History;
import fragments.MyOrders;


public class OfertalocaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private MenuItem drawerSelected;


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

        //Drawer
        initNavigationDrawer(email, remainingBids);

    }


    // -----------------
    // NAVIGATION DRAWER
    // -----------------

    public void initNavigationDrawer(String email, int remainingBids) {
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {

            //Style Navigation Drawer
            View header = navigationView.getHeaderView(0);
            TextView dr_email = (TextView) header.findViewById(R.id.drawer_email);
            TextView dr_bids = (TextView) header.findViewById(R.id.drawer_remaining_bids);
            dr_email.setText(email);
            dr_bids.setText(String.format(getString(R.string.drawer_remaining_bids), remainingBids));

            //Setup Some more stuff.
            drawerToggle();
            prepararDrawer(navigationView);
            seleccionarItem(navigationView.getMenu().getItem(0));
            navigationView.getMenu().getItem(0).setChecked(true);
            drawerSelected = navigationView.getMenu().getItem(0);

        }

    }

    public void drawerToggle() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){

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

    private void prepararDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    menuItem.setChecked(true);
                    seleccionarItem(menuItem);
                    drawerLayout.closeDrawers();
                    return true;
                }
            });

    }

    private void seleccionarItem(MenuItem itemDrawer) {
        Fragment fragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (itemDrawer != drawerSelected) {
            drawerSelected = itemDrawer;
            switch (itemDrawer.getItemId()) {
                case R.id.auctions:
                    fragmentoGenerico = new Auctions();
                    break;
                    case R.id.buy_bids:
                        fragmentoGenerico = new BuyBids();
                        break;
                    case R.id.my_orders:
                        fragmentoGenerico = new MyOrders();
                        break;
                    case R.id.bid_history:
                        fragmentoGenerico = new History();
                        break;
                    case R.id.settings:
                        startActivity(new Intent(this, PreferencesActivity.class));
                        break;
                    case R.id.help:
                        fragmentoGenerico = new Help();
                        break;
                    case R.id.logout:
                        Toast.makeText(getApplicationContext(), "Logging you out...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, HomeActivity.class));
                        break;
            }
        }

        if (fragmentoGenerico != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.contenedor_principal, fragmentoGenerico)
                    .commit();
        }

        // Setear título actual
        setTitle(itemDrawer.getTitle());
    }


    //
    // TOOLBAR MENU
    //
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
            return true;
        } else if (id == R.id.action_user) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_taking_to_login), Toast.LENGTH_LONG).show();
            OfertalocaActivity.this.startActivity(new Intent(OfertalocaActivity.this, HomeActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
