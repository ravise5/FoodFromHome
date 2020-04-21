package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

public class deliveryDashBoard extends AppCompatActivity implements View.OnClickListener {

    ImageView backImageView;
    ImageView locationImageView;
    TextView hiTextView;
    ImageView accountImageView;
    ImageView transcation;
    ParseUser user = ParseUser.getCurrentUser();


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.backImageView)
        {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
        else if(view.getId()==R.id.locationImageView)
        {
            Intent intent = new Intent(getApplicationContext(),LocationDelivery.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.deliveryImageView)
        {
            Intent intent = new Intent(getApplicationContext(),Requests.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.accountImageView)
        {
            Intent intent = new Intent(getApplicationContext(),MyAccount.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.transcation)
        {
            Intent intent = new Intent(getApplicationContext(),Transaction.class);
            startActivity(intent);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_dash_board);

        backImageView = findViewById(R.id.backImageView);
        backImageView.setOnClickListener(this);

        locationImageView = findViewById(R.id.locationImageView);
        locationImageView.setOnClickListener(this);

        hiTextView = findViewById(R.id.hiTextView);
        hiTextView.append(" "+user.getString("Name"));

        accountImageView = findViewById(R.id.accountImageView);
        accountImageView.setOnClickListener(this);

        transcation = findViewById(R.id.transcation);
        transcation.setOnClickListener(this);


        ImageView deliveryImageView = findViewById(R.id.deliveryImageView);
        deliveryImageView.setOnClickListener(this);
    }
}
