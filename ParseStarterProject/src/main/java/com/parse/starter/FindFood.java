package com.parse.starter;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FindFood extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Home> homeList;
    int image[] = new int[]{R.mipmap.home1_resize,R.mipmap.home2_resize,R.mipmap.home3_resize};
    ParseUser user = ParseUser.getCurrentUser();


    public void  refresh(View view){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_food);

          homeList = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Homes");


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0)
                    {
                        Log.i("Entered the loop","fine");
                        int i=0;
                        for(ParseObject home:objects)
                        {
                            ParseGeoPoint homeLocation = home.getParseGeoPoint("Location");

                            ParseUser user = ParseUser.getCurrentUser();
                            ParseGeoPoint userLocation = user.getParseGeoPoint("Location");






                           Double distanceInKms = homeLocation.distanceInKilometersTo(userLocation);

                            Double distance = (double)Math.round(distanceInKms);




//                                homeList.add(new Home(R.mipmap.home1_resize,home.getString("Name"),home.getString("Description"),home.getParseGeoPoint("Location")));
//                                mAdapter.notifyDataSetChanged();

                            if(distance <= 20)
                            {
                                homeList.add(new Home(R.mipmap.home1_resize,home.getString("Name"),home.getString("Description"),home.getParseGeoPoint("Location")));
                                mAdapter.notifyDataSetChanged();                            }



                        }
                    }

                }
                else
                {
                    Toast.makeText(FindFood.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Toast.makeText(this, "Fetching Nearby Homely Food...", Toast.LENGTH_LONG).show();

        for(Home home: homeList)
        {
            Log.i("line1",home.getmText1());
            Log.i("line2",home.getmText2());
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        mAdapter = new HomeAdapter(homeList);


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String homeName = homeList.get(position).getmText1();
//                ParseGeoPoint homeLocation = new ParseGeoPoint(homeList.get(position).getLocation().getLatitude(),homeList.get(position).getLocation().getLongitude());
//                user.add("HomeLocation",homeLocation);
//                user.saveInBackground();

                Intent intent = new Intent(getApplicationContext(),FoodMenu.class);
                intent.putExtra("HomeName",homeName);
                startActivity(intent);
            }
        });
    }
}

