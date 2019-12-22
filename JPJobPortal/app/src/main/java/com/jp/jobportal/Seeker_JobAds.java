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

public class Seeker_JobAds extends Fragment {

    RecyclerView recyclerview1;
    public FirebaseAuth mAuth;
    public FirebaseUser mFirebaseUser;
    public FirebaseDatabase mFirebasePostedAds;
    public DatabaseReference postedAds, users, currentUser, jobsApplied, currentAd;
    List<PostJobDB> listData;
    public List<String> listData1;
    public List<String> adSave;
    public int i = 0;
    Seeker_Job_Ads_Adapter adapter;
    public String adID, comname, result;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.seeker_jobads, null);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mFirebasePostedAds = FirebaseDatabase.getInstance();
        postedAds = mFirebasePostedAds.getReference("Posted Jobs");

        recyclerview1 = (RecyclerView)view.findViewById(R.id.recview1);

        RecyclerView.LayoutManager LM1 = new LinearLayoutManager(getContext());
        recyclerview1.setHasFixedSize(true);
        recyclerview1.setLayoutManager(LM1);
        recyclerview1.setItemAnimator(new DefaultItemAnimator());
        recyclerview1.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        listData = new ArrayList<>();
        listData1 = new ArrayList<>();
        adSave = new ArrayList<>();

        postedAds.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot1, @Nullable String s) {

                final PostJobDB data = dataSnapshot1.getValue(PostJobDB.class);

                users = mFirebasePostedAds.getReference("Users");

                currentUser = users.child(mAuth.getUid());

                jobsApplied = currentUser.child("Jobs Applied");

                adSave.add(data.getAdID());


                jobsApplied.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        if(dataSnapshot.child(adSave.get(i)).exists()) {
                            result = "True";
                            i++;
                        }
                        else {
                            result = "False";
                            i++;
                        }
                       listData.add(data);
                       listData1.add(result);
                       adapter = new Seeker_Job_Ads_Adapter(listData, listData1);
                       recyclerview1.setAdapter(adapter);
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
}
