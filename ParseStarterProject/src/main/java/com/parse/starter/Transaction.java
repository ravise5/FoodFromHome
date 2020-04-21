package com.parse.starter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Transaction extends AppCompatActivity {

    ArrayList<String> upcoming;
    ArrayList<String> previous = new ArrayList<>();
    ListView upcomingListView;
    ListView previousListView;
    ArrayAdapter<String> arrayAdapter;
    ParseUser user =    ParseUser.getCurrentUser();


    public void showUp(View view){
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,upcoming);
        upcomingListView.setAdapter(arrayAdapter);
        upcomingListView.setVisibility(View.VISIBLE);
    }

    public void showPrev(View view){
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,previous);
        previousListView.setAdapter(arrayAdapter);
        previousListView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);


        upcomingListView = findViewById(R.id.upcoming);
        previousListView = findViewById(R.id.previous);

        upcoming = new ArrayList<>();


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");

//        upcoming.add("ravi");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects.size()>0)
                {
                    for(ParseObject request:objects)
                    {
                        if(request.getBoolean("Status"))
                        {
                            upcoming.add(request.getString("HomeName")+", Order Price: "+request.getNumber("OrderPrice"));

                        }
                        else
                        {
                            previous.add(request.getString("HomeName")+", Order Price: "+request.getNumber("OrderPrice"));
                        }
                    }
                }
            }
        });







//        Toast.makeText(this, upcoming.size(), Toast.LENGTH_SHORT).show();







        }
}
