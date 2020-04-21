package com.parse.starter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class MyAccount extends AppCompatActivity {

    ArrayList<String> accountDetails = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ListView accountListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        accountListView = findViewById(R.id.account);



        ParseUser user = ParseUser.getCurrentUser();
        accountDetails.add("");
        accountDetails.add("Name:\t"+user.getString("Name"));
        accountDetails.add("E-Mail:\t"+user.getString("email"));
        accountDetails.add("Phone No:\t"+user.getString("Phone"));
        accountDetails.add("Delivery Radius:\t"+user.getNumber("Delivery_Radius"));


        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,accountDetails);
        accountListView.setAdapter(arrayAdapter);


    }
}
