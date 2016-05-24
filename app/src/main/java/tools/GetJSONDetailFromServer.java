package tools;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.hecticus.ofertaloca.testapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sansagara on 22/04/16.
 * Async get data from Server
 */

public class GetJSONDetailFromServer extends AsyncTask<String,String,String> {

    //Get the Context for the Toast
    private String ApiRoute = null;
    private Context context = null;
    public AsyncResponseDetail delegate = null;

    /**
     * Constructor
     * @param context The calling activity context.
     */
    public GetJSONDetailFromServer(Context context, AsyncResponseDetail delegate, int AuctionID) {
        this.context=context;
        this.ApiRoute = context.getString(R.string.ofertaloca_server_route)+context.getString(R.string.api_route_listauction_detailbyid)+AuctionID;
        this.delegate = delegate;

    } // End SendJSONDataToServer constructor.

    public GetJSONDetailFromServer(AsyncResponseDetail delegate) {
        this.delegate = delegate;

    } // End SendJSONDataToServer constructor.





    @Override
    protected String doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(ApiRoute);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }

        return response.toString();

    } // End doInBackground method


    @Override
    protected void onPostExecute(String response) {

        if (response != null) {  // If response is not null.
            //Toast.makeText(context, "Post Executed: " + response, Toast.LENGTH_SHORT).show();

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
                String product_description_url = auction_data.getString("path_description");

                //Auction attributes.
                int id = auction_data.getInt("id_auction");
                int status = auction_data.getInt("status");
                int remaining_time = auction_data.getInt("time_remaining");
                double accumulated_price = auction_data.getDouble("accumulated_price");

                //Get Bids for history!.
                JSONArray bids = auction_data.getJSONArray("bids_list");
                ArrayList<Bid> bids_list = new ArrayList<>();
                for (int i = 0; i < bids.length(); ++i) {
                    JSONObject bid = bids.getJSONObject(i);
                    String nickname = bid.getString("client");
                    double accumulated = bid.getDouble("accumulated");
                    double value = bid.getDouble("value");
                    bids_list.add(new Bid(nickname, accumulated, value));
                }

                //Create Auction (and Product) objects.
                Auction auction_detail = new Auction(product_name, product_description, product_market_price, product_image_url, id, status, remaining_time, accumulated_price, bids_list, product_description_url);

                //Return the Auction Detail.
                delegate.processFinish(auction_detail);

                //Toast.makeText(context, "Response:" + response2.toString(), Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else { // If response is null
            Toast.makeText(context, "Something gone wrong. Null result!", Toast.LENGTH_SHORT).show();
        }
    } // End onPostExecute method
}  // End GetJSONDataToServer class
