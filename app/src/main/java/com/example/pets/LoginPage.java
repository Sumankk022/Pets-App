package com.example.pets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class LoginPage extends AppCompatActivity {

    Button btn,submit;
    TextView registerbtn;
    EditText username,password;
    String user,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        registerbtn = findViewById(R.id.registerbtn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submitbtn);

        Intent intent = getIntent();
        String usernameFromDB = intent.getStringExtra("username");
        String passwordFromDB = intent.getStringExtra("password");

        username.setText(usernameFromDB);
        password.setText(passwordFromDB);

    }

    public void backtoregister(View view){
        Intent intent = new Intent(LoginPage.this,RegistrationPage.class);
        startActivity(intent);
    }
}