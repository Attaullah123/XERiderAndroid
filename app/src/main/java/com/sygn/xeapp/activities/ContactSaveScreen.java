package com.sygn.xeapp.activities;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sygn.xeapp.R;

import com.sygn.xeapp.adapter.MianMenuAdapter;
import com.sygn.xeapp.adapter.SaveContactAdapter;
import com.sygn.xeapp.model.ConatctSaveModel;
import com.sygn.xeapp.model.MainMenuModel;

import java.util.ArrayList;

public class ContactSaveScreen extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<ConatctSaveModel> conatctSaveModels;
    private Toolbar toolbar;
    private TextView titleBar;
    private ImageView pressBack;
    private SaveContactAdapter saveContactAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_screen_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar_save_contact);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //toolbar.setBackgroundColor(Color.parseColor("#ffffff"));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        pressBack=findViewById(R.id.back_btn);
        titleBar = findViewById(R.id.bar_title);
        titleBar.setText("Mange Safe Contacts");
        pressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        conatctSaveModels = new ArrayList<>();
        conatctSaveModels.add(new ConatctSaveModel("AK", "Attaullah Khizar","03245256258"));
        conatctSaveModels.add(new ConatctSaveModel("A", "Ali","03245223562"));
        conatctSaveModels.add(new ConatctSaveModel("Z", "Zeeshan","03335256258"));
        conatctSaveModels.add(new ConatctSaveModel("U", "Umer","03455256258"));

        saveContactAdapter = new SaveContactAdapter(this, conatctSaveModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(saveContactAdapter);

    }
}
