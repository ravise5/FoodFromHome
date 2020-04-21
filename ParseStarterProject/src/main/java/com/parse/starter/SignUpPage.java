package com.parse.starter;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

public class SignUpPage extends AppCompatActivity implements View.OnKeyListener,View.OnClickListener
{

    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private RadioButton radioButton;
    private RadioGroup radioGroup;

    TextView errorTextview;
    Boolean signupSuccess = true;

    // method to show user dash board on successful signup

    public void showUserDashboard(){

        Intent intent = new Intent(getApplicationContext(),UserDashBoard.class);
        startActivity(intent);

    }



    // method called when form submitted

    public void signUpClicked(View view){


        ParseUser user  = new ParseUser();


        user.setPassword(passwordEditText.getText().toString());

        if(isValidMail(emailEditText.getText().toString()))
            user.setEmail(emailEditText.getText().toString());
        else
        {
            errorTextview.setText("\t\t\tInvalid e-mail address");
            emailEditText.setText("");
        }

        if(phoneEditText.getText().toString().length() == 10 )
            user.put("Phone",phoneEditText.getText().toString());
        else
        {
            errorTextview.append(", Phone Number");
            phoneEditText.setText("");
        }


        user.setUsername(usernameEditText.getText().toString());


        user.put("Name",nameEditText.getText().toString());

        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);

        user.put("Category",radioButton.getText().toString());


        user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null)
                    {
                        Log.d("sign up","Success!");
                        Toast.makeText(SignUpPage.this,"Congrats you are now Signed Up!",Toast.LENGTH_LONG).show();

                        if(radioButton.getText().toString().equalsIgnoreCase("user"))
                            showUserDashboard();

                    }
                    else
                    {
                        signupSuccess = false;
                        Toast.makeText(SignUpPage.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });


    }


    private boolean isValidMail(String email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    // method for submitting the form when enter is pressed

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if(i == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_DOWN)
        {
            signUpClicked(view);
        }

        return false;
    }



    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.backgroundLayout || view.getId() == R.id.signUpTextView)
        {
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        radioGroup = findViewById(R.id.categoryRadioGroup);
        errorTextview = findViewById(R.id.errorTextView);


        ConstraintLayout constraintLayout = findViewById(R.id.backgroundLayout);
        TextView signupTextView = findViewById(R.id.signUpTextView);
        constraintLayout.setOnClickListener(this);
        signupTextView.setOnClickListener(this);
    }
}
