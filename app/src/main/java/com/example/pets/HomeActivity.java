package com.example.pets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    public static String username11;
    RecyclerView recyclerView;
    ProgramAdapter programAdapter;
    RecyclerView.LayoutManager layoutManager;
    String[] programNameList = new String[10];
    String[] programDescList = new String[100] ;
    int[] images = {R.drawable.img1, R.drawable.img2,R.drawable.img3};
    Bitmap[] uploadimg = new Bitmap[100];
    TextView tv;
    ArrayList<String> arrayList;
    ArrayList<Integer> arratLike;
    String[] strings;
    ImageView imga;
    TextView[] textViews =new TextView[100];
    int c=0;
    ImageView[] img = new ImageView[100];
    TextView[] username = new TextView[100];
    Bitmap[] bitmaps = new Bitmap[100];
    String[] likecount = new String[100];
    String[] lc = {"5","2","4","2","3","4","5","6","0","5","4"};

    private long backPressedTime;
    private Toast backToast;
    int l=0;

    private FirebaseStorage storage,storage2;
    private StorageReference storageReference,storageReference2;
    String userEnteredusername = MainActivity.userEnteredusername;

    ImageView profilePic;
    ListView id1;

    int n=0;
    String ns;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("followersList").child(userEnteredusername);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.rvProgram);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation);
        profilePic = findViewById(R.id.pet_profile);

        tv=findViewById(R.id.tv1);

//        img[1] = findViewById(R.id.img1);
//        img[2] = findViewById(R.id.img2);
//        img[3] = findViewById(R.id.img3);
//        img[4] = findViewById(R.id.img4);
//        img[5] = findViewById(R.id.img5);
//        img[6] = findViewById(R.id.img6);
//
//        username[1] = findViewById(R.id.username1);
//        username[2] = findViewById(R.id.username2);
//        username[3] = findViewById(R.id.username3);
//        username[4] = findViewById(R.id.username4);
//        username[5] = findViewById(R.id.username5);
//        username[6] = findViewById(R.id.username6);
//
//        for(int i=1;i<7;i++){
//            username[i].setText("Margie@123");
//        }


        arrayList = new ArrayList<String>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    arrayList.add(ds.getKey());
                }
                for(int i=0;i<arrayList.size();i++){
                    display(arrayList.get(i));
                }

//                String m = String.valueOf(arrayList.size());
//                tv.setText(m);




            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        loadprofilepic();



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

    private void getlikecount(String frnd){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("uploadcount").child(frnd);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("likeCount").child(frnd);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ns = snapshot.child("num").getValue().toString();
                n = Integer.parseInt(ns);
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot ds) {
                        for (int i=1;i<=n;i++){
                            String str = ds.child("img"+i).getValue().toString();
                            int k = i-1;
                            lc[l]=str;
                            l=increment(l);

                        }
//                        l = incrapid(l,n);
//                        tv.setText(lc[2]);

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void display(String frnd) {
        storage = FirebaseStorage.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("uploadcount").child(frnd);

        

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ns = snapshot.child("num").getValue().toString();
                n = Integer.parseInt(ns);

                for(int i=1;i<=n;i++){
                    storageReference = storage.getReference().child(frnd).child("images").child("/img"+i);try {
                        final File localfile = File.createTempFile(userEnteredusername,"jpeg");
                        int finalI = i;
                        int finalI1 = i;
                        int finalI2 = i;
                        storageReference.getFile(localfile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                                bitmaps[c] = bitmap;
                                                programNameList[c] = frnd;
                                                programDescList[c] = frnd;

                                                programAdapter = new ProgramAdapter(HomeActivity.this,programNameList,programDescList,bitmaps,lc);
                                                recyclerView.setAdapter(programAdapter);

                                                c = increment(c);

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

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        
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

    private void getAllUsers(){

    }


    public int increment(int cnt){
        return cnt+1;
    }
    public int incrapid(int i,int n){return i+n;}

    public void onBackPressed() {

        if(backPressedTime+2000>System.currentTimeMillis()){
            backToast.cancel();
            finish();
            return;
        }else {
            backToast = Toast.makeText(getApplicationContext(),"Press back again to exit",Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }
}