package com.jp.jobportal;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Provider_PostedJobs extends Fragment {

    RecyclerView recyclerview;
    public FirebaseAuth mAuthh;
    public FirebaseUser mFirebaseUser;
    public FirebaseDatabase mFirebasePostedAds;
    public DatabaseReference postedAdsByCompany,userComName,users;
    List<PostJobDB> listData;
    Provider_Posted_Jobs_Adapter adapter;
    public String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.provider_postedjobs, null);
    }

    public String comnameee;

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuthh = FirebaseAuth.getInstance();
        mFirebasePostedAds = FirebaseDatabase.getInstance();


        userId = mAuthh.getUid();
        users = mFirebasePostedAds.getReference("Users");
        userComName = users.child(userId);




        userComName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                comnameee = dataSnapshot.child("pCname").getValue(String.class);

                postedAdsByCompany = mFirebasePostedAds.getReference(comnameee);

                recyclerview = (RecyclerView) view.findViewById(R.id.recview);

                RecyclerView.LayoutManager LM = new LinearLayoutManager(getContext());
                recyclerview.setHasFixedSize(true);
                recyclerview.setLayoutManager(LM);
                recyclerview.setItemAnimator(new DefaultItemAnimator());
                recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

                listData = new ArrayList<>();


                postedAdsByCompany.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        final PostJobDB data = dataSnapshot.getValue(PostJobDB.class);

                        adapter = new Provider_Posted_Jobs_Adapter(listData);
                        listData.add(data);

                        recyclerview.setAdapter(adapter);

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
