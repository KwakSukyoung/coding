package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;

import Model.Event;
import Model.Person;

public class SearchActivity extends AppCompatActivity {
    private static final int PEOPLE_TYPE = 0;
    private static final int EVENTS_TYPE = 1;
    DataCache data = DataCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        DataCache data = DataCache.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Iconify.with(new FontAwesomeModule());

        //find recycler view
        RecyclerView recyclerView = findViewById(R.id.recyclingview);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        //get the data to display
        ArrayList<Person> TempPersons = data.getPersons();

        ArrayList<Event> TempEvents = new ArrayList<>();
        for(Event event: data.getEvents()){
            if((!data.isShowMale())&&(data.findPerson(event.getPersonID()).getGender().equals("m"))||
                    (!data.isShowFemale())&&(data.findPerson(event.getPersonID()).getGender().equals("f"))){
            }
            else{
                if((!data.isShowFather())&&(data.getFatherSide().contains(data.findPerson(event.getPersonID())))||
                        (!data.isShowMother())&&(data.getMotherSide().contains(data.findPerson(event.getPersonID())))){
                }
                else{
                    TempEvents.add(event);
                }
            }
        }

        //get the text
        SearchView searchView = findViewById(R.id.typeHere);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return false;}
            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Person> personResult = new ArrayList<>();
                ArrayList<Event> eventResult = new ArrayList<>();
                newText = newText.toLowerCase();
                //add people
                if(newText.length()>=1){
                    for(Person person: TempPersons){
                        if((person.getLastName().toLowerCase().contains(newText))||
                                (person.getFirstName().toLowerCase().contains(newText))){
                            personResult.add(person);
                        }
                    }
                    //add TempEvents
                    for(Event event: TempEvents){
                        if((event.getCountry().toLowerCase().contains(newText))||
                                (event.getCity().toLowerCase().contains(newText))||
                                (event.getEventType().toLowerCase().contains(newText))||
                                (String.valueOf(event.getYear()).toLowerCase().contains(newText))){
                            eventResult.add(event);
                        }
                    }
                }

                //get Adapter
                MyAdapter adapter = new MyAdapter(personResult, eventResult);
                recyclerView.setAdapter(adapter);

                return true;
            }
        });
    }
    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        private final ArrayList<Person> people;
        private final ArrayList<Event> events;

        public MyAdapter(ArrayList<Person> people, ArrayList<Event> events) {
            this.people = people;
            this.events = events;
        }

        @Override
        public int getItemViewType(int position) {
            return position < people.size()? PEOPLE_TYPE: EVENTS_TYPE;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if(viewType == PEOPLE_TYPE){
                view = getLayoutInflater().inflate(R.layout.style_event, parent, false);
            }
            else{
                view = getLayoutInflater().inflate(R.layout.style_person, parent, false);
            }
            return new MyViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            if(position < people.size()){
                holder.bind(people.get(position));
            }
            else{
                holder.bind(events.get(position-people.size()));
            }
        }

        @Override
        public int getItemCount() {
            return people.size()+events.size();
        }
    }
    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final int viewType;
        private TextView fullname;
        private TextView identity;
        private TextView eventView;
        private TextView name;
        private Person person;
        private Event event;
        ImageView genderImageView;
        ImageView marker;

        MyViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;
            itemView.setOnClickListener(this);

            if(viewType==PEOPLE_TYPE){
                fullname = itemView.findViewById(R.id.fullNameForFamily);
                identity = itemView.findViewById(R.id.whoRU);
                genderImageView = itemView.findViewById(R.id.markerImage);

            }
            else{
                eventView = itemView.findViewById(R.id.typeNplace);
                name = itemView.findViewById(R.id.name);
                marker = itemView.findViewById(R.id.markerImage);
            }
        }
        private void bind(Person daPerson){
            this.person = daPerson;
            fullname.setText(daPerson.getFirstName()+" "+ daPerson.getLastName());
            identity.setText(data.getIdentify().get(daPerson));

            if (person.getGender().equals("f")) {
                Drawable personIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female).colorRes(android.R.color.holo_red_light).sizeDp(40);
                genderImageView.setImageDrawable(personIcon);
            } else {
                Drawable personIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male).colorRes(android.R.color.holo_blue_light).sizeDp(40);
                genderImageView.setImageDrawable(personIcon);
            }
        }
        private void bind(Event daEvent){
            this.event = daEvent;
            eventView.setText(daEvent.getEventType().toUpperCase()+ ": " + daEvent.getCity()+", "+ daEvent.getCountry()+ " ("+ daEvent.getYear()+ ")");
            Person me = data.findPerson(daEvent.getPersonID());
            if(me!=null){
                name.setText(me.getFirstName() + " " + me.getLastName());
                Drawable personIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(android.R.color.black).sizeDp(40);
                marker.setImageDrawable(personIcon);
            }
        }

        @Override
        public void onClick(View v) {
            if(viewType == PEOPLE_TYPE) {
                v.setOnClickListener(v12 -> {
                    Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                    intent.putExtra("personID", person.getPersonID());
                    startActivity(intent);
                });

            } else {

                v.setOnClickListener(v1 -> {
                    Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                    intent.putExtra("eventID", event.getEventID());
                    startActivity(intent);
                });
            }
        }
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