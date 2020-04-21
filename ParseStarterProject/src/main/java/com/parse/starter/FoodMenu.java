package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FoodMenu extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FoodAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Food> foodList;
    private String homeName;
    ParseUser user = ParseUser.getCurrentUser();
    ParseGeoPoint homeLocation;
    ParseGeoPoint userLocation = user.getParseGeoPoint("Location");
    String HomeName;
    double distance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);
        distanceCalc();


        updateMenu();

        buildRecyclerView();


    }


    public void distanceCalc(){
        HomeName = user.getString("HomeName");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Homes");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0)
                    {
                        for(ParseObject home:objects)
                        {
                            if(home.getString("Name").equals(HomeName))
                            {
                                homeLocation = home.getParseGeoPoint("Location");

                                LatLng location1 = new LatLng(homeLocation.getLatitude(),homeLocation.getLongitude());
                                LatLng location2 = new LatLng(userLocation.getLatitude(),userLocation.getLongitude());

                                distance = SphericalUtil.computeDistanceBetween(location1,location2)/1000;

                                user.put("Distance",distance);

                                user.saveInBackground();

//

                            }
                        }
                    }
                }

            }
        });

    }
    public void updateMenu(){

        Bundle bundle = getIntent().getExtras();
        homeName = bundle.getString("HomeName");
        TextView menuTextView = findViewById(R.id.menuTextView);
        menuTextView.setText(homeName + " Menu");
        user.put("HomeName",homeName);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null)
                    Toast.makeText(FoodMenu.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        foodList = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(homeName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()!=0)
                    {
                        for(ParseObject Food:objects)
                        {
                            foodList.add(new Food(R.mipmap.home2_resize,Food.getString("Food"),"Rs: "+Food.getInt("Price"),Food.getInt("Qty")));
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
                else
                {
                    Toast.makeText(FoodMenu.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Toast.makeText(this, "Loading the menu...", Toast.LENGTH_SHORT).show(); }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        mAdapter = new FoodAdapter(foodList);


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {


            @Override
            public void onItemClick(int position) {

            }

            @Override
            public int onItemAddClick(int position) {
                foodList.get(position).incrementQty();
                return foodList.get(position).getFoodQuantity();

            }

            @Override
            public int onItemRemoveClick(int position) {
                foodList.get(position).decrementQty();
                return foodList.get(position).getFoodQuantity();


            }
        });
    }

    public void addToCart(View view){
        ParseUser user = ParseUser.getCurrentUser();

        user.remove("FoodName");
        user.remove("Qty");
        user.remove("Price");
        for(Food food:foodList)
        {
            if(food.getFoodQuantity()>0)
            {
                user.add("FoodName",food.getmText1());
                user.add("Qty",food.getFoodQuantity());
                String str = food.getmText2();
                Pattern p = Pattern.compile("\\d+");
                Matcher matcher = p.matcher(str);

                if(matcher.find()) {
                user.add("Price", food.getFoodQuantity() * Integer.parseInt(matcher.group()));
                }

            }


        }

        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null) {
                    Toast.makeText(FoodMenu.this, "Successfully added to cart", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),UserCart.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(FoodMenu.this, e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });

    }

}
