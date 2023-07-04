package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize iconfiy
        Iconify.with(new FontAwesomeModule());

        //1. start
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentFrameLayout);

        //2. start the program
        if(fragment==null){
            // Since it's null, means we start with login fragment
            fragment = createLoginFragment();
            // Embed the fragment inside the layout
            fragmentManager.beginTransaction()
                           .add(R.id.fragmentFrameLayout, fragment)
                           .commit();
        }else{
            // If the fragment is not null, the Main Activity was destroyed and recreated
            // so we need to rest the listener to the new instance of the fragment
            if(fragment instanceof  LoginFragment){
                ((LoginFragment) fragment).LoginListener();
            }
        }

    }


    //fragments
    private Fragment createLoginFragment(){
        LoginFragment fragment = new LoginFragment();
        fragment.LoginListener();
        return fragment;
    }

    public void loginTOmap(){
        //what main activity wants to do when "done" button is clicked
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        //change to fragment
        Fragment fragment = new MapsFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentFrameLayout, fragment)
                .commit();
        DataCache data = DataCache.getInstance();
        data.setFromMain(true);
    }


}