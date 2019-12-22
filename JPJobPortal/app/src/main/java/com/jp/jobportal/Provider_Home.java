package com.jp.jobportal;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class Provider_Home extends Fragment {

    private ImageView proimg;
    private FirebaseAuth mAuth;
    public FirebaseUser mFirebaseUser;
    public DatabaseReference mFirebaseDatabase;
    public DatabaseReference user;
    public TextView providercompanyNameInHome, providerEmailInHome, providercompanyIdInHome, providerMobileInHome;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.provider_home,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        user = mFirebaseDatabase.child("Users").child(mFirebaseUser.getUid());

        proimg = (ImageView) view.findViewById(R.id.providerimginhome);

        providercompanyNameInHome = (TextView) view.findViewById(R.id.providercompanynameinhome);
        providerEmailInHome = (TextView) view.findViewById(R.id.provideremailinhome);
        providercompanyIdInHome = (TextView) view.findViewById(R.id.providercompanyidinhome);
        providerMobileInHome = (TextView) view.findViewById(R.id.providermobnuminhome);

        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                providercompanyNameInHome.setText(dataSnapshot.child("pCname").getValue(String.class));
                providerEmailInHome.setText(dataSnapshot.child("pEmail").getValue(String.class));
                providercompanyIdInHome.setText(dataSnapshot.child("pCID").getValue(String.class));
                providerMobileInHome.setText(dataSnapshot.child("mobileNum").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                return;
            }
        });

        user.child("ProImgUri").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String ur = dataSnapshot.getValue(String.class);
                    Uri urrr = Uri.parse(ur);
                    Glide.with(getActivity()).load(urrr).fitCenter().into(proimg);


                } else {
                    proimg.setImageResource(R.drawable.intro_job_provider_img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
