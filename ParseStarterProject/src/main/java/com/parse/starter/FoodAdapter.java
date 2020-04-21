package com.parse.starter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private ArrayList<Food> mFoodArrayList;

    private OnItemClickListener mListener;



    public interface OnItemClickListener{

        void onItemClick(int position);

        int  onItemAddClick(int position);

        int onItemRemoveClick(int position);
    }



    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public FoodAdapter(ArrayList<Food> foodList){
        mFoodArrayList = foodList;

    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public ImageView mAddButton;
        public ImageView mRemoveButton;
        public TextView mqtyTextView;

        public FoodViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.homeImageView);
            mTextView1 = itemView.findViewById(R.id.TextView1);
            mTextView2 = itemView.findViewById(R.id.TextView2);
            mAddButton = itemView.findViewById(R.id.addImageView);
            mRemoveButton = itemView.findViewById(R.id.removeImageView);
            mqtyTextView = itemView.findViewById(R.id.qtyTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(listener!=null)
                    {
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION)
                        {
                           listener.onItemClick(position);
                        }
                    }

                }
            });


            mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(listener!=null)
                    {
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION)
                        {
                            int qty = listener.onItemAddClick(position);
                            mqtyTextView.setText(Integer.toString(qty));

                        }
                    }

                }
            });


            mRemoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(listener!=null)
                    {
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION)
                        {
                           int qty = listener.onItemRemoveClick(position);
                           mqtyTextView.setText(Integer.toString(qty));
                        }
                    }


                }


            });
        }
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food,parent,false);
        FoodAdapter.FoodViewHolder foodViewHolder = new FoodAdapter.FoodViewHolder(view,mListener);
        return foodViewHolder;
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {

        Food currentFood = mFoodArrayList.get(position);
        holder.mImageView.setImageResource(currentFood.getImageResource());
        holder.mTextView1.setText(currentFood.getmText1());
        holder.mTextView2.setText(currentFood.getmText2());
        holder.mqtyTextView.setText(Integer.toString(currentFood.getFoodQuantity()));

    }

    @Override
    public int getItemCount() {
        return mFoodArrayList.size();
    }






}
