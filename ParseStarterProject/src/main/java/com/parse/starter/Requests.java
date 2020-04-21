package com.parse.starter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Requests extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RequestAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Request> requestList;
    private ParseUser user = ParseUser.getCurrentUser();
    private ArrayList<String> orderID = new ArrayList<>();
    private String deliveryPhoneNo = user.getString("Phone");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
        requestList = new ArrayList<>();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()!=0)
                    {
                        for(ParseObject request:objects)
                        {
                            JSONArray foods = request.getJSONArray("FoodItems");
                            String foodItems = "";
                            for(int i=0;i<foods.length();i++)
                            {
                                try{
                                    foodItems+=foods.get(i);
                                    foodItems+=",";
                                }catch (Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                            }

                            requestList.add(new Request(request.getString("Name"),
                                    request.getString("HomeName"),
                                    request.getObjectId(),
                                    request.getString("Phone"),
                                    request.getDouble("OrderPrice"),
                                    foodItems));
                            orderID.add(request.getObjectId());
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
                else
                {
                    Toast.makeText(Requests.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        mRecyclerView  = findViewById(R.id.requestRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        mAdapter = new RequestAdapter(requestList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onConfirmClicked(int position) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
            query.getInBackground(orderID.get(position), new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    object.put("DeliveryPhone",deliveryPhoneNo);
                    object.put("DeliveryPerson",user.getString("Name"));
                    object.saveInBackground();
                }
            });

            removeItem(position);

                Toast.makeText(Requests.this, "Confirmed"+deliveryPhoneNo, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void removeItem(int position){
        requestList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }
}
