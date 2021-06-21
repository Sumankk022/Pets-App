package com.example.pets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsList extends AppCompatActivity {

    ListView lv1;
    String username =MainActivity.userEnteredusername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        lv1 = findViewById(R.id.list);

        ArrayList<String> list = new ArrayList<String>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.friends_list,list);
        lv1.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("followersList").child(username);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    list.add(ds.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_item= String.valueOf(adapterView.getItemAtPosition(i));
                Intent intent = new Intent(getApplicationContext(), friendProfile.class);
                intent.putExtra("userFriendName",selected_item);
                startActivity(intent);
            }
        });


    }
}