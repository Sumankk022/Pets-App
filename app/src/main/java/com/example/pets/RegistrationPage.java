package com.example.pets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class RegistrationPage extends AppCompatActivity {

    TextInputLayout petname,ownername,username,breed,email,password,conpassword;
    Button btn;
    TextView registerbtn;

    FirebaseDatabase database;
    DatabaseReference reference,reference2,reference3,reference4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        btn = findViewById(R.id.registerbtn);
        registerbtn = findViewById(R.id.backtoregister);
        petname = findViewById(R.id.petname);
        ownername = findViewById(R.id.ownername);
        username = findViewById(R.id.username);
        breed = findViewById(R.id.breed);
        email=findViewById(R.id.ownermail);
        password = findViewById(R.id.password);
        conpassword = findViewById(R.id.conpassword);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");
        reference2 = database.getReference("uploadcount");
        reference3 = database.getReference("followersCount");
        reference4 = database.getReference("followersList");


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });


    }

    private void registerUser() {
        if (!ValidatepetName() | !Validatebreed() | !Validateemail() | !ValidateOwnerName() | !Validateusername() | !Validatepassword() | !Validateconfirmpassword()){
            return;
        }


        String fpetname = petname.getEditText().getText().toString();
        String fownername = ownername.getEditText().getText().toString();
        String fusername = username.getEditText().getText().toString();
        String fbreed = breed.getEditText().getText().toString();
        String femail = email.getEditText().getText().toString();
        String fpassword = password.getEditText().getText().toString();
        String fconpassword = conpassword.getEditText().getText().toString();
        UserHelperClass helperClass = new UserHelperClass(fpetname,fownername,fusername,fbreed,femail,fpassword,fconpassword);
        FollowerList followerList = new FollowerList("markus0123");
        reference.child(fusername).setValue(helperClass);
        reference2.child(fusername).child("num").setValue(0);
        reference3.child(fusername).child("count").setValue(0);
        reference4.child(fusername).setValue(followerList);

        Intent intent = new Intent(this,MainActivity.class);
        Toast.makeText(this,"Register Successful Login to Continue",Toast.LENGTH_SHORT);
        startActivity(intent);
    }

    private Boolean ValidatepetName(){
        String val = petname.getEditText().getText().toString();
        if(val.isEmpty()){
            petname.setError("Should not be empty");
            return false;
        }
        else if(val.length()<=2){
            petname.setError("Name is too small");
            return false;
        }
        else{
            petname.setError(null);
            return true;
        }

    }

    private Boolean ValidateOwnerName(){
        String val = ownername.getEditText().getText().toString();
        if(val.isEmpty()){
            ownername.setError("Should not be empty");
            return false;
        }
        else if(val.length()<=2){
            ownername.setError("Name is too small");
            return false;
        }
        else{
            ownername.setError(null);
            return true;
        }

    }

    private Boolean Validateusername(){
        String val = username.getEditText().getText().toString();
        if(val.isEmpty()){
            username.setError("Should not be empty");
            return false;
        }

        else{
            username.setError(null);
            return true;
        }

    }

    private Boolean Validatebreed(){
        String val = breed.getEditText().getText().toString();
        if(val.isEmpty()){
            breed.setError(null);
            return true;
        }
        else{
            if(val.length()<=2){
                breed.setError("Name is too small");
                return false;
            }
            else{
                breed.setError(null);
                return true;
            }
        }

    }

    private Boolean Validateemail(){
        String val = email.getEditText().getText().toString();
        String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
;        if(val.isEmpty()){
            email.setError("Should not be empty");
            return false;
        }
        else if(val.length()<=2){
            email.setError("Name is too small");
            return false;
        }
        else if (!val.matches(emailpattern)){
            email.setError("Invalid Email address");
            return false;
        }
        else{
            email.setError(null);
            return true;
        }

    }

    private Boolean Validatepassword(){
        String val = password.getEditText().getText().toString();
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

    private Boolean Validateconfirmpassword(){
        String val = conpassword.getEditText().getText().toString();
        String pat = password.getEditText().getText().toString();
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


    public void backtologin(View view){
        Intent intent = new Intent(RegistrationPage.this,MainActivity.class);
        startActivity(intent);
    }

}