package tools;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.hecticus.ofertaloca.testapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sansagara on 20/04/16.
 * Async send data to Server
 */

public class SendBidToServer extends AsyncTask<String,String,String> {

    //Get the Context for the Toast
    private Boolean isScheduled;
    private String ApiRoute;
    private Context context;
    public AsyncResponseDetail delegate = null;

    /**
     * Constructor
     * @param context The calling activity context.
     * @param isScheduled  if the bid is scheduled or immediate.
     * @param AuctionID  the ID of the Auction to bid
     * @param ClientID  the ID of the bidding client.
     */
    public SendBidToServer(Context context, Boolean isScheduled, int AuctionID, int ClientID) {
        this.context=context;
        this.isScheduled = isScheduled;
        if (isScheduled) {
            String prevApiRoute= context.getString(R.string.unit_server_route)+context.getString(R.string.api_route_schedulebid);
            this.ApiRoute =  String.format(prevApiRoute, AuctionID, ClientID);

        } else {
            String prevApiRoute= context.getString(R.string.unit_server_route)+context.getString(R.string.api_route_bid);
            this.ApiRoute =  String.format(prevApiRoute, AuctionID, ClientID);
        }

    } // End SendBidToServer method.


    public SendBidToServer(AsyncResponseDetail delegate) {
        this.delegate = delegate;

    } // End SendJSONDataToServer constructor.


    @Override
    protected String doInBackground(String... params) {
        String JsonResponse = null;
        String JsonDATA = params[0];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL( ApiRoute);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);

            // is output buffer writer
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Host", "myhost.com");
            //urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; PPC; en-US; rv:1.3.1)");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            //urlConnection.setRequestProperty("Accept-Charset", "UTF-8");

            //set headers and method
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(JsonDATA);

            // json data
            writer.close();
            InputStream inputStream = urlConnection.getInputStream();

            //input stream
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine + "\n");
            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            JsonResponse = buffer.toString();

            if (JsonResponse.isEmpty()) {
                // JsonResponse was empty. No point in parsing.
                return null;
            }
            //response data!.
            Log.i("JSON: ",JsonResponse);
            return JsonResponse;

        } catch (IOException e) {
            e.printStackTrace();
        }

        finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("Error: ", "Error closing stream", e);
                }
            }

        } //End finally
        return null;
    } // End doInBackground method


    @Override
    protected void onPostExecute(String response) {
        if (response != null) {
            Toast.makeText(context, "Posting to WS: " + ApiRoute , Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "Post Executed: " + response, Toast.LENGTH_LONG).show();

            try {
                //Parse response as JSON.
                JSONObject JSONResponse = new JSONObject(response);

                //Get error node from JSON.
                Integer error = JSONResponse.getInt("error");
                if (error != 0) throw new JSONException("Rest Service returned error.");
                //Toast.makeText(context, "Error:" + error, Toast.LENGTH_SHORT).show();

                //Get description node from JSON.
                //String description = JSONResponse.getString("description");
                //Toast.makeText(context, "Description:" + description, Toast.LENGTH_SHORT).show();

                //Get response object node from JSON.
                JSONObject auction_data = JSONResponse.getJSONObject("response");

                //Product attributes
                String product_name = auction_data.getString("product_name");
                String product_description = auction_data.getString("product_description");
                double product_market_price = auction_data.getDouble("market_price");
                String product_image_url = auction_data.getJSONArray("product_images").getJSONObject(0).getString("url");

                //Auction attributes.
                int id = auction_data.getInt("id_auction");
                int status = auction_data.getInt("status");
                int remaining_time = auction_data.getInt("time_remaining");
                double accumulated_price = auction_data.getDouble("accumulated_price");

                //Get Bids for history!.
                JSONArray bids = JSONResponse.getJSONArray("bids_list");
                ArrayList<Bid> bids_list = new ArrayList<>();
                for (int i = 0; i < bids.length(); ++i) {
                    JSONObject bid = bids.getJSONObject(i);
                    String nickname = auction_data.getString("client");
                    double accumulated = auction_data.getDouble("accumulated");
                    double value = auction_data.getDouble("value");
                    bids_list.add(new Bid(nickname, accumulated, value));
                }

                //Create Auction (and Product) objects.
                Auction auction_detail = new Auction(product_name, product_description, product_market_price, product_image_url, id, status, remaining_time, accumulated_price, bids_list);

                //Return the Auction Detail. This will re-draw the layout after the bid.
                delegate.processFinish(auction_detail);

                //Toast.makeText(context, "Response:" + response2.toString(), Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(context, "Something gone wrong. Null result!", Toast.LENGTH_LONG).show();
        }
    } // End onPostExecute method

} // End SendBidToServer class