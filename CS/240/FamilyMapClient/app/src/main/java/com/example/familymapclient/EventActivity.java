package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Bundle extra = new Bundle();
        String eventID = getIntent().getExtras().getString("eventID");
        extra.putString("eventID", eventID);

        DataCache data = DataCache.getInstance();
        data.setFromMain(false);

        //1. start
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        MapsFragment fragmentMap = new MapsFragment();
        fragmentManager.beginTransaction().add(R.id.eventFragmentFrameLayout,fragmentMap).commit();
        fragmentMap.setArguments(extra);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

