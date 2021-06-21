package com.example.pets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Forgot_Password extends AppCompatActivity {

    TextView username,email,password,conpassword;

    Button reset;

    TextView backtologin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.new_password);
        conpassword = findViewById(R.id.con_password);
        reset = findViewById(R.id.resetbtn);
        backtologin = findViewById(R.id.loginpage);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Forgot_Password.this,MainActivity.class));
            }
        });
    }



    private void validate() {
        if(!validateusername() | !validateemail() | !validatepassword() | !validateconfirmpassword()){
            return;
        }
//        String enteredusername = username.getText().toString();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(enteredusername).child("password");
//        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("users").child(enteredusername).child("conpassword");
//        String new_password = password.getText().toString();
//        String con_password = password.getText().toString();
//
//        reference.setValue(new_password);
//        reference2.setValue(con_password);

        Toast.makeText(this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();


    }

    private Boolean validateconfirmpassword() {
        String val = conpassword.getText().toString();
        String pat = password.getText().toString();
        if(val.isEmpty()){
            conpassword.setError("Should not be empty");
            return false;
        }
        else if(!val.matches(pat)){
            conpassword.setError("Password Doesn't match");
            return false;
        }
        else{
            conpassword.setError(null);
            return true;
        }
    }

    private Boolean validatepassword() {
        String val = password.getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        if(val.isEmpty()){
            password.setError("Should not be empty");
            return false;
        }
        else if(!val.matches(passwordVal)){
            password.setError("Invalid Password");
            return false;
        }
        else{
            password.setError(null);
            return true;
        }
    }

    private Boolean validateemail() {
        String emailfromdb = getEmailfromdb();
        String enteredemail = email.getText().toString();
        if(enteredemail.isEmpty()){
            email.setError("Should not be empty");
            return false;
        }
        else if(emailfromdb.equals(enteredemail)){
            email.setError(null);
            return true;
        }
        else {
            email.setError("Wrong email");
            return false;
        }

    }

    private Boolean validateusername() {
        String enteredusername = username.getText().toString();
        String usernamefromdb = getUsernamefromdb();
        if(enteredusername.isEmpty()){
            username.setError("Should not be empty");
            return false;
        }
        else if (usernamefromdb.equals(enteredusername)){
            username.setError(null);
            return true;
        }
        else {
            username.setError("Wrong Username");
            return false;
        }
    }

    private String getEmailfromdb(){
        String enteredusername = username.getText().toString();
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("users").child(enteredusername).child("email");

        final String[] emailfromd = new String[1];
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                emailfromd[0] = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        return emailfromd[0];
    }

    private String getUsernamefromdb(){
        String enteredusername = username.getText().toString();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("username").equalTo(enteredusername);
        final String[] usernamefromd = new String[1];

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()){
                    usernamefromd[0] = snapshot.child("username").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        return usernamefromd[0];
    }

}