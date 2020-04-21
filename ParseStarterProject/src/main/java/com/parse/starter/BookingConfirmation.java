package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class BookingConfirmation extends AppCompatActivity {

    private String orderID;
    private String name;
    private String phone;

    public void  refresh(View view){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);

        Bundle bundle = getIntent().getExtras();
        String info = bundle.getString("Info");
        String[] details = info.split("\\s+");

        TextView orderIDTextView =  findViewById(R.id.orderIDTextView);
        orderIDTextView.append(" "+details[0]);

        TextView nameTextView =  findViewById(R.id.nameTextView);
        nameTextView.append(" "+details[1]);

        TextView phoneTextView =  findViewById(R.id.phoneTextView);
        phoneTextView.append(" "+details[2]);
    }
}

