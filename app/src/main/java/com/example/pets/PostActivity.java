package com.example.pets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class PostActivity extends AppCompatActivity {

    public Uri imageUri;
    String username = MainActivity.userEnteredusername;
    FirebaseDatabase database;
    DatabaseReference reference,reference2;
    String countinstring;
    public static int count;
    String cnt;
    TextView tv;
    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        tv = findViewById(R.id.tv);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("uploadcount");
        reference2 = database.getReference("uploadcount").child(username);

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                countinstring = snapshot.child("num").getValue().toString();
                count = Integer.parseInt(countinstring);
                cnt = String.valueOf(count+1);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation);


        bottomNavigationView.setSelectedItemId(R.id.nav_addpost);

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
                        Intent fintent = new Intent(getApplicationContext(),FriendsActivity.class);
                        startActivity(fintent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_addpost:
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

    public void chosePicture(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = data.getData();
        uploadPicture();
    }

    private void uploadPicture() {

        final ProgressDialog pd =new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();
        final String randomKey = UUID.randomUUID().toString();



// Create a reference to 'images/mountains.jpg'
        FirebaseStorage storage2 = FirebaseStorage.getInstance();
        StorageReference storageReference2 = storage2.getReference();
        StorageReference riversRef = storageReference2.child(""+username+"/images/img"+cnt);

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(findViewById(android.R.id.content),"Image Uploaded.",Snackbar.LENGTH_LONG).show();

                        ImageUploadcount imageUploadcount = new ImageUploadcount(count);
                        reference.child(username).setValue(imageUploadcount);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        pd.dismiss();
                        Toast.makeText(PostActivity.this,"Failed to Upload",Toast.LENGTH_LONG);
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                        pd.setMessage("Percentage: "+(int)progressPercent+"%");
                    }
                });
    }
}