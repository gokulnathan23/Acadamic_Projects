package com.jp.jobportal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class Seeker_JobStatus extends Fragment {

    public FirebaseAuth mAuth1;
    public FirebaseDatabase mFirebaseDB ;
    public DatabaseReference usersRef,userDB, userJobsApplied, postedJobs, jobAdId, currAd;
    public String currentUserID, jobAdID, result;

    public RecyclerView recyview1;
    public List<PostJobDB> listData;
    private List<String> listData1;
    public Seeker_Job_Status_Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.seeker_jobstatus, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth1 = FirebaseAuth.getInstance();

        currentUserID = mAuth1.getUid();
        mFirebaseDB = FirebaseDatabase.getInstance();
        usersRef = mFirebaseDB.getReference("Users");
        userDB = usersRef.child(currentUserID);
        userJobsApplied = userDB.child("Jobs Applied");
        postedJobs = mFirebaseDB.getReference("Posted Jobs");

        recyview1 = (RecyclerView) view.findViewById(R.id.recviewjs);

        RecyclerView.LayoutManager LM1 = new LinearLayoutManager(getContext());
        recyview1.setHasFixedSize(true);
        recyview1.setLayoutManager(LM1);
        recyview1.setItemAnimator(new DefaultItemAnimator());
        recyview1.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        listData = new ArrayList<>();
        listData1 = new ArrayList<>();



        userJobsApplied.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                jobAdID = dataSnapshot.getKey();


                jobAdId = postedJobs.child(jobAdID);

                jobAdId.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot1) {

                        if(dataSnapshot1.exists())
                        {
                            currAd = userJobsApplied.child(dataSnapshot1.getKey());

                            currAd.child("Result").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    final PostJobDB data = dataSnapshot1.getValue(PostJobDB.class);
                                    result = dataSnapshot.getValue(String.class);
                                    listData.add(data);
                                    listData1.add(result);
                                    adapter = new Seeker_Job_Status_Adapter(listData, listData1);
                                    recyview1.setAdapter(adapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        else
                        {
                            userJobsApplied.child(dataSnapshot1.getKey()).removeValue();
                        }
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
                Log.e("Data snapshot error",""+databaseError);
            }
        });


    }
}
