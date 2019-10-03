package com.sygn.xeapp.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sygn.xeapp.R;
import com.sygn.xeapp.model.UserTripModel;

import java.util.ArrayList;

public class UserTripAdapter extends RecyclerView.Adapter<UserTripAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<UserTripModel> userTripModels;
    int selectedPosition=-1;

    public UserTripAdapter(Context mContext, ArrayList<UserTripModel> userTripModels) {
        this.mContext = mContext;
        this.userTripModels = userTripModels;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tripLoc,tripDesLoc,tripTime,tripPrice,tripStatus;
        private ImageView thumbnail;
        private Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            tripLoc = (TextView) itemView.findViewById(R.id.trip_location);
            tripDesLoc = (TextView) itemView.findViewById(R.id.trip_des_location);
            tripTime = (TextView) itemView.findViewById(R.id.trip_time);
            tripPrice = (TextView) itemView.findViewById(R.id.trip_price);
            tripStatus = (TextView) itemView.findViewById(R.id.trip_status);
            thumbnail = (ImageView) itemView.findViewById(R.id.trip_status_ic);

        }
    }



    @Override
    public UserTripAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_history_list_row, parent, false);

        return new UserTripAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UserTripAdapter.MyViewHolder holder, final int position) {
        UserTripModel userTripModel = userTripModels.get(position);
        holder.tripLoc.setText(userTripModel.getTripLoc());
        holder.tripDesLoc.setText(userTripModel.getTripDesLoc());
        holder.tripTime.setText(userTripModel.getTripTime());
        holder.tripStatus.setText(userTripModel.getTripStatus());
        holder.tripPrice.setText(userTripModel.getTripPrice());
        holder.thumbnail.setImageResource(userTripModel.getTripStatusIcon());

//        if(selectedPosition==position){
//            holder.itemView.setBackgroundResource(R.drawable.rect2);
//        }else {
//            holder.itemView.setBackgroundResource(R.drawable.rect1);
//        }
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectedPosition=position;
//                notifyDataSetChanged();
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return userTripModels.size();
    }

    public void clearCart() {
        userTripModels.clear();
        userTripModels.clear();
        notifyDataSetChanged();
    }
}
