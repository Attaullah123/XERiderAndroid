package com.sygn.xeapp.adapter;

import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sygn.xeapp.R;
import com.sygn.xeapp.activities.MapView;
import com.sygn.xeapp.model.MainMenuModel;
import com.sygn.xeapp.utility.Constants;

import org.json.JSONObject;

import java.util.ArrayList;

public class MianMenuAdapter extends RecyclerView.Adapter<MianMenuAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<MainMenuModel> mainMenuModels;
//    public String productName;
//    private ImageLoader imageLoader;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        private Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.main_menu_text);
            thumbnail = (ImageView) itemView.findViewById(R.id.main_menu_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Context context = v.getContext();
                    Intent intent = new Intent(v.getContext(), MapView.class);
                    JSONObject xeService;
                    try {
                        xeService = (JSONObject) Constants.getContstantInstance().getJsonArray().get(position);
                        intent.putExtra(Constants.KEY_CURRENT_XE, xeService.toString());
                        Log.d("hello", "onClick: "+xeService.toString());
                        context.startActivity(intent);
                    }catch (Exception e){
                        context.startActivity(intent);
                    }
                    //  loadFragment();
                }
            });
        }

    }

    public MianMenuAdapter(Context mContext, ArrayList<MainMenuModel> productModelList) {
        this.mContext = mContext;
        this.mainMenuModels = productModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        MainMenuModel mainMenuModel = mainMenuModels.get(position);
        holder.title.setText(mainMenuModel.getName());
        //holder.thumbnail.setImageResource(mainMenuModel.getMenuImage());
        Glide.with(mContext).load(mainMenuModel.getImageUrl()).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return mainMenuModels.size();
    }

    public void clearCart() {
        mainMenuModels.clear();
        mainMenuModels.clear();
        notifyDataSetChanged();
    }
}