package com.example.pets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class FriendsActivity extends AppCompatActivity {

    LinearLayout ll;
    TextView username;
    public static String userEnteredFriends;
    TextView search,follow_btn;
    TextView result,username_of_user,name_of_user;
    public static ImageView userphoto;
    private long backPressedTime;
    private Toast backToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        username_of_user = findViewById(R.id.usernameofuser);
        name_of_user = findViewById(R.id.nameofuser);
        userphoto = findViewById(R.id.userphoto);
        follow_btn = findViewById(R.id.follow_btn);
        ll = findViewById(R.id.elevation);


        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation);


        bottomNavigationView.setSelectedItemId(R.id.nav_friends);
        username = findViewById(R.id.username);
        search = findViewById(R.id.search_btn);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findFriends();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_search:
                        Intent sintent = new Intent(getApplicationContext(),SearchActivity.class);
                        startActivity(sintent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_friends:
                        return true;
                    case R.id.nav_addpost:
                        Intent aintent = new Intent(getApplicationContext(),PostActivity.class);
                        startActivity(aintent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_home:
                        Intent hintent = new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(hintent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_profile:
                        Intent pintent = new Intent(getApplicationContext(),ProfileActivity.class);
                        startActivity(pintent);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private Boolean validateusername(){
        String val = username.getText().toString();

        if(val.isEmpty()){
            username.setError("Should not be empty");
            return false;
        }
        else{
            username.setError(null);
            return true;
        }
    }

    public void findFriends(){
        if(!validateusername()){
            return;
        }
        else {
//            Toast.makeText(MainActivity.this,"Logged in Successfullu",Toast.LENGTH_SHORT);
            isUser();
        }
    }

    public void isUser() {

        userEnteredFriends = username.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Query checkUser = reference.orderByChild("username").equalTo(userEnteredFriends);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                if(datasnapshot.exists()){

                    String petnameFromDB = datasnapshot.child(userEnteredFriends).child("petname").getValue(String.class);
                    String usernameFromDB = datasnapshot.child(userEnteredFriends).child("username").getValue(String.class);

                    username_of_user.setText(usernameFromDB);
                    name_of_user.setText(petnameFromDB);
                    follow_btn.setText("See profile");
                    ll.setElevation(16);
                    ll.setBackground(getDrawable(R.drawable.shadow));




                   loadprofilepic();


                }
                else {
                    username.setError("No such user exists");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void loadprofilepic() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child(userEnteredFriends).child("profilepic");
        try {
            final File localfile = File.createTempFile(userEnteredFriends,"jpeg");
            storageReference.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            userphoto.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFollow_btn(View view){
        Intent intent = new Intent(getApplicationContext(),friendProfile.class);
        intent.putExtra("userFriendName",userEnteredFriends);
        startActivity(intent);
    }
}