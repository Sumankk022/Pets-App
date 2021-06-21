package com.example.pets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.temporal.ValueRange;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {


    private ImageView profilePic;
    TextInputEditText tv1;
    TextView tv2,frnds;
    RelativeLayout rv;
    FirebaseDatabase database,database2;
    DatabaseReference reference;
    Button btn;
    Bitmap bt[];
    ImageView[] image =new ImageView[100];
    public static String countinstring = "Default";
    String cnt;
    int count;
    TextView friendscount;
    String usernameFromDB;

    String userEnteredusername = MainActivity.userEnteredusername;
    TextInputEditText username,petname,ownername,email,password,breed;
    TextView mainusername,mainpetname;

    private long backPressedTime;
    private Toast backToast;


    public Uri imageUri;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseDatabase firebaseDatabase2 = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference first =databaseReference.child("image");
    private FirebaseStorage storage,storage2;
    private StorageReference storageReference,storageReference2;
    DatabaseReference reference2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mainpetname =findViewById(R.id.mainpetname);
        mainusername =findViewById(R.id.mainuser);
        petname = findViewById(R.id.petname);
        ownername = findViewById(R.id.ownername);
        email = findViewById(R.id.ownermail);
        breed = findViewById(R.id.breed);
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        btn = findViewById(R.id.update_btn);
        frnds = findViewById(R.id.friends_text);
        friendscount = findViewById(R.id.friends_num);
        rv = findViewById(R.id.friends_list);



    loadimg();
    friendcount();



    rv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ProfileActivity.this,FriendsList.class);
            startActivity(intent);
        }
    });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(userEnteredusername);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                usernameFromDB = snapshot.child("username").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        showUserDetails();

        profilePic = findViewById(R.id.pet_profile);

        loadprofilepic();

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosePicture();
            }
        });



        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_profile);


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
                        return true;
                }
                return false;
            }
        });

    }

    public void registerUser() {
        if (!ValidatepetName() | !Validatebreed() | !Validateemail() | !ValidateOwnerName() | !Validateusername() | !Validatepassword()){
            return;
        }


        String fpetname = petname.getText().toString();
        String fownername = ownername.getText().toString();
        String fusername = username.getText().toString();
        String fbreed = breed.getText().toString();
        String femail = email.getText().toString();
        String fpassword = password.getText().toString();
        UserHelperClass helperClass = new UserHelperClass(fpetname,fownername,fusername,fbreed,femail,fpassword,fpassword);
        reference.child(fusername).setValue(helperClass);
        Toast.makeText(this, "Information Updated Successfully ✔", Toast.LENGTH_SHORT).show();
    }

    private void loadprofilepic() {

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child(MainActivity.userEnteredusername).child("profilepic");
        try {
            final File localfile = File.createTempFile(userEnteredusername,"jpeg");
            storageReference.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            profilePic.setImageBitmap(bitmap);
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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(userEnteredusername);


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
                mainusername.setText(usernameFromDB);
                mainpetname.setText(petnameFromDB);
                password.setText(passwordFromDB);
                breed.setText(breedFromDB);
                email.setText(ownermailFromDB);
                ownername.setText(ownernameFromDB);

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }
    

    public void chosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
            uploadPicture();
    }

    private void uploadPicture() {

        final ProgressDialog pd =new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();
        final String randomKey = UUID.randomUUID().toString();

// Create a reference to 'images/mountains.jpg'
        storage2 = FirebaseStorage.getInstance();
        storageReference2 = storage2.getReference();
        StorageReference riversRef = storageReference2.child(""+userEnteredusername+"/profilepic");

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(findViewById(android.R.id.content),"Image Uploaded.",Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        pd.dismiss();
                        Toast.makeText(ProfileActivity.this,"Failed to Upload",Toast.LENGTH_LONG);
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

    public void backToLoginPage(View v){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }




    private Boolean ValidatepetName(){
        String val = petname.getText().toString();
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
        String val = ownername.getText().toString();
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
        String val = username.getText().toString();

        if(val.matches(userEnteredusername)){
            username.setError(null);
            return true;
        }

        else{
            username.setError("Cannot Update Username");
            return false;
        }

    }

    private Boolean Validatebreed(){
        String val = breed.getText().toString();
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
        String val = email.getText().toString();
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

    private void loadimg(){

        database = FirebaseDatabase.getInstance();
        reference2 = FirebaseDatabase.getInstance().getReference("uploadcount").child(userEnteredusername);

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                countinstring = snapshot.child("num").getValue().toString();
                count = Integer.parseInt(countinstring);

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
        StorageReference storageReference3 = storage3.getReference().child(MainActivity.userEnteredusername).child("/images/img"+str);
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
    public void friendcount(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("followersCount").child(userEnteredusername);
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




}