package com.example.familymapclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SettingsActivity extends AppCompatActivity {
    DataCache data = DataCache.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Switch life = findViewById(R.id.LSL);
        Switch family = findViewById(R.id.FTL);
        Switch spouse = findViewById(R.id.SL);
        Switch father = findViewById(R.id.FS);
        Switch mother = findViewById(R.id.MS);
        Switch male = findViewById(R.id.ME);
        Switch female = findViewById(R.id.FE);
        TextView logout = findViewById(R.id.LOGOUT);

        //life events lines
        life.setChecked(data.isShowLifeStory());
        life.setOnClickListener(v -> {
            data.setShowLifeStory(!data.isShowLifeStory());
            data.setFromSetting(true);
        });

        //family events lines
        family.setChecked(data.isShowFamilyTree());
        family.setOnClickListener(v -> {
            data.setShowFamilyTree(!data.isShowFamilyTree());
            data.setFromSetting(true);
        });

        //spouse lines
        spouse.setChecked(data.isShowSpouse());
        spouse.setOnClickListener(v -> {
            data.setShowSpouse(!data.isShowSpouse());
            data.setFromSetting(true);
        });

        //father side
        father.setChecked(data.isShowFather());
        father.setOnClickListener(v -> {
            data.setShowFather(!data.isShowFather());
            data.setFromSetting(true);
        });

        //mother side
        mother.setChecked(data.isShowMother());
        mother.setOnClickListener(v -> {
            data.setShowMother(!data.isShowMother());
            data.setFromSetting(true);
        });

        //male events
        male.setChecked(data.isShowMale());
        male.setOnClickListener(v -> {
            data.setShowMale(!data.isShowMale());
            data.setFromSetting(true);
        });

        //Female events
        female.setChecked(data.isShowFemale());
        female.setOnClickListener(v -> {
            data.setShowFemale(!data.isShowFemale());
            data.setFromSetting(true);
        });

        //logout
        findViewById(R.id.LOGOUT).setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

    }


    // this event will enable the back function to the button on press
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}