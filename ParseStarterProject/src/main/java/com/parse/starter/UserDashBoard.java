package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

public class UserDashBoard extends AppCompatActivity implements View.OnClickListener {

    private TextView hiTextView;



    // method for back botton


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.backImageView)
        {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
        else if(view.getId()==R.id.foodImageView)
        {
            Intent intent = new Intent(getApplicationContext(),FindFood.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.locationImageView)
        {
            Intent intent = new Intent(getApplicationContext(),LocationUser.class);
            startActivity(intent);
        }
        else if(view.getId()==R.id.cartImageView)
        {
            Intent intent = new Intent(getApplicationContext(),UserCart.class);
            startActivity(intent);
        }
        else if(view.getId()==R.id.mail)
        {
            Intent intent = new Intent(getApplicationContext(),Feedback.class);
            startActivity(intent);

        }
        else if(view.getId() == R.id.trackImageView)
        {
            Intent intent = new Intent(getApplicationContext(),TrackOrder.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dash_board);


        hiTextView = findViewById(R.id.hiTextView);
        ParseUser user = ParseUser.getCurrentUser();
        hiTextView.append(" "+ user.get("Name").toString());

        ImageView backImageView = findViewById(R.id.backImageView);
        backImageView.setOnClickListener(this);

        ImageView foodImageView = findViewById(R.id.foodImageView);
        foodImageView.setOnClickListener(this);

        ImageView locationImageView = findViewById(R.id.locationImageView);
        locationImageView.setOnClickListener(this);

        ImageView cartImageView = findViewById(R.id.cartImageView);
        cartImageView.setOnClickListener(this);

        ImageView mail = findViewById(R.id.mail);
        mail.setOnClickListener(this);

        ImageView trackImageView = findViewById(R.id.trackImageView);
        trackImageView.setOnClickListener(this);


    }
}
