package com.parse.starter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private ArrayList<Request> mRequestList;

    private OnItemClickListener mListener;

    public RequestAdapter(ArrayList<Request> requestList){
        mRequestList = requestList;
    }



    public interface OnItemClickListener{
        void onItemClick(int position);

        void onConfirmClicked(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;

    }



    public static class RequestViewHolder extends RecyclerView.ViewHolder{

        public TextView mOrderID;
        public TextView mUsername;
        public TextView mHousename;
        public TextView mFoods;
        public TextView mPrice;
        public TextView mPhone;
        public Button mConfirm;


        public RequestViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mOrderID = itemView.findViewById(R.id.orderIDTextView);
            mUsername = itemView.findViewById(R.id.usernameTextView);
            mHousename = itemView.findViewById(R.id.housenameTextView);
            mFoods = itemView.findViewById(R.id.foodTextView);
            mPrice = itemView.findViewById(R.id.priceTextView);
            mConfirm = itemView.findViewById(R.id.confirmButton);
            mPhone = itemView.findViewById(R.id.phoneTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                    {
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            mConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(listener!=null)
                    {
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onConfirmClicked(position);
                        }

                    }

                }
            });
        }
    }


    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request,parent,false);
        RequestViewHolder requestViewHolder = new RequestViewHolder(view,mListener);
        return requestViewHolder;
    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, int position) {
        Request currentRequest = mRequestList.get(position);
        holder.mUsername.append(" "+currentRequest.getUsername());
        holder.mHousename.append(" "+currentRequest.getHomename());
        holder.mOrderID.append(" "+currentRequest.getOrderID());
        holder.mFoods.append(" "+currentRequest.getFoods());
        holder.mPrice.append(" "+currentRequest.getOrderPrice());
        holder.mPhone.append(" "+ currentRequest.getPhone());


    }

    @Override
    public int getItemCount() {
        return mRequestList.size();
    }



}
