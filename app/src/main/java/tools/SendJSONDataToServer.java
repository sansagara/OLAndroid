package tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.hecticus.ofertaloca.testapp.OfertalocaActivity;
import com.hecticus.ofertaloca.testapp.R;

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

/**
 * Created by sansagara on 20/04/16.
 * Async send data to Server
 * @params[0] = The JSON to Send!
 */

public class SendJSONDataToServer extends AsyncTask<String,String,String> {

    //Get the Context for the Toast
    private final Boolean isFacebookLogin;
    private final String ApiRoute;
    private final Context context;

    /**
     * Constructor
     * @param context The calling activity context.
     * @param isFacebookLogin isFacebookLogin if the create is with facebook (true) or with email (false).
     */
    public SendJSONDataToServer(Context context, Boolean isFacebookLogin) {
        this.context=context;
        this.isFacebookLogin = isFacebookLogin;
        if (isFacebookLogin) {
            this.ApiRoute = context.getString(R.string.unit_server_route)+context.getString(R.string.api_route_registerfb);
        } else {
            this.ApiRoute = context.getString(R.string.unit_server_route)+context.getString(R.string.api_route_registeremail);
        }

    } // End SendJSONDataToServer method.

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
            //urlConnection.setRequestProperty("Host", "myhost.com");
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
                Toast.makeText(context, context.getString(R.string.toast_error_rest), Toast.LENGTH_LONG).show();
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine + "\n");
            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                Toast.makeText(context, context.getString(R.string.toast_empty_stream), Toast.LENGTH_LONG).show();
                return null;
            }
            JsonResponse = buffer.toString();

            if (JsonResponse.isEmpty()) {
                // JsonResponse was empty. No point in parsing.
                Toast.makeText(context, context.getString(R.string.toast_empty_response), Toast.LENGTH_LONG).show();
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
    protected void onPostExecute(String Response) {
        if (Response != null) {
            //Toast.makeText(context, "Post Executed: " + Response, Toast.LENGTH_LONG).show();
            Log.i("JSON","Post Executed: " + Response );

            //Parse response to get its values (and check for userID).

            JSONObject JSONCompResponse = null;
            JSONObject JSONResponse = null;
            try {
                JSONCompResponse = new JSONObject(Response);
                JSONResponse = JSONCompResponse.getJSONObject("response");

                //If the response haves error = 0 means everything is OK.
                if (JSONCompResponse.getInt("error") == 0 || JSONCompResponse.getInt("error") == 1) {
                    //Get the userID
                    int userID = JSONResponse.getInt("id_client");
                    String nickname = JSONResponse.getString("nickname");
                    String email = JSONResponse.getString("login");
                    int remainingBids = JSONResponse.getInt("available_bids");


                    //Save to Shared Preferences
                    final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    Log.i("SP", "Saving userID " + userID);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(context.getString(R.string.prefs_userid_key), userID);
                    editor.putString(context.getString(R.string.prefs_nickname_key), nickname);
                    editor.putString(context.getString(R.string.prefs_email_key), email);
                    editor.putInt(context.getString(R.string.prefs_remaining_bids_key), remainingBids);
                    editor.apply();

                    //Handle special case when user already logged in. (Just send a toast)
                    Toast.makeText(context, context.getString(R.string.toast_user_exists_but_logged), Toast.LENGTH_SHORT).show();

                    //Call Next Activity
                    Intent intent = new Intent(context.getApplicationContext(), OfertalocaActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);


                } else if (JSONCompResponse.getInt("error") == 2) {
                    //User exists!! (But password don't match)
                    Toast.makeText(context, context.getString(R.string.toast_user_exists), Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                Log.e("JSON","Exception parsing response: " + e );
            }

        } else {
            Toast.makeText(context, "Something gone wrong. Null result!", Toast.LENGTH_SHORT).show();
        }
    } // End onPostExecute method

} // End SendJSONDataToServer class.