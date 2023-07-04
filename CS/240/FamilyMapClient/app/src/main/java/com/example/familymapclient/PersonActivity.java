package com.example.familymapclient;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import Model.Person;
import Model.Event;

public class PersonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Bundle extra = getIntent().getExtras();

        ExpandableListView expandableListView = findViewById(R.id.layoutView);

        //get the info
        DataCache data = DataCache.getInstance();
        Person me = data.findPerson(extra.getString("personID"));

        //put info
        TextView FirstName = findViewById(R.id.FirstName);
        FirstName.setText(me.getFirstName());
        TextView LastName = findViewById(R.id.LastName);
        LastName.setText(me.getLastName());
        TextView Gender = findViewById(R.id.Gender);
        if(Objects.equals(me.getGender(), "f")){
            Gender.setText("Female");
        }else Gender.setText("Male");

        expandableListView.setAdapter(new ExpandableListAdapter(me));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private class ExpandableListAdapter extends BaseExpandableListAdapter{

        private static final int EVENT_POSITION = 0;
        private static final int PERSON_POSITION = 1;

        private final ArrayList<Event> events;
        private ArrayList<Person> allPeople = new ArrayList<>();

        private final Person me;
        private DataCache data = DataCache.getInstance();

        public ExpandableListAdapter(Person me) {
            this.me = me;
            this.events = data.findEvents(me.getPersonID());
            this.allPeople = data.forPersonActivity(me);
        }
        @Override
        public int getGroupCount() {return 2;}
        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case EVENT_POSITION:
                    return events.size();
                case PERSON_POSITION:
                    return allPeople.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case EVENT_POSITION:
                    return getString(R.string.lifeEvents);
                case PERSON_POSITION:
                    return getString(R.string.family);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case EVENT_POSITION:
                    return events.get(childPosition);
                case PERSON_POSITION:
                    return allPeople.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {return groupPosition;}

        @Override
        public long getChildId(int groupPosition, int childPosition) {return childPosition;}

        @Override
        public boolean hasStableIds() {return false;}

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case EVENT_POSITION:
                    titleView.setText(R.string.lifeEvents);
                    break;
                case PERSON_POSITION:
                    titleView.setText(R.string.family);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case EVENT_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.style_person, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                case PERSON_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.style_event, parent, false);
                    initializePersonView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeEventView(View eventItemView, final int childPosition) {
            if((!data.isShowFemale())&&(Objects.equals(data.findPerson(events.get(childPosition).getPersonID()).getGender(), "f"))||
                    (!data.isShowMale())&&(Objects.equals(data.findPerson(events.get(childPosition).getPersonID()).getGender(), "m"))){

            }else{
                TextView eventView = eventItemView.findViewById(R.id.typeNplace);
                eventView.setText(events.get(childPosition).getEventType().toUpperCase()+ ": " + events.get(childPosition).getCity()+", "+ events.get(childPosition).getCountry()+ " ("+ events.get(childPosition).getYear()+ ")");

                TextView fullname = eventItemView.findViewById(R.id.name);
                fullname.setText(me.getFirstName() + " " + me.getLastName());
                //put the icon
                Drawable markerIcon = new IconDrawable( PersonActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(android.R.color.black).sizeDp(40);
                ImageView genderImageView = eventItemView.findViewById(R.id.markerImage);
                genderImageView.setImageDrawable(markerIcon);

                eventItemView.findViewById(R.id.personStyle).setOnClickListener(v -> {
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    intent.putExtra("eventID", events.get(childPosition).getEventID());
                    startActivity(intent);
                });
            }
        }

        private void initializePersonView(View personItemView, final int childPosition) {

                TextView fullname = personItemView.findViewById(R.id.fullNameForFamily);
                fullname.setText(allPeople.get(childPosition).getFirstName() + " " + allPeople.get(childPosition).getLastName());
                TextView identity = personItemView.findViewById(R.id.whoRU);
                identity.setText(data.getIdentify().get(allPeople.get(childPosition)));

                //put the icon
                if (allPeople.get(childPosition).getGender().equals("f")) {
                    Drawable personIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female).colorRes(android.R.color.holo_red_light).sizeDp(40);
                    ImageView genderImageView = personItemView.findViewById(R.id.markerImage);
                    genderImageView.setImageDrawable(personIcon);
                } else {
                    Drawable personIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male).colorRes(android.R.color.holo_blue_light).sizeDp(40);
                    ImageView genderImageView = personItemView.findViewById(R.id.markerImage);
                    genderImageView.setImageDrawable(personIcon);
                }

                personItemView.setOnClickListener(v -> {
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    intent.putExtra("personID", allPeople.get(childPosition).getPersonID());
                    startActivity(intent);
                });
        }


        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }
        //false for now
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