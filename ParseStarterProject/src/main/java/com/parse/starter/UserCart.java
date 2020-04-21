package com.parse.starter;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
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

import org.json.JSONArray;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.parse.starter.StarterApplication.CHANNEL_ID;

public class UserCart extends AppCompatActivity {

    ArrayList<String> foodItems;
    ArrayAdapter<String> arrayAdapter;
    ListView cartListView;
    ParseUser user = ParseUser.getCurrentUser();
//    private ParseUser user = ParseUser.getCurrentUser();
    private String deliveryPhoneNo = user.getString("Phone");
    Boolean regular = false;
    double total = 0;
    double orderPrice = 0;
//    double distance;
    double NotRegularTotal;
    private NotificationManagerCompat notificationManagerCompat;
    private EditText title;
    private EditText message;


    private Random random = new Random();
    private int OTP = random.nextInt(10000);


    public void cancelOrder(View view){

        Toast.makeText(this,"Order Cancelled", Toast.LENGTH_SHORT).show();
        foodItems.clear();
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_gallery_item,foodItems);
        cartListView.setAdapter(arrayAdapter);
        Intent intent = new Intent(getApplicationContext(),UserDashBoard.class);
        startActivity(intent);


    }




    public void prepareListView(){


        cartListView = findViewById(R.id.cartListView);
        JSONArray food = user.getJSONArray("FoodName");
        JSONArray qty  = user.getJSONArray("Qty");
        JSONArray price = user.getJSONArray("Price");
        Double distance = user.getDouble("Distance");

        int itemSize  = 0;

        JSONArray item = new JSONArray();
        for(int i=0;i<food.length();i++)
        {
            try{
                item.put(i,food.getString(i)+"\tx"+ qty.getInt(i)+ "\tPrice: "+price.getInt(i));
                total+=price.getInt(i);
                itemSize+=qty.getInt(i);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        foodItems = new ArrayList<>();
        if(item!=null)
        {
            for(int i=0;i<item.length();i++) {
                try {
                    foodItems.add(item.getString(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(itemSize<=3)
            {
                foodItems.add("Packet Type: Small Price:10");
                total+=10;
            }
            else if(itemSize>3 && itemSize<7)
            {
                foodItems.add("Packet Type: medium Price:20");
                total+=20;
            }
            else if(itemSize>=7)
            {
                foodItems.add("Packet Type: large Price:30");
                total+=30;
            }


            double distancePrice = Math.round(2*distance);

            total +=distancePrice;

            foodItems.add("Distance Price: "+distancePrice);

            NotRegularTotal = total;

            Switch regularButton = findViewById(R.id.regularButton);

            regularButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        foodItems.remove(foodItems.size()-1);
                        orderPrice = total*0.8;
                        foodItems.add("Total after regular Customer Discount: "+orderPrice);
                        cartListView.setAdapter(arrayAdapter);
                        regular = true;
                    }
                    else
                    {
                        foodItems.remove(foodItems.size()-1);
                        orderPrice = NotRegularTotal;
                        foodItems.add("Grand Total: "+NotRegularTotal);
                        cartListView.setAdapter(arrayAdapter);
                    }

                }
            });
            orderPrice = NotRegularTotal;

            foodItems.add("Grand Total: "+orderPrice);
            arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,foodItems);
            cartListView.setAdapter(arrayAdapter);
        }
        else{
            Toast.makeText(this, "Your Cart is empty!", Toast.LENGTH_SHORT).show();
        }

    }

    public void showListView(){


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);

        prepareListView();

        notificationManagerCompat = NotificationManagerCompat.from(this);


    }

    public void Notify(View v){

        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Order Confirmation")
                .setContentText("Your OTP: "+OTP)
                .setColor(Color.parseColor("#6600cc"))
                .setSmallIcon(R.drawable.fork)
                .setBadgeIconType(R.drawable.fork)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManagerCompat.notify(1,notification);


    }

    public void placeOrder(View view){

        Notify(view);

        ParseObject requests = new ParseObject("Request");
        requests.put("OrderPrice",orderPrice);
        requests.put("Name",user.getString("Name"));
        requests.put("Phone",user.getString("Phone"));
        requests.put("Location",user.getParseGeoPoint("Location"));
        requests.put("FoodItems",user.getJSONArray("FoodName"));
        requests.put("HomeName",user.getString("HomeName"));
        requests.put("Status",true);


        requests.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                    Toast.makeText(UserCart.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                else
                {
                    Toast.makeText(UserCart.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        Intent intent = new Intent(getApplicationContext(),OTP.class);
        intent.putExtra("OTP",OTP);
        startActivity(intent);


    }
}
