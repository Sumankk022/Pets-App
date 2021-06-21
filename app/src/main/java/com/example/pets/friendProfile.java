package com.example.pets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class friendProfile extends AppCompatActivity {
    String userFriendName;
    ImageView petprofile;
    TextView username,petname;
    Button follow_btn;
    TextView friendscount;
    ImageView[] image =new ImageView[100];
    String count,count2;
    int c,c2;
    String cou;
    int co;
    int finaldeccount;
    int dec,inc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        petprofile = findViewById(R.id.pet_profile);
        username = findViewById(R.id.username);
        petname = findViewById(R.id.petname);
        follow_btn = findViewById(R.id.follow_btn);
        friendscount = findViewById(R.id.followers_num);

        Intent intent = getIntent();
        userFriendName = intent.getStringExtra("userFriendName");

        loadprofilepic();
        showUserDetails();
        setfollow_btn();
        friendcount();
        loadimg();


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("followersCount");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                count = snapshot.child(userFriendName).child("count").getValue().toString();
                count2 = snapshot.child(MainActivity.userEnteredusername).child("count").getValue().toString();

                c = Integer.parseInt(count);
                c2= Integer.parseInt(count2);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        });




        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation);


        bottomNavigationView.setSelectedItemId(R.id.nav_friends);


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

    private void loadprofilepic() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child(userFriendName).child("profilepic");
        try {
            final File localfile = File.createTempFile(userFriendName,"jpeg");
            storageReference.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            petprofile.setImageBitmap(bitmap);
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

    private void showUserDetails() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(userFriendName);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String usernameFromDB = snapshot.child("username").getValue().toString();
                String petnameFromDB = snapshot.child("petname").getValue().toString();
                String passwordFromDB = snapshot.child("password").getValue().toString();
                String breedFromDB = snapshot.child("breed").getValue().toString();
                String ownermailFromDB = snapshot.child("email").getValue().toString();
                String ownernameFromDB = snapshot.child("ownername").getValue().toString();

                username.setText(usernameFromDB);
                petname.setText(petnameFromDB);

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    public void setfollow_btn(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("followersList").child(MainActivity.userEnteredusername);

        Query checkUser = reference.child(userFriendName);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                if(datasnapshot.exists()){
                    follow_btn.setText("FOLLOWING");
                    follow_btn.setBackgroundColor(Color.LTGRAY);

                }
                else {
                    follow_btn.setText("FOLLOW");
                    follow_btn.setTextColor(Color.WHITE);
                    follow_btn.setBackgroundColor(Color.BLACK);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void newFriend(){
        DatabaseReference reference4 = FirebaseDatabase.getInstance().getReference("followersList");
        reference4.child(MainActivity.userEnteredusername).child(userFriendName).setValue("friend");


    }

    public void whenbtnclicked(View view){
        DatabaseReference refere = FirebaseDatabase.getInstance().getReference("followersCount").child(userFriendName).child("count");
        DatabaseReference refere2 = FirebaseDatabase.getInstance().getReference("followersCount").child(MainActivity.userEnteredusername).child("count");
        DatabaseReference refere3 = FirebaseDatabase.getInstance().getReference("followersList").child(MainActivity.userEnteredusername).child(userFriendName).child("username");
        DatabaseReference refere4 = FirebaseDatabase.getInstance().getReference("followersList").child(userFriendName).child(MainActivity.userEnteredusername).child("username");
        String text = follow_btn.getText().toString();
        if(text=="FOLLOW"){
            follow_btn.setText("FOLLOWING");
            follow_btn.setBackgroundColor(Color.GRAY);
            int val = increment(c);
            refere.setValue(val);

            int val2 = increment(c2);
            refere2.setValue(val2);

            refere3.setValue(userFriendName);
            refere4.setValue(MainActivity.userEnteredusername);
        }
        else {
            follow_btn.setText("FOLLOW");
            follow_btn.setBackgroundColor(Color.BLACK);
            follow_btn.setTextColor(Color.WHITE);
            int val = decrement(c);
            refere.setValue(val);

            int val2 = decrement(c2);
            refere2.setValue(val2);

            refere3.removeValue();
            refere4.removeValue();
        }
    }

    public void friendcount(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("followersCount").child(userFriendName);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String count = snapshot.child("count").getValue().toString();
                friendscount.setText(count);

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void loadimg(){

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("uploadcount").child(userFriendName);

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String countinstring = snapshot.child("num").getValue().toString();
                int count = Integer.parseInt(countinstring);

                for (int i = 1; i <= count; i++) { //You might have to change that slightly depending on where you want to start/end counting
                    int res = getResources().getIdentifier("img"+i, "id", getPackageName()); //This line is necessary to "convert" a string (e.g. "i1", "i2" etc.) to a resource ID
                    image[i] = (ImageView) findViewById(res);
                    //setOnclicklistener for letters[i] and whatever you would like to do.
                }
                for(int i=1;i<= count;i++){
                    String iinstr = String.valueOf(i);
                    loaduploadimg(i,iinstr);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }


    private void loaduploadimg(int i,String str) {



        FirebaseStorage storage3 = FirebaseStorage.getInstance();
        StorageReference storageReference3 = storage3.getReference().child(userFriendName).child("/images/img"+str);
        try {
            final File localfile = File.createTempFile("temp","jpeg");
            storageReference3.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            image[i].setImageBitmap(bitmap);
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

    public int increment(int cnt){
        return cnt+1;
    }

    public int decrement(int cnt){
        return cnt-1;
    }

    public void decval(){



    }



}