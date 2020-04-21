/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

//password : ulQNL2c6mHAd

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener,GoogleApiClient.OnConnectionFailedListener {

  TextView loginTextView;
  EditText usernameEditText;
  EditText passwordEditText;
  ImageView googleSignUp;
  private GoogleApiClient googleApiClient;
  private static final int REQ_CODE = 9001;


  public void handleGoogleSignIn(GoogleSignInResult result){

    if(result.isSuccess())
    {
      GoogleSignInAccount account = result.getSignInAccount();
      String name = account.getDisplayName();
      String email =  account.getEmail();

      ParseUser user = new ParseUser();
      user.put("Name",name);
      user.setEmail(email);
      user.setUsername(name);
      user.put("Category","user");
      user.signUpInBackground();

      Intent intent = new Intent(getApplicationContext(),UserDashBoard.class);
      startActivity(intent);


    }
  }


  // method called when login/SignUp button is pressed

  public void loginClicked(View view){

    if(usernameEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals(""))
    {
      Toast.makeText(this,"A username and password are required.",Toast.LENGTH_LONG).show();
    }
    else
    {

        ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
            if(user!=null && e==null)
            {
              Toast.makeText(MainActivity.this,"Welcome "+ usernameEditText.getText().toString(),Toast.LENGTH_SHORT).show();
              redirectDashboard();
            }
            else
            {
              Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
          }
        });
      }


  }

  // method called when TextView is pressed
  // also takes care of keyboard hiding when pressed elsewhere

  @Override
  public void onClick(View view) {
    if(view.getId()== R.id.loginTextView)
    {
      showSignUpPage();
    }
    else if(view.getId() == R.id.backgroundLayout)
    {
      InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
    else if(view.getId() == R.id.googleSignUp)
    {
      Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
      startActivityForResult(intent,REQ_CODE);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if(requestCode == REQ_CODE)
    {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      handleGoogleSignIn(result);

//      ParseUser.logOut();
      ParseUser.logInInBackground("ravi","qwerty");

      Intent intent = new Intent(getApplicationContext(),UserDashBoard.class);
      startActivity(intent);

    }
  }

  // method for submitting the form when enter is pressed

  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {

    if(i == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_DOWN)
    {
      loginClicked(view);
    }

    return false;
  }


  // method for moving to new SignUp activity

  public void showSignUpPage(){
    Intent intent = new Intent(getApplicationContext(),SignUpPage.class);
    startActivity(intent);
  }


  // redirect function

  public void redirectDashboard(){

    if(ParseUser.getCurrentUser().get("Category").toString().equalsIgnoreCase("user"))
    {
      Intent intent = new Intent(getApplicationContext(),UserDashBoard.class);
      startActivity(intent);
    }
    else if(ParseUser.getCurrentUser().get("Category").toString().equalsIgnoreCase("delivery"))
    {
      Intent intent = new Intent(getApplicationContext(),deliveryDashBoard.class);
      startActivity(intent);
    }
  }





  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

     usernameEditText =  findViewById(R.id.usernameEditText);
     passwordEditText =  findViewById(R.id.passwordEditText);

     loginTextView = findViewById(R.id.loginTextView);
     loginTextView.setOnClickListener(this);

     googleSignUp = findViewById(R.id.googleSignUp);
     googleSignUp.setOnClickListener(this);


    RelativeLayout backgroundLayout = findViewById(R.id.backgroundLayout);


    backgroundLayout.setOnClickListener(this);

    GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

    googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

    if(ParseUser.getCurrentUser()!=null)
    {
      redirectDashboard();
    }





    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }
}


