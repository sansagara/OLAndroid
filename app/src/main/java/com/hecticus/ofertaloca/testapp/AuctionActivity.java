package com.hecticus.ofertaloca.testapp;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import tools.AsyncResponseDetail;
import tools.Auction;
import tools.Bid;
import tools.DownloadImageTask;
import tools.GetJSONDetailFromServer;
import tools.SendBidToServer;

public class AuctionActivity extends AppCompatActivity implements AsyncResponseDetail {

    int remBids;
    String regID;
    String nickName;
    int userID;
    String product_name;
    int auction_id;
    boolean table_filled = false;
    double prevAccumPrice;
    Animation animationBounce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction);

        //Avoid keyboard popping up!.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Get extra (auction_id and product_name) params via intent.
        Bundle extras = getIntent().getExtras();
        auction_id = extras.getInt("auction_id");
        product_name = extras.getString("product_name");

        //Get Client info from Shared Prefs.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AuctionActivity.this);
        userID = prefs.getInt(getString(R.string.prefs_userid_key), 0);
        nickName = prefs.getString(getString(R.string.prefs_nickname_key), "0");
        regID = prefs.getString(getString(R.string.prefs_registration_id_key), "");
        remBids = prefs.getInt(getString(R.string.prefs_remaining_bids_key), 0);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get Layout items
        TextView auctionProductName = (TextView) findViewById(R.id.auctionProductName);
        Button bidNowButton = (Button) findViewById(R.id.auction_bidnow);
        Button bidProgramButton = (Button) findViewById(R.id.auction_bidprogram_button);
        Button buyNow = (Button) findViewById(R.id.auction_buynow_button);
        TextView currentPrice = (TextView) findViewById(R.id.currentPrice);

        //Set the Name immediately.
        auctionProductName.setText(product_name);

        //Inform Auction ID via Toast
        Toast.makeText(getApplicationContext(), "AuctionID: " + auction_id, Toast.LENGTH_SHORT).show();

        //Button Handler for Bid Now
        bidNowButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (remBids > 0) {
                    SendBid(auction_id, userID, 0, false, product_name);
                } else {
                    Toast.makeText(getApplicationContext(), "You don't have enough bids.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Button Handler for Bid Schedule
        bidProgramButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (remBids > 0) {
                    final EditText bidsToSchedule = (EditText) findViewById(R.id.auction_bidprogram_edit);
                    int bidQuant = Integer.parseInt(bidsToSchedule.getText().toString());
                    SendBid(auction_id, userID, bidQuant, false, product_name);
                } else {
                    Toast.makeText(getApplicationContext(), "You don't have enough bids.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Button Handler for Buy Now
        buyNow.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Buying product " + product_name + " for retail price...", Toast.LENGTH_SHORT).show();
                //TODO: Call WS for Buy Now (No WS yet)
            }
        });

        //Format Remaining Bids with Default Bid.
        TextView remainingBids = (TextView) findViewById(R.id.remainingBids);
        String remainingBidsPrev = getResources().getString(R.string.auction_default_remainingbids);
        String remainingBidsFinal = String.format(remainingBidsPrev, remBids);
        remainingBids.setText(remainingBidsFinal);

        //Format Current price and buy now..
        currentPrice.setText("0.0");
        String buyNowPrev = getResources().getString(R.string.auction_buynowbutton_text);
        String buyNowFinal = String.format(buyNowPrev, 0.0 );
        buyNow.setText(buyNowFinal);


        //Make Async call to Detail WS.
        // Call with 0 in auctionType for All Auctions
        GetJSONDetailFromServer asyncTask = new GetJSONDetailFromServer(getApplicationContext(), this, auction_id);
        //Call Async task
        //this to set delegate/listener back to this class
        asyncTask.delegate = this;
        //execute the async task
        asyncTask.execute();

    }


    //Go back in the toolbar.
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }


    @Override  //this override the implemented method from asyncTask
    public void processFinish(Auction auction_detail) {

        Log.d("DEL", "processFinish: CALLED");

        //Decimal formatting (2 digits)
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        //TODO: Do all this stuff below only if auction is on active status.
        final Auction fauction_detail = auction_detail;
        TextView currentPrice = (TextView) findViewById(R.id.currentPrice);
        final TextView remainingTime = (TextView) findViewById(R.id.remainingTime);
        TextView remainingBids = (TextView) findViewById(R.id.remainingBids);
        Button buyNow = (Button) findViewById(R.id.auction_buynow_button);
        ImageView productImage = (ImageView) findViewById(R.id.auctionProductImage);
        TextView maxPerfUser = (TextView) findViewById(R.id.auction_user_maxperformance);
        ImageView maxPerfUserPic = (ImageView) findViewById(R.id.imageView3);

        //Set product Image.
        if (auction_detail.getImage_url() != null) {
            new DownloadImageTask(productImage).execute(auction_detail.getImage_url());
        }

        //Current price.
        currentPrice.setText(df.format(auction_detail.getAccumulated_price()));
        animationBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        if (auction_detail.getAccumulated_price() != prevAccumPrice) {
            currentPrice.startAnimation(animationBounce);
            prevAccumPrice = auction_detail.getAccumulated_price();
        }

        //Handler for remaining time timer.
        final Handler aHandler = new Handler();

        //Timer handling.
        Runnable aMyTimeTask = new Runnable() {

            int nCounter = fauction_detail.getRemainingTime();
            public void run() {
                if (nCounter > 60) nCounter = 60;
                nCounter--;
                remainingTime.setText("00:00:" + String.format("%02d", nCounter));

                //Delay execution by one second!
                if (nCounter > 0) {
                    aHandler.postDelayed(this, 1000);
                } else {
                    //Make a new request to the server and get updated status.
                    callAsyncAuctionDetails();
                }
            }
        };

        //Start timer immediately
        aHandler.post(aMyTimeTask);

        //Remaining Bids!
        String remainingBidsPrev = getResources().getString(R.string.auction_default_remainingbids);
        String remainingBidsFinal = String.format(remainingBidsPrev, remBids);
        remainingBids.setText(remainingBidsFinal);

        //Buy now button
        String buyNowPrev = getResources().getString(R.string.auction_buynowbutton_text);
        String buyNowFinal = String.format(buyNowPrev, df.format( auction_detail.getMarket_price() ) );
        buyNow.setText(buyNowFinal);


        //If bids is null..
        List<Bid> bids;
        if (auction_detail.getBids() == null || auction_detail.getBids().size() == 0 ) {
            //Bid History Table
            bids = new ArrayList<>();
//            for (int i = 0; i < 10; ++i) {
//                bids.add(new Bid("Nickname" + i+1, i+1000, i+2500, null));
//            }
        } else {
            //Max Performing User (Right now it shows the Last Bidding user).
            maxPerfUser.setText( auction_detail.getLastUser() );
            if (auction_detail.getLastUserPic() != null) {
                new DownloadImageTask(maxPerfUserPic).execute(auction_detail.getLastUserPic());
            }
            //Create bids history table.
            bids = auction_detail.getBids();

        }

        //Method to create the bid history table.
        createBidHistoryTable(bids);

        //Create HTML Product Description detail.
        String descriptionURL = (auction_detail.getDescriptionPath());
        WebView productHTMLDescription = (WebView) findViewById(R.id.webView);
        productHTMLDescription.setWebViewClient(new WebViewClient());
        productHTMLDescription.loadUrl(descriptionURL);

    }

    private void createBidHistoryTable(List<Bid> bids) {

        TableLayout tl = (TableLayout) findViewById(R.id.bidHistory);

        if (table_filled) {
            tl.removeAllViews();
        }

        TableRow htr = new TableRow(this);
        htr.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView hnick = new TextView(this);
        hnick.setText("Usuario");
        hnick.setTypeface(null, Typeface.BOLD);
        hnick.setGravity(Gravity.CENTER);
        htr.addView(hnick);

        TextView hacum = new TextView(this);
        hacum.setText("Oferta");
        hacum.setTypeface(null, Typeface.BOLD);
        hacum.setGravity(Gravity.CENTER);
        htr.addView(hacum);

        TextView hval = new TextView(this);
        hval.setText("Val");
        hval.setTypeface(null, Typeface.BOLD);
        hval.setGravity(Gravity.CENTER);
        htr.addView(hval);

        tl.addView(htr);

        int i=0;
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);

        for (Bid bid : bids) {
            TableRow tr = new TableRow(this);
            if (i%2 == 0) {
                tr.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.bgBlue));
            }
            TextView nick = new TextView(this);
            nick.setText(bid.getClient());
            nick.setTypeface(null, Typeface.BOLD);
            nick.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.boldTextBlue));
            nick.setPadding(150, 0, 0, 0);
            tr.addView(nick);


            TextView acum = new TextView(this);
            acum.setText( df.format( bid.getAccumulated() ) );
            acum.setGravity(Gravity.CENTER);
            acum.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textBlue));
            tr.addView(acum);

            TextView val = new TextView(this);
            val.setText( df.format( bid.getValue() ) );
            val.setGravity(Gravity.CENTER);
            tr.addView(val);

            tl.addView(tr);
            i++;
        }

        table_filled = true;
    }


    /**
     * Create the client with a call to AsyncTask
     * @param AuctionID The ID of the Auction to bid for.
     * @param ClientID The ID of the bidding user/client.
     * @param bidQuantity The number of scheduled bids the user wants to do (if scheduled)
     * @param isScheduled if the bid is scheduled or direct.
     */
    public void SendBid(int AuctionID, int ClientID, int bidQuantity, boolean isScheduled, String productName) {
        JSONObject bidDataJSON = new JSONObject();
        if (isScheduled) {
            try {
                bidDataJSON.put("quantity", bidQuantity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), getString(R.string.toast_biddingnow) + productName + ". clientID: " + ClientID, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_biddingscheduled) + productName + ". clientID: " + ClientID, Toast.LENGTH_SHORT).show();
        }

        //call to async class
        SendBidToServer asyncTask = new SendBidToServer(getApplicationContext(), isScheduled, AuctionID, ClientID );
        asyncTask.delegate = this;
        asyncTask.execute(String.valueOf(bidDataJSON));

        //Substract remaining bids to shared Preferences.
        //Update pic in OfertaLocaActivity
        this.remBids = remBids -1;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(getApplicationContext().getString(R.string.prefs_remaining_bids_key), remBids);
        editor.apply();

        OfertalocaActivity  oal = new OfertalocaActivity();
        oal.updateRemainingBids(remBids -1);

    } // End CreateClient Method



    public void callAsyncAuctionDetails(){
        //call to async class
        GetJSONDetailFromServer asyncTask = new GetJSONDetailFromServer(getApplicationContext(), this, auction_id);
        asyncTask.delegate = this;
        asyncTask.execute();
    }

}