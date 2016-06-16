package tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.hecticus.ofertaloca.testapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sansagara on 15/06/16.
 */
public class GetRemainingBids extends AsyncTask<String, String, String> {

    private String ApiRoute;
    private Context context;
    public AsyncResponseRemBids delegate = null;

    /**
     * Constructor
     * @param context The calling activity context.
     * @param ClientID  the ID of the bidding client.
     */
    public GetRemainingBids(Context context, AsyncResponseRemBids delegate, int ClientID) {
        String prevApiRoute = context.getString(R.string.unit_server_route)+context.getString(R.string.api_route_get_remaining_bids);
        this.ApiRoute =  String.format(prevApiRoute, ClientID);
        this.context = context;
        this.delegate = delegate;
        Log.d("REST", "API Route: " + ApiRoute);
    }

    public GetRemainingBids(AsyncResponseRemBids delegate) {
        this.delegate = delegate;

    } // End GetRemainingBids constructor.

    @Override
    protected String doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL( ApiRoute);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return response.toString();
    }


    @Override
    protected void onPostExecute(String response) {

        Log.d("REST", "Updating Bids - Got client response: " + response);
        if (response != null) {  // If response is not null.
            JSONObject JSONResponse = null;

            try {
                JSONResponse = new JSONObject(response);
                JSONObject client_data = JSONResponse.getJSONObject("response");

                int remainingBids = client_data.getInt("available_bids");
                Toast.makeText(context, "Available bids updated. You have " + remainingBids, Toast.LENGTH_SHORT).show();

                final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(context.getString(R.string.prefs_remaining_bids_key), remainingBids);
                editor.apply();

                delegate.processFinish(remainingBids);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
