package com.sygn.xeapp.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.sygn.xeapp.R;
import com.sygn.xeapp.adapter.UserTripAdapter;
import com.sygn.xeapp.model.UserTripModel;

import java.util.ArrayList;

public class UserTripHistory extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<UserTripModel> userTripModels;
    private UserTripAdapter userTripAdapter;
    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_history_layout);

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        back =findViewById(R.id.back_btn);
        userTripModels = new ArrayList<>();
        userTripModels.add(new UserTripModel("Model Town Link Road Lahore",R.drawable.checked_ic,"Y-block, Phase 5, DHA, Lahore",  "1:50 pm", "PKR 150","Completed"));
        userTripModels.add(new UserTripModel("Barket Market",R.drawable.checked_ic,"Y-block, Phase 5, DHA, Lahore",  "2:50 pm", "PKR 200","Completed"));
        userTripModels.add(new UserTripModel("Gullberg, 3",R.drawable.checked_ic,"Y-block, Phase 5, DHA, Lahore",  "8:50 am", "PKR 280","Completed"));
        userTripModels.add(new UserTripModel("Niazi Bus Stand",R.drawable.checked_ic,"Y-block, Phase 5, DHA, Lahore",  "7:50 pm", "PKR 1200","Completed"));
        userTripModels.add(new UserTripModel("Thokar Niaz Baig",R.drawable.checked_ic,"Y-block, Phase 5, DHA, Lahore",  "12:50 am", "PKR 1350","Completed"));
        userTripModels.add(new UserTripModel("Shadman Road Lahore",R.drawable.checked_ic,"Y-block, Phase 5, DHA, Lahore",  "1:50 pm", "PKR 230","Completed"));



        userTripAdapter = new UserTripAdapter(this, userTripModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(userTripAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
