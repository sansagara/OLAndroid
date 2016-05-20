package com.hecticus.ofertaloca.testapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tools.SendJSONDataToServer;

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
                        Log.i("Facebook: ", "FBToken" + fbToken);
                        Log.i("Facebook: ", "FBUserId" + fbUserId);
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

        final Button createAccountButton = (Button) findViewById(R.id.create_account);
        createAccountButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //createAccountButton.setEnabled(false);
                //function in the activity that corresponds to the layout button
                String login = InputText1.getText().toString();
                String email = InputText2.getText().toString();
                String password = InputText3.getText().toString();
                String confirmpassword = InputText4.getText().toString();
                //Check passwords are the same!
                if ((confirmpassword == null) || !password.equals(confirmpassword)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_password_mismatch), Toast.LENGTH_LONG).show();
                    //createAccountButton.setEnabled(true);
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

    } // End onActivityResult method


    /**
     * Create the client with a call to AsyncTask
     * @param login the user login
     * @param email the user email
     * @param password the user (confirmed) password
     * @param facebookId the user facebookid
     * @param isFacebookLogin if the create is with facebook (true) or with email (false).
     */
    public void CreateClient(String login, String email, String password, String facebookId, Boolean isFacebookLogin) {

        //GET the registration_id from SharedPreferences.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SignupActivity.this);
        final String regID = prefs.getString(getString(R.string.prefs_registration_id_key), "");

        //Build correct JSON to send to Reg API
        JSONObject createUserJSON = new JSONObject();
        JSONObject device = new JSONObject();
        JSONArray devices = new JSONArray();
        try {

            //put device nodes.
            device.put("device_id", 1);
            device.put("registration_id", regID);
            //put "device" object into "devices" array.
            devices.put(device);
            //put first-level nodes. login is nickname on db. login is email on db.
            createUserJSON.put("nickname" , login);
            createUserJSON.put("login", email);
            createUserJSON.put("password", password);
            createUserJSON.put("facebookId", facebookId);
            createUserJSON.put("country", 3);
            createUserJSON.put("language", 405);
            createUserJSON.put("devices", devices);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //If the JSON is actually filled.
        if (createUserJSON.length() > 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_creating_account), Toast.LENGTH_LONG).show();
            Log.i("JSON", "POST body:  " + createUserJSON );
            //call to async class
            new SendJSONDataToServer(getApplicationContext(), isFacebookLogin).execute(String.valueOf(createUserJSON));
        }

    } // End CreateClient Method

} // End SignupActivity Class


