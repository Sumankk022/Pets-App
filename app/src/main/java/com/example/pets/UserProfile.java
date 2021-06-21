package com.example.pets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class UserProfile extends AppCompatActivity {

    TextView username,mainpetname,mainusername,petname,ownername,email,password,logoutbtn,breed;
    String petnameFromDB,usernameFromDB,ownernameFromDB,breedFromDB,emailFromDB,passwordFromDB;
    ProfileFragment m4 = new ProfileFragment();
    FragmentManager manager =getSupportFragmentManager();
    FragmentTransaction t = manager.beginTransaction();
    UserHelperClass u = new UserHelperClass();
    String user,pet,pass,breedname,emailid,owner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);



        username = findViewById(R.id.username);
        mainpetname = findViewById(R.id.mainpetname);
        mainusername = findViewById(R.id.mainusername);
        petname = findViewById(R.id.petname);
        ownername = findViewById(R.id.ownername);
        email = findViewById(R.id.ownermail);
        password = findViewById(R.id.password);
        logoutbtn = findViewById(R.id.logoutbtn);
        breed = findViewById(R.id.breed);

        Intent intent = getIntent();
        user = intent.getStringExtra("username");
        pet= intent.getStringExtra("petname");
        owner= intent.getStringExtra("ownername");
        emailid= intent.getStringExtra("email");
        pass= intent.getStringExtra("password");
        breedname= intent.getStringExtra("breed");

        showAllUserData();
    }

    private void showAllUserData() {

        Intent intent = getIntent();
        String usernameFromDB = intent.getStringExtra("username");
        String petnameFromDB = intent.getStringExtra("petname");
        String ownernameFromDB = intent.getStringExtra("ownername");
        String emailFromDB = intent.getStringExtra("email");
        String passwordFromDB = intent.getStringExtra("password");
        String breedFromDB = intent.getStringExtra("breed");



        username.setText(usernameFromDB);
        mainusername.setText(usernameFromDB);
        mainpetname.setText(petnameFromDB);
        petname.setText(petnameFromDB);
        ownername.setText(ownernameFromDB);
        email.setText(emailFromDB);
        password.setText(passwordFromDB);
        breed.setText(breedFromDB);


    }

    public void backtologinpage(View v){

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

//        Bundle b = new Bundle();
//        b.putString("username",usernameFromDB);
//        b.putString("petname",petnameFromDB);
//        b.putString("ownername",ownernameFromDB);
//        b.putString("email",emailFromDB);
//        b.putString("password",passwordFromDB);
//        b.putString("breed",breedFromDB);
//        m4.setArguments(b);
//        t.add(ProfileFragment.class,m4);
    }

    public void menulist(View v){
//        Intent intent = new Intent(getApplicationContext(),LoginPage.class);
//        intent.putExtra("petname",petnameFromDB);
//        intent.putExtra("ownername",ownernameFromDB);
//        intent.putExtra("breed",breedFromDB);
//        intent.putExtra("username",usernameFromDB);
//        intent.putExtra("password",passwordFromDB);
//        intent.putExtra("email",emailFromDB);
//        startActivity(intent);

        Intent intent = new Intent(getApplicationContext(),Navigation.class);
//        String user = username.getText().toString();
        //String pass= password.getText().toString();
        intent.putExtra("username",user);
        intent.putExtra("password",pass);

        intent.putExtra("petname",pet);
        intent.putExtra("ownername",owner);
        intent.putExtra("breed",breedname);
        intent.putExtra("email",emailid);

        startActivity(intent);

    }

}