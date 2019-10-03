package com.sygn.xeapp.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sygn.xeapp.R;
import com.sygn.xeapp.model.CabListModel;

import java.util.ArrayList;

public class CabListAdapter extends RecyclerView.Adapter<CabListAdapter.MyViewHolder> {

    private final ServicesListAdapterListener listener;
    private Context mContext;
    private ArrayList<CabListModel> cabListModels;
    int selectedPosition=-1;

    public CabListAdapter(Context mContext, ArrayList<CabListModel> cabListModels, ServicesListAdapterListener listener) {
        this.mContext = mContext;
        this.cabListModels = cabListModels;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title,time,rate,driverId;
        private ImageView thumbnail;
        private Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.cab_name);
            time = (TextView) itemView.findViewById(R.id.cab_time);
            rate = (TextView) itemView.findViewById(R.id.rps_rupees);
            driverId = itemView.findViewById(R.id.driver_id);
            thumbnail = (ImageView) itemView.findViewById(R.id.cab_photo);

        }
    }

    @Override
    public CabListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cab_list_row, parent, false);

        return new CabListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CabListAdapter.MyViewHolder holder, final int position) {
        CabListModel cabListModel = cabListModels.get(position);
        holder.title.setText(cabListModel.getCabName());
        holder.time.setText(cabListModel.getCabTime());
        holder.driverId.setText(cabListModel.getDriverId());
        holder.rate.setText(cabListModel.getCabPrice());
        Glide.with(mContext).load(cabListModel.getCabImage()).into(holder.thumbnail);

        if(selectedPosition==position){
            holder.itemView.setBackgroundResource(R.drawable.rect2);
        }else {
            holder.itemView.setBackgroundResource(R.drawable.rect1);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.cardViewViewOnClick(v,position);
                selectedPosition=position;
                notifyDataSetChanged();

            }
        });
    }

    public interface ServicesListAdapterListener {

        void cardViewViewOnClick(View v, int position);

    }

    @Override
    public int getItemCount() {
        return cabListModels.size();
    }

    public void clearCart() {
        cabListModels.clear();
        cabListModels.clear();
        notifyDataSetChanged();
    }
}
