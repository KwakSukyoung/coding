package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import Model.Person;
import Model.Event;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapsFragment extends Fragment implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback, GoogleMap.OnMapLoadedCallback{
    //save the map
    private GoogleMap mapmap;
    //get the datacache
    private DataCache data = DataCache.getInstance();
    //saving polylines
    private ArrayList<Polyline> eventsLines;
    private ArrayList<Polyline> spouseLines;
    private ArrayList<Polyline> ancestorLines;
    //saving the view FSR
    private View vv;
    private Event myEvent = new Event();

    //NADA
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    //NADA
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container,savedInstanceState );
        //inflate the layout for the fragment
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //initialize the map and pass it to callback when map is ready
        mapFragment.getMapAsync(this);
        vv = view;
        setHasOptionsMenu(true);
        //initialize icons
        Iconify.with(new FontAwesomeModule());
        return view;
    }
    //NADA
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }
    //when setting and search icons are clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menu) {
        if(menu.getItemId()==R.id.settingMenuItem){
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
            onResume();
            return true;
        }
        if(menu.getItemId()==R.id.searchMenuItem){
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
            onResume();
            return true;
        }
        else return super.onContextItemSelected(menu);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        eventsLines = new ArrayList<>();
        spouseLines = new ArrayList<>();
        ancestorLines = new ArrayList<>();
        data.fatherSide();
        data.motherSide();
        //stores pointer to the map
        mapmap = googleMap;
        //get the map shrinker
        UiSettings settings = mapmap.getUiSettings();
        settings.setZoomControlsEnabled(true);

        //assign colors
        Map<String, Float> colorMap = new HashMap<>();
        Float color = 0f;
        for(String type: data.eventTypes()){
            colorMap.put(type, color);
            color += 30;
        }
        data.setEventsColors(colorMap);

        //mark events
        if(data.isShowMother()){
            putMotherMarkers();
        }
        if(data.isShowFather()){
            putFatherMarkers();
        }
        putMeNSpouseMarkers();

        //coming from eventActivity and we have eventID
        if(!data.isFromMain()){
            setHasOptionsMenu(false);
            if(getArguments().getString("eventID")!=null){
                myEvent = data.findMyEvent(getArguments().getString("eventID"));
                mapmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myEvent.getLatitude(), myEvent.getLongitude()), 5.0f));

                //put the full name
                putEventInfo(myEvent);

                //put the icon
                putIcon(myEvent);

                // Set a listener for polylines
                //spouse
                if(data.isShowSpouse()){
                    addSpouseLine();
                }

                //my Events
                if(data.isShowLifeStory()){
                    addEventLine();
                }
                //ancestors
                addAncestorLine();

                //make things clickable
                checkClicking(myEvent);

            }
        }

        mapmap.setOnMarkerClickListener(marker1 -> {
            for(Polyline polyline: ancestorLines){
                polyline.remove();
            }
            for(Polyline polyline: spouseLines){
                polyline.remove();
            }
            for(Polyline polyline: eventsLines){
                polyline.remove();
            }
            //get the event
            myEvent = (Event) marker1.getTag();
            mapmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myEvent.getLatitude(), myEvent.getLongitude()), 5.0f));

            //put the full name
            putEventInfo(myEvent);

            //put the icon
            putIcon(myEvent);
            // Set a listener for poly-lines
            //spouse
            if(data.isShowSpouse()){
                addSpouseLine();
            }

            //my Events
            if(data.isShowLifeStory()){
                addEventLine();
            }

            //ancestors
            addAncestorLine();

            //make things clickable
            checkClicking(myEvent);

            return true;
        });
    }

    @Override
    public void onMapLoaded() {}

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {return false;}

    private void lineHelper(Person person,Event theEvent, Float width){
        Person me = person;
        if(me.getFatherID()!=null){
            Person father = data.findPerson(me.getFatherID());
            Event daEvent = data.sortEvents(father).get(0);
            if(daEvent!=null){
                ancestorLines.add(this.mapmap.addPolyline((new PolylineOptions())
                        .add(new LatLng(theEvent.getLatitude(), theEvent.getLongitude()), new LatLng(daEvent.getLatitude(), daEvent.getLongitude()))
                        .width(width-5f).color(Color.BLUE)));
            }
            lineHelper(father, daEvent, width-5f);
        }
        if(me.getMotherID()!=null){
            Person mother = data.findPerson(me.getMotherID());
            Event daEvent = data.sortEvents(mother).get(0);
            if(daEvent!=null){
                ancestorLines.add(this.mapmap.addPolyline((new PolylineOptions())
                        .add(new LatLng(theEvent.getLatitude(), theEvent.getLongitude()), new LatLng(daEvent.getLatitude(), daEvent.getLongitude()))
                        .width(width-5f).color(Color.BLUE)));
            }
            lineHelper(mother,daEvent,width-5f);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(data.isFromSetting()){
            data.setFromSetting(false);
            //clear everything
            mapmap.clear();
            //add markers
            if(data.isShowMother()){
                putMotherMarkers();
            }
            if(data.isShowFather()){
                putFatherMarkers();
            }
            putMeNSpouseMarkers();
            //add lines - any lines
            if(data.isShowSpouse()){
                if((data.isShowMale())&&(data.isShowFemale())){
                    addSpouseLine();
                }
            }
            if(data.isShowFamilyTree()){
                addAncestorLine();
            }
            if(data.isShowLifeStory()){
                addEventLine();
            }

            mapmap.setOnMarkerClickListener(marker1 -> {
                for(Polyline polyline: ancestorLines){
                    polyline.remove();
                }
                for(Polyline polyline: spouseLines){
                    polyline.remove();
                }
                for(Polyline polyline: eventsLines){
                    polyline.remove();
                }
                //get the event
                myEvent = (Event) marker1.getTag();
                mapmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myEvent.getLatitude(), myEvent.getLongitude()), 5.0f));

                //put the full name
                putEventInfo(myEvent);

                //put the icon
                putIcon(myEvent);
                // Set a listener for poly-lines
                //spouse
                if(data.isShowSpouse()){
                    if((data.isShowMale())&&(data.isShowFemale())){
                        addSpouseLine();
                    }
                }

                //my Events
                if(data.isShowLifeStory()){
                    addEventLine();
                }

                //ancestors
                if(data.isShowFamilyTree()){
                    addAncestorLine();
                }

                //make things clickable
                checkClicking(myEvent);

                return true;
            });

        }
    }
    private void putEventInfo(Event event){
        //put the full name
        TextView fullname = getView().findViewById(R.id.fullname);
        String name = data.findPerson(event.getPersonID()).getFirstName() + " "+ data.findPerson(event.getPersonID()).getLastName();
        fullname.setText(name);
        //put the Event PT
        TextView cityType = getView().findViewById(R.id.cityNtype);
        name = event.getEventType() + ": "+ event.getCity()+", ";
        cityType.setText(name);
        //put the county and Time
        TextView countyTime = getView().findViewById(R.id.countryNtime);
        name = event.getCountry() + " ("+ event.getYear()+")";
        countyTime.setText(name);
    }
    private void putIcon(Event event){
        if (Objects.equals(data.findPerson(event.getPersonID()).getGender(), "f")){
            //female
            Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(android.R.color.holo_red_light).sizeDp(40);
            ImageView genderImageView = getView().findViewById(R.id.icon);
            genderImageView.setImageDrawable(genderIcon);
        } else{
            Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(android.R.color.holo_blue_light).sizeDp(40);
            ImageView genderImageView = getView().findViewById(R.id.icon);
            genderImageView.setImageDrawable(genderIcon);
        }
    }
    private void checkClicking(Event event){
        vv.findViewById(R.id.clickThis).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PersonActivity.class);
            intent.putExtra("personID", event.getPersonID());
            startActivity(intent);
        });
        vv.findViewById(R.id.fullname).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PersonActivity.class);
            intent.putExtra("personID", event.getPersonID());
            startActivity(intent);
        });
        vv.findViewById(R.id.icon).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PersonActivity.class);
            intent.putExtra("personID", event.getPersonID());
            startActivity(intent);
        });
        vv.findViewById(R.id.cityNtype).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PersonActivity.class);
            intent.putExtra("personID", event.getPersonID());
            startActivity(intent);
        });
        vv.findViewById(R.id.countryNtime).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PersonActivity.class);
            intent.putExtra("personID", event.getPersonID());
            startActivity(intent);
        });
    }
    private void putFatherMarkers(){
        for(Person person: data.getFatherSide()){
            if(((!data.isShowMale())&&(person.getGender().equals("m")))||
                    ((!data.isShowFemale())&&(person.getGender().equals("f")))){
            }
            else{
                for(Event event: data.findEvents(person.getPersonID())){
                    //stores each event
                    LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
                    Marker marker = mapmap.addMarker(new MarkerOptions().position(location)
                            .title(event.getEventType())
                            .icon(BitmapDescriptorFactory.defaultMarker(data.getEventColors().get(event.getEventType().toLowerCase()))));
                    marker.setTag(event);
                    // Set a listener for marker click.
                    mapmap.setOnMarkerClickListener(this);
                }
            }
        }
    }
    private void putMotherMarkers(){
        for(Person person: data.getMotherSide()){
            if(((!data.isShowMale())&&(person.getGender().equals("m")))||
                    ((!data.isShowFemale())&&(person.getGender().equals("f")))){
            }
            else{
                for(Event event: data.findEvents(person.getPersonID())){
                    //stores each event
                    LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
                    Marker marker = mapmap.addMarker(new MarkerOptions().position(location)
                            .title(event.getEventType())
                            .icon(BitmapDescriptorFactory.defaultMarker(data.getEventColors().get(event.getEventType().toLowerCase()))));
                    marker.setTag(event);
                    // Set a listener for marker click.
                    mapmap.setOnMarkerClickListener(this);
                }
            }
        }
    }
    private void putMeNSpouseMarkers(){
        Person me = data.findPerson(data.getUserPersonID());
        if(me!=null){
            if(((!data.isShowMale())&&(me.getGender().equals("m")))||
                    ((!data.isShowFemale())&&(me.getGender().equals("f")))){
            }
            else {
                for (Event event : data.findEvents(me.getPersonID())) {
                    //stores each event
                    LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
                    Marker marker = mapmap.addMarker(new MarkerOptions().position(location)
                            .title(event.getEventType())
                            .icon(BitmapDescriptorFactory.defaultMarker(data.getEventColors().get(event.getEventType().toLowerCase()))));
                    marker.setTag(event);
                    // Set a listener for marker click.
                    mapmap.setOnMarkerClickListener(this);
                }
            }
            if(me.getSpouseID()!=null){
                Person spouse = data.findPerson(me.getSpouseID());
                if(((!data.isShowMale())&&(spouse.getGender().equals("m")))||
                        ((!data.isShowFemale())&&(spouse.getGender().equals("f")))){
                }
                else {
                    if (spouse != null) {
                        for (Event event : data.findEvents(spouse.getPersonID())) {
                            //stores each event
                            LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
                            Marker marker = mapmap.addMarker(new MarkerOptions().position(location)
                                    .title(event.getEventType())
                                    .icon(BitmapDescriptorFactory.defaultMarker(data.getEventColors().get(event.getEventType().toLowerCase()))));
                            marker.setTag(event);
                            // Set a listener for marker click.
                            mapmap.setOnMarkerClickListener(this);
                        }
                    }
                }
            }
        }

    }
    private void addSpouseLine(){
        if (data.findPerson(myEvent.getPersonID()).getSpouseID()!=null){
            Person spouse = data.findPerson(data.findPerson(myEvent.getPersonID()).getSpouseID());
            ArrayList<Event> mySpouseEvent = data.sortEvents(spouse);
            if (!mySpouseEvent.isEmpty()) {
                Event daEvent = mySpouseEvent.get(0);
                spouseLines.add(this.mapmap.addPolyline((new PolylineOptions())
                        .add(new LatLng(myEvent.getLatitude(), myEvent.getLongitude()), new LatLng(daEvent.getLatitude(), daEvent.getLongitude()))
                        .width(10).color(Color.RED)));
            }
        }
    }
    private void addAncestorLine(){
        Person me = data.findPerson(myEvent.getPersonID());
        if(me!=null){
            if(data.isShowFather()){
                if(me.getFatherID()!=null){
                    Person father = data.findPerson(me.getFatherID());
                    Event daEvent = data.sortEvents(father).get(0);
                    if((!data.isShowMale())||
                            ((!data.isShowFemale())&&(me.getGender().equals("f")))){
                    }
                    else {
                        if (daEvent != null) {
                            ancestorLines.add(this.mapmap.addPolyline((new PolylineOptions())
                                    .add(new LatLng(myEvent.getLatitude(), myEvent.getLongitude()), new LatLng(daEvent.getLatitude(), daEvent.getLongitude()))
                                    .width(10).color(Color.BLUE)));
                        }
                    }
                    lineHelper(father, daEvent, 10f);
                }
            }
            if(data.isShowMother()){
                if(me.getMotherID()!=null){
                    Person mother = data.findPerson(me.getMotherID());
                    Event daEvent = data.sortEvents(mother).get(0);
                    if((!data.isShowFemale())||
                            ((!data.isShowMale())&&(me.getGender().equals("m")))){
                    }
                    else {
                        if (daEvent != null) {
                            ancestorLines.add(this.mapmap.addPolyline((new PolylineOptions())
                                    .add(new LatLng(myEvent.getLatitude(), myEvent.getLongitude()), new LatLng(daEvent.getLatitude(), daEvent.getLongitude()))
                                    .width(10).color(Color.BLUE)));
                        }
                    }
                    lineHelper(mother, daEvent, 10f);
                }
            }
        }
    }

    private void addEventLine(){
        if(myEvent.getPersonID()!=null){
            if(((!data.isShowMale())&&(data.findPerson(myEvent.getPersonID()).getGender().equals("m")))||
                    ((!data.isShowFemale())&&(data.findPerson(myEvent.getPersonID()).getGender().equals("f")))){
            }
            else{
                ArrayList<Event> myEvents = data.sortEvents(data.findPerson(myEvent.getPersonID()));
                for(int i =0;i< myEvents.size()-1;++i){
                    eventsLines.add(this.mapmap.addPolyline((new PolylineOptions())
                            .add(new LatLng(myEvents.get(i).getLatitude(), myEvents.get(i).getLongitude()), new LatLng(myEvents.get(i+1).getLatitude(), myEvents.get(i+1).getLongitude()))
                            .width(10).color(Color.GREEN)));
                }
            }
        }
    }

}