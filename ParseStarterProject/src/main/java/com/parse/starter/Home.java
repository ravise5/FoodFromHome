package com.parse.starter;

import com.parse.ParseGeoPoint;

public class Home {

    private String mText1;
    private String mText2;
    private int mImageResource;
    private ParseGeoPoint mlocation;

    public Home(int imageResource,String Text1,String Text2,ParseGeoPoint location){
        mImageResource = imageResource;
        mText1 = Text1;
        mText2 = Text2;
        mlocation = location;
    }

    public int getImageResource(){
        return mImageResource;
    }

    public String getmText1(){
        return mText1;
    }

    public String getmText2(){
        return mText2;
    }

    public ParseGeoPoint getLocation(){return mlocation;}


}
