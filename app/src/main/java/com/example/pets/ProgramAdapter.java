package com.example.pets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ViewHolder> {

    Context context;
    String[] programNameList;
    String[] programDescList;
    Bitmap[] images;
    String[] likecount;
    FirebaseDatabase database;
    String like="Like";
    int likecnt = 0;

    String username;
    String cnt;
    int c;


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView rowName;
        TextView rowDesc;
        ImageView rowimage;
        public TextView rowLike;
        ImageView img;
        TextView rowlikecount;
        public ViewHolder(View itemView,String[] prgramNameList) {
            super(itemView);
            rowName = itemView.findViewById(R.id.textview1);
            rowDesc = itemView.findViewById(R.id.textview2);
            rowimage = itemView.findViewById(R.id.imageView);
            rowLike = itemView.findViewById(R.id.textview3);
            img = itemView.findViewById(R.id.pet_profile);
            rowlikecount = itemView.findViewById(R.id.textview4);
            rowLike.setTextColor(Color.BLACK);
            //rowlikecount.setText(0);

//            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("likeCount");
//            reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//                    String user = prgramNameList[getAdapterPosition()];
//                    int c = getAdapterPosition()+1;
//                    String a =String.valueOf(c);
//                    String val = "img"+a;
//                    //String s = snapshot.child("markus0123").child("img1").getValue(String.class);
//
//                    rowlikecount.setText(val);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//
//                }
//            });


            rowLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("likeCount");
                    if(rowLike.getText()=="Like"){
                        rowLike.setText("Liked");
                        rowLike.setTextColor(Color.BLUE);
                        //String cn = rowlikecount.getText().toString();
                        //int n = Integer.parseInt(cn);
                        String st = rowlikecount.getText().toString();
                        int n = Integer.valueOf(st);
                        rowlikecount.setText(String.valueOf(n+1));
                        //reference.child(su).child("img]1").setValue(count-1);
                    }
                    else if(rowLike.getText()=="Liked"){
                        rowLike.setText("Like");
                        rowLike.setTextColor(Color.BLACK);
//                        String cn = rowlikecount.getText().toString();
//                        int n = Integer.parseInt(cn);
                        String st = rowlikecount.getText().toString();
                        int n = Integer.valueOf(st);
                        rowlikecount.setText(String.valueOf(n-1));
                        //reference.child(su).child("img]1").setValue(count+1);

                    }



                }
            });


        }


    }

    public ProgramAdapter (Context context, String[] programNameList, String[] programDescList, Bitmap[] images,String[] likecount) {
        this.context = context;
        this.programNameList = programNameList;
        this.images = images;
        this.programDescList = programDescList;
        this.likecount = likecount;
    }


    @Override
    public ProgramAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view,programNameList);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProgramAdapter.ViewHolder holder, int position) {
            if(programNameList[position]!=null){
                holder.rowName.setText(programNameList[position]);
                holder.rowimage.setImageBitmap(images[position]);
                holder.rowLike.setText(like);
                holder.rowlikecount.setText(likecount[position]);
                holder.img.setImageResource(R.drawable.profile_photo);
            }
            return;



    }
    @Override
    public int getItemCount() {
        return programNameList.length;
    }


}
