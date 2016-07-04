package com.hecticus.ofertaloca.testapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import fragments.Auctions;
import fragments.BuyBids;
import fragments.BuyCreditCard;
import fragments.BuyMSISDN;
import fragments.Help;
import fragments.History;
import fragments.MyOrders;


public class OfertalocaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private MenuItem drawerSelected;
    public int userID;
    public int remainingBids;
    public String profilePic;
    TextView dr_bids;
    String checkedPackID;
    View header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Client info from Shared Prefs.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(OfertalocaActivity.this);
        userID =  prefs.getInt(getString(R.string.prefs_userid_key), 0);
        final String nickname = prefs.getString(getString(R.string.prefs_nickname_key), "");
        final String regID = prefs.getString(getString(R.string.prefs_registration_id_key), "");
        final String email = prefs.getString(getString(R.string.prefs_email_key), "cliente@ofertaloca.com");
        remainingBids = prefs.getInt(getString(R.string.prefs_remaining_bids_key), 0);
        profilePic = prefs.getString(getString(R.string.prefs_user_profile_pic), "");
        Toast.makeText(getApplicationContext(), "userID: " + userID + " nickname: " + nickname + " regID: " + regID, Toast.LENGTH_SHORT).show();

        if (userID == 0) {
            //If no userID stored, get the client to the home screen so they can signin/signup.
            Intent myIntent = new Intent(OfertalocaActivity.this, HomeActivity.class);
            OfertalocaActivity.this.startActivity(myIntent);
            finish();
        }

        //Fill layout.
        setContentView(R.layout.activity_ofertaloca);

        //Toolbar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Drawer.
        initNavigationDrawer(email, remainingBids);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Set the profile pic in the draawer again (Now with an updated path).
        setProfileinDrawer();
        setRemainingBids();
    }

    // -----------------
    // NAVIGATION DRAWER.
    // -----------------

    public void initNavigationDrawer(String email, int remainingBids) {
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {

            //Style Navigation Drawer.
            header = navigationView.getHeaderView(0);
            TextView dr_email = (TextView) header.findViewById(R.id.drawer_email);
            dr_bids = (TextView) header.findViewById(R.id.drawer_remaining_bids);

            dr_email.setText(email);

            //Call remaining_bids set.
            setRemainingBids();

            //Call profile_pic set.
            setProfileinDrawer();

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
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_logging_out), Toast.LENGTH_SHORT).show();
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
        //  Set title in toolbar for fragments.
        setTitle(itemDrawer.getTitle());
    }


    // -------------
    // TOOLBAR MENU
    // -------------
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


        if (id == R.id.action_search) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_coming_soon), Toast.LENGTH_SHORT).show();
            //handleMenuSearch();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateDrawer() {
        dr_bids.setText(String.format(getString(R.string.drawer_remaining_bids), remainingBids));
    }



    // -----------------
    // BUY BIDS FRAGMENT
    // -----------------

    //Select the pack.
    //Gets the pack number (id) by tag
    public void selectPack(View pack) {
        String packNum = String.valueOf(pack.getTag());
        Toast.makeText(getApplicationContext(), "Pack: " + packNum, Toast.LENGTH_SHORT).show();

        if (checkedPackID != null) {
            //The packID the user selected...
            String prevCheckedPackID = checkedPackID;
            checkedPackID = "drawable_check" + packNum;

            //put new check.
            TextView check_draw = (TextView) findViewById(getResources().getIdentifier(checkedPackID, "id", getPackageName()));
            check_draw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_white_24dp, 0, 0, 0);
            check_draw.getLayoutParams().height = 100;

            if (!checkedPackID.equals(prevCheckedPackID)) {
                //remove old check.
                TextView prev_check_draw = (TextView) findViewById(getResources().getIdentifier(prevCheckedPackID, "id", getPackageName()));
                prev_check_draw.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                prev_check_draw.getLayoutParams().height = 0;
            }

        } else {
            //put new check.
            checkedPackID = "drawable_check" + packNum;
            TextView check_draw = (TextView) findViewById(getResources().getIdentifier(checkedPackID, "id", getPackageName()));
            check_draw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_white_24dp, 0, 0, 0);
            check_draw.getLayoutParams().height = 100;
        }
    }

    //Buy pack
    //Gets the payment type by tag.
    //0 -> MSISDN
    //1 -> Credit Card
    public void buyPack(View method) {
        Fragment fragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        Integer payMethod = Integer.parseInt(String.valueOf(method.getTag()));

        if (payMethod == 0) {
            Toast.makeText(getApplicationContext(), "Payment method: MSISDN ", Toast.LENGTH_SHORT).show();
            //Check if already subscribed.

            //Change fragment to MSISDN.
            fragmentoGenerico = new BuyMSISDN();
        } else if (payMethod == 1) {
            Toast.makeText(getApplicationContext(), "Payment method: CreditCard" , Toast.LENGTH_SHORT).show();
            //Check if credit card already registered.

            //Change fragment to credit card.
            fragmentoGenerico = new BuyCreditCard();
        }

        //Commit new Fragment
        fragmentManager
                .beginTransaction()
                .replace(R.id.contenedor_principal, fragmentoGenerico)
                .commit();

    }


    //------------------
    // PROFILE_PIC UPLOAD
    //--------------------


    /*
    * Set (and update) profile pic in drawer.
    * @params: picPath. The path to the profile pic drawable.
    */
    public void setProfileinDrawer(){
        //Set Profile Picture in NavBar. (From path stored in SharedPrefs).
        File imgFile = new File(profilePic);
        if (!profilePic.isEmpty() && imgFile.exists()) {
            Log.d("PROFILE", "ImagePref is not empty and file exists!");
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView circleView = (ImageView) header.findViewById(R.id.circleView);
            circleView.setImageBitmap(myBitmap);
        }
    }


    //Update path of profile picture if coming from another activity.
    public void updateProfilePicPath(String path){
        this.profilePic = path;
    }

    //Call activity to change Profile Picture of the user.
    public void changeProfilePic(View method) {
        startActivity(new Intent(this, UploadActivity.class));
    }




    //------------------
    // REMAINING BIDS
    //--------------------

    //Update path of profile picture if coming from another activity.
    public void updateRemainingBids(int bids){
        this.remainingBids = bids;
    }

    //Update textview in drawer.
    public void setRemainingBids(){
        dr_bids.setText(String.format(getString(R.string.drawer_remaining_bids), remainingBids));

    }

}
