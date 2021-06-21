package com.example.pets;

import android.app.AppComponentFactory;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class ProfileFragment extends Fragment{
    @Nullable

    TextView username,mainpetname,mainusername,petname,ownername,email,password,logoutbtn,breed;
    String usernameFromDB,passwordFromDB,petnameFromDB,ownernameFromDB,emailFromDB,breedFromDB;
    private ImageView profilePic;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_profile,container,false);
        username = v.findViewById(R.id.username);
        mainpetname = v.findViewById(R.id.mainpetname);
        mainusername = v.findViewById(R.id.mainusername);
        petname = v.findViewById(R.id.petname);
        ownername = v.findViewById(R.id.ownername);
        email = v.findViewById(R.id.ownermail);
        password = v.findViewById(R.id.password);
        logoutbtn = v.findViewById(R.id.logoutbtn);
        breed = v.findViewById(R.id.breed);
        profilePic = v.findViewById(R.id.profileimg);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        usernameFromDB = getArguments().getString("username");
        passwordFromDB = getArguments().getString("password");
        emailFromDB = getArguments().getString("email");
        ownernameFromDB = getArguments().getString("ownername");
        petnameFromDB = getArguments().getString("petname");
        breedFromDB = getArguments().getString("breed");

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosePicture();
            }
        });

        showUserDetails();
        return  v;
    }

    private void chosePicture() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1000 && resultCode == Navigation.RESULT_OK){
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
            //uploadPicture();
        }
    }

    private void uploadPicture() {

        final ProgressDialog pd =new ProgressDialog(getActivity());
        pd.setTitle("Uploading Image...");
        pd.show();
        final String randomKey = UUID.randomUUID().toString();

// Create a reference to 'images/mountains.jpg'
        StorageReference riversRef = storageReference.child("images/"+randomKey);

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(getView().findViewById(android.R.id.content),"Image Uploaded.",Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        pd.dismiss();
                        Toast.makeText(getActivity(),"Failed to Upload",Toast.LENGTH_LONG);
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

    private void showUserDetails() {

        mainpetname.setText(petnameFromDB);
        mainusername.setText(usernameFromDB);
        petname.setText(petnameFromDB);
        ownername.setText(ownernameFromDB);
        email.setText(emailFromDB);
        password.setText(passwordFromDB);
        breed.setText(breedFromDB);
        username.setText(usernameFromDB);


    }

    private void backtologin(){
        Intent intent = new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
    }


}
