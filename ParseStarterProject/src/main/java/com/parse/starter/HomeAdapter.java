package com.parse.starter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;



public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder>{

    private ArrayList<Home> mHomeArrayList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }



    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public HomeViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.homeImageView);
            mTextView1 = itemView.findViewById(R.id.TextView1);
            mTextView2 = itemView.findViewById(R.id.TextView2);

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
        }
    }

    public HomeAdapter(ArrayList<Home> homeArrayList){
        mHomeArrayList = homeArrayList;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home,parent,false);
        HomeViewHolder homeViewHolder = new HomeViewHolder(view,mListener);
        return homeViewHolder;

    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {

        Home currentHome = mHomeArrayList.get(position);
        holder.mImageView.setImageResource(currentHome.getImageResource());
        holder.mTextView1.setText(currentHome.getmText1());
        holder.mTextView2.setText(currentHome.getmText2());

    }

    @Override
    public int getItemCount() {
        return mHomeArrayList.size();
    }
}
