package com.parse.starter;

public class Food {
    private String mText1;     // mText1 : Food name
    private String mText2;     // mText2 : Food price
    private int mImageResource;
    private int mFoodQuantity;

    public Food(int imageResource,String Text1,String Text2,int qty){
        mImageResource = imageResource;
        mText1 = Text1;
        mText2 = Text2;
        mFoodQuantity = qty;
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

    public int getFoodQuantity(){return mFoodQuantity; }

    public void  incrementQty(){
            mFoodQuantity++;
    }

    public void decrementQty(){

        if(mFoodQuantity>0)
            mFoodQuantity--;
    }
}
