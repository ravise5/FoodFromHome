package com.parse.starter;

import com.parse.ParseGeoPoint;

import org.json.JSONArray;

public class Request {
    private String mUsername;
    private String mHomeName;
    private double mOrderPrice;
//    private ParseGeoPoint mUserLocation;
    private String mFoods;
    private String mOrderID;
    private String mPhoneNumber;

    public Request(String username,String homeName,String orderID,String phoneNumber,double orderPrice,String foods){
        mUsername = username;
        mHomeName = homeName;
        mOrderPrice = orderPrice;
//        mUserLocation =  userLocation;
        mFoods = foods;
        mOrderID = orderID;
        mPhoneNumber = phoneNumber;

    }

    public String getUsername(){return mUsername;}

    public String getPhone(){return mPhoneNumber;}

    public String getHomename(){return mHomeName;}

    public String getOrderID(){return mOrderID;}

    public double getOrderPrice(){return mOrderPrice;}

//    public ParseGeoPoint getUserLocation(){return mUserLocation;}

    public String getFoods(){return mFoods;}
}
