package com.example.pets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Navigation extends AppCompatActivity {

    String petnameFromDB,usernameFromDB,ownernameFromDB,breedFromDB,emailFromDB,passwordFromDB;
    FragmentManager manager =getSupportFragmentManager();
    //FragmentTransaction t = manager.beginTransaction();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Intent intent = getIntent();
        usernameFromDB = intent.getStringExtra("username");
        petnameFromDB = intent.getStringExtra("petname");
        ownernameFromDB = intent.getStringExtra("ownername");
        emailFromDB = intent.getStringExtra("email");
        passwordFromDB = intent.getStringExtra("password");
        breedFromDB = intent.getStringExtra("breed");

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();


    }

    private void showUserDetails() {


    }

    private  BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            Bundle k = new Bundle();
                            k.putString("username",usernameFromDB);
                            selectedFragment.setArguments(k);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                            break;
                        case R.id.nav_addpost:
                            selectedFragment = new PostFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                            break;
                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                            break;
                        case R.id.nav_profile:
                            ProfileFragment p = new ProfileFragment();
                            Bundle b = new Bundle();
                            b.putString("username",usernameFromDB);
                            b.putString("petname",petnameFromDB);
                            b.putString("ownername",ownernameFromDB);
                            b.putString("email",emailFromDB);
                            b.putString("password",passwordFromDB);
                            b.putString("breed",breedFromDB);
                            p.setArguments(b);
                            //t.add(R.id.fragment_container,m4);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,p).commit();
                            break;
                    }
                    return true;
                }
            };
}