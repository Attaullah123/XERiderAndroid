package com.sygn.xeapp.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sygn.xeapp.R;
import com.sygn.xeapp.model.ConatctSaveModel;

import java.util.ArrayList;

public class SaveContactAdapter extends RecyclerView.Adapter<SaveContactAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ConatctSaveModel> conatctSaveModels;
//    public String productName;
//    private ImageLoader imageLoader;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView user_NickName, user_Name, user_PhoneNumber;
        private Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            user_NickName = (TextView) itemView.findViewById(R.id.user_nickname);
            user_Name = (TextView) itemView.findViewById(R.id.user_name);
            user_PhoneNumber = (TextView) itemView.findViewById(R.id.user_number);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    Context context = v.getContext();
//                    Intent intent = new Intent(v.getContext(), MapView.class);
//                    context.startActivity(intent);
//
//                }
//            });
        }
    }

    public SaveContactAdapter(Context mContext, ArrayList<ConatctSaveModel> conatctSaveModels) {
        this.mContext = mContext;
        this.conatctSaveModels = conatctSaveModels;
    }

    @Override
    public SaveContactAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_save_row, parent, false);

        return new SaveContactAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SaveContactAdapter.MyViewHolder holder, int position) {
        ConatctSaveModel conatctSaveModel = conatctSaveModels.get(position);
        holder.user_NickName.setText(conatctSaveModel.getUseNickName());
        holder.user_Name.setText(conatctSaveModel.getUserName());
        holder.user_PhoneNumber.setText(conatctSaveModel.getUserNumber());

    }

    @Override
    public int getItemCount() {
        return conatctSaveModels.size();
    }

    public void clearCart() {
        conatctSaveModels.clear();
        conatctSaveModels.clear();
        notifyDataSetChanged();
    }
}