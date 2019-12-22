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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Provider_SeekersApplied extends Fragment {


    public FirebaseAuth mAuth1;
    public FirebaseDatabase fbDB;
    public DatabaseReference users,providerData, postedAds,providerComp, providercurrentAd, appliedSeeker, jobsapplied, appliedad;
    public String providerCompName;
    public String appliedSeekerID,adId;

    public RecyclerView recyview;
    public List<SeekersDB> listData;
    public List<String> listData1;
    public Provider_Seekers_Applied_Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.provider_appliedseekers, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mAuth1 = FirebaseAuth.getInstance();
        fbDB = FirebaseDatabase.getInstance();
        users = fbDB.getReference("Users");

        providerData = users.child(mAuth1.getUid());

        recyview = (RecyclerView) view.findViewById(R.id.recviewappliedseekers);

        RecyclerView.LayoutManager LM1 = new LinearLayoutManager(getContext());
        recyview.setHasFixedSize(true);
        recyview.setLayoutManager(LM1);
        recyview.setItemAnimator(new DefaultItemAnimator());
        recyview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        listData = new ArrayList<>();
        listData1 = new ArrayList<>();

        providerData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                providerCompName = dataSnapshot.child("pCname").getValue(String.class);
                providerComp = fbDB.getReference(providerCompName);

                providerComp.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(final DataSnapshot dataSnapshot1, @Nullable String s) {



                        providercurrentAd = providerComp.child(dataSnapshot1.getKey()).child("Applied Seekers");

                        providercurrentAd.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                appliedSeekerID = dataSnapshot.getKey();

                                appliedSeeker = users.child(appliedSeekerID);

                                appliedSeeker.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(final DataSnapshot dataSnapshot2) {

                                        jobsapplied = appliedSeeker.child("Jobs Applied");

                                        jobsapplied.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                adId = dataSnapshot1.getKey();
                                                if(dataSnapshot.child(adId).exists()) {

                                                    listData1.add(dataSnapshot.child(adId).getKey());

                                                    SeekersDB data = dataSnapshot2.getValue(SeekersDB.class);

                                                    adapter = new Provider_Seekers_Applied_Adapter(listData, listData1);
                                                    listData.add(data);

                                                    recyview.setAdapter(adapter);
                                                }
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
