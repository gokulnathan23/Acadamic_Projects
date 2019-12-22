package com.jp.jobportal;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class Seeker_Home extends Fragment {

    public ImageView proimg;
    public FirebaseAuth mAuth;
    public FirebaseUser mFirebaseUser;
    public DatabaseReference mFirebaseDatabase;
    public DatabaseReference user;
    public TextView seekerNameInHome,seekerEmailInHome,seekerMobile,seekerDob, seekerGender, seekerEducation, seekerWorkExperience, seekerPreLoc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.seeker_home,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        user = mFirebaseDatabase.child("Users").child(mFirebaseUser.getUid());

        proimg = (ImageView)view.findViewById(R.id.seekerproimginhome);

        seekerNameInHome = (TextView)view.findViewById(R.id.seekernameinhome);
        seekerEmailInHome = (TextView)view.findViewById(R.id.seekeremailinhome);
        seekerMobile = (TextView)view.findViewById(R.id.seekermobnuminhome);
        seekerDob = (TextView)view.findViewById(R.id.seekerdobinhome);
        seekerGender = (TextView)view.findViewById(R.id.seekergenderinhome);
        seekerEducation = (TextView)view.findViewById(R.id.seekereducationinhome);
        seekerWorkExperience = (TextView)view.findViewById(R.id.seekerexperienceinhome);
        seekerPreLoc = (TextView)view.findViewById(R.id.seekerlocinhome);

        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                seekerNameInHome.setText(dataSnapshot.child("sFname").getValue(String.class));
                seekerEmailInHome.setText(dataSnapshot.child("sEmail").getValue(String.class));
                seekerMobile.setText(dataSnapshot.child("sMobilenum").getValue(String.class));
                seekerDob.setText(dataSnapshot.child("sDOB").getValue(String.class));
                seekerGender.setText(dataSnapshot.child("sGender").getValue(String.class));
                seekerEducation.setText(dataSnapshot.child("sEducation").getValue(String.class));
                seekerWorkExperience.setText(dataSnapshot.child("sWorkExp").getValue(String.class));
                seekerPreLoc.setText(dataSnapshot.child("sPreLoc").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                return;
            }
        });

        user.child("ProImgUri").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String ur = dataSnapshot.getValue(String.class);
                    Uri urrr = Uri.parse(ur);
                    Glide.with(getActivity()).load(urrr).fitCenter().into(proimg);


                }
                else
                {
                    proimg.setImageResource(R.drawable.intro_job_seeker_img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
