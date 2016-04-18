package com.example.sansagara.testapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

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

public class SignupActivity extends AppCompatActivity {
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        //Debug mode On!
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }

        setContentView(R.layout.activity_signup);


        //Start Facebook Callback
        callbackManager = CallbackManager.Factory.create();
        final LoginButton loginButton = (LoginButton) findViewById(R.id.facebook_login);
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        String fbToken = loginResult.getAccessToken().getToken().toString();
                        String fbUserId = loginResult.getAccessToken().getUserId().toString();
                        Log.e("Facebook: ", "FBToken" + fbToken);
                        Log.e("Facebook: ", "FBUserId" + fbUserId);
                        Toast.makeText(getApplicationContext(), "Facebook UserId:" + fbUserId, Toast.LENGTH_LONG).show();

                        //Call the Create Method for Facebook SignUp.
                        CreateClient("", "", "", fbUserId, true );

                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }

                }); //End Facebook Callback

        //Set Typeface
        Typeface typeFace = Typeface.createFromAsset(getAssets(),"Futura-Book.ttf");
        //Get TextViews
        TextView TextView1 = (TextView)findViewById(R.id.login_header_text);
        TextView TextView2 = (TextView)findViewById(R.id.login_header_text2);
        TextView TextView3 = (TextView)findViewById(R.id.login_header_text_iphone);
        TextView TextView4 = (TextView)findViewById(R.id.login_header_text_iphone_desc);
        //Get InputTexts
        final EditText InputText1 = (EditText) findViewById(R.id.username);
        final EditText InputText2 = (EditText) findViewById(R.id.email);
        final EditText InputText3 = (EditText) findViewById(R.id.password);
        final EditText InputText4 = (EditText) findViewById(R.id.confirmpassword);

        //Set Font
        TextView1.setTypeface(typeFace);
        TextView2.setTypeface(typeFace);
        TextView3.setTypeface(typeFace);
        TextView4.setTypeface(typeFace);
        InputText1.setTypeface(typeFace);
        InputText2.setTypeface(typeFace);
        InputText3.setTypeface(typeFace);
        InputText4.setTypeface(typeFace);

        Button createAccountButton = (Button) findViewById(R.id.create_account);
        createAccountButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //function in the activity that corresponds to the layout button
                String login = InputText1.getText().toString();
                String email = InputText2.getText().toString();
                String password = InputText3.getText().toString();
                String confirmpassword = InputText4.getText().toString();
                //Check passwords are the same!
                if ((confirmpassword == null) || !password.equals(confirmpassword)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_password_mismatch), Toast.LENGTH_LONG).show();
                    return;
                }

                //Call the Create Method for Email SignUp.
                CreateClient(login, email, password, null, false );

            } //End OnClick
        }); // End ClickListener

    } // End OnCreate

    //Get Facebook Result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.i("Error", "Request code is " + requestCode + " ResultCode is " + resultCode + " And the data is " + data.getDataString());

    }


    public void CreateClient(String login, String email, String password, String facebookId, Boolean isFacebookLogin) {
        JSONObject createUserJSON = new JSONObject();
        try {
            createUserJSON.put("login" , login);
            createUserJSON.put("email", email);
            createUserJSON.put("password", password);
            createUserJSON.put("facebookId", facebookId);
            createUserJSON.put("country", 3);
            createUserJSON.put("language", 405);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (createUserJSON.length() > 0) {
            new SendJSONDataToServer(getApplicationContext(), isFacebookLogin).execute(String.valueOf(createUserJSON));
            //call to async class
        }

    }

} // End SignupActivity Class


//Async send data to Server
//@params[0] = The JSON to Send!
class SendJSONDataToServer extends AsyncTask <String,String,String>{

    //Get the Context for the Toast
    private final Boolean isFacebookLogin;
    private final String ApiRoute;
    private final Context context;

    //Constructor with Context
    public SendJSONDataToServer(Context context, Boolean isFacebookLogin)
    {
        this.context=context;
        this.isFacebookLogin = isFacebookLogin;
        if (isFacebookLogin) {
            this.ApiRoute = context.getString(R.string.api_route_registerfb);
        } else {
            this.ApiRoute = context.getString(R.string.api_route_registeremail);
        }

    }

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
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; PPC; en-US; rv:1.3.1)");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");

            //set headers and method
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(JsonDATA);

            // json data
            writer.close();
            InputStream inputStream = urlConnection.getInputStream();

            //input stream
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                Toast.makeText(context, "Error connecting to REST Service", Toast.LENGTH_LONG).show();
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine + "\n");
            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                Toast.makeText(context, "Empty Stream", Toast.LENGTH_LONG).show();
                return null;
            }
            JsonResponse = buffer.toString();

            if (JsonResponse.isEmpty()) {
                // JsonResponse was empty. No point in parsing.
                Toast.makeText(context, "Empty Response", Toast.LENGTH_LONG).show();
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
    }


    @Override
    protected void onPostExecute(String JSONResponse) {
        Toast.makeText(context, "Post Executed: " + JSONResponse, Toast.LENGTH_LONG).show();

        //Call MainActivity
        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

}