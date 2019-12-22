package com.jp.jobportal;

import android.app.AlertDialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Seeker_Job_Ads_Adapter extends RecyclerView.Adapter<Seeker_Job_Ads_Adapter.MyViewHolder> {



    public TextView jobDe, jobTy, jobLo, qualifi, exper, cna, cem;
    List<PostJobDB> listArray;
    List<String> listArray1;
    public View alertView;
    public int i=0;

    public Seeker_Job_Ads_Adapter(List list, List list1) {
        this.listArray = list;
        this.listArray1 = list1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seeker_job_ads_recyler, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PostJobDB data = listArray.get(position);

        holder.jobDesig.setText(data.getJobDesig());
        holder.experien.setText(data.getExperien());
        holder.loc.setText(data.getJobLoc());
        holder.cn.setText(data.getComname());
        holder.id.setText(data.getAdID());


        if(listArray1.get(i).equals("True"))
        {
            holder.viewBtn.setEnabled(false);
            holder.viewBtn.setText("Applied");
            i++;
        }
        else if(listArray1.get(i).equals("False"))
        {
            holder.viewBtn.setEnabled(true);
            holder.viewBtn.setText("View");
            i++;
        }

    }

    @Override
    public int getItemCount() {
        return listArray.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView jobDesig, experien, loc, cn, id;

        public AlertDialog myDialog;
        public String idd;
        public Button viewBtn, aplyBtn, rejBtn;
        public FirebaseDatabase fbDB;
        public DatabaseReference rootref, userref, userID, postedadsbyID, appliedseekers, jobsapplied, appliedjobID, appliedseekerid;


        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        public MyViewHolder(final View itemView) {
            super(itemView);

            jobDesig = (TextView) itemView.findViewById(R.id.jobDesiginjobsads);
            experien = (TextView) itemView.findViewById(R.id.expinjobsads);
            loc = (TextView) itemView.findViewById(R.id.joblocinjobsads);
            cn = (TextView) itemView.findViewById(R.id.cninjobsads);
            id = (TextView) itemView.findViewById(R.id.idinjobsads);
            viewBtn = (Button) itemView.findViewById(R.id.viewBtninjobsads);


            idd = id.getText().toString();
            fbDB = FirebaseDatabase.getInstance();
            rootref = fbDB.getReference("Posted Jobs");
            postedadsbyID = rootref.child(idd);

            viewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    idd = id.getText().toString();
                    fbDB = FirebaseDatabase.getInstance();
                    rootref = fbDB.getReference("Posted Jobs");
                    postedadsbyID = rootref.child(idd);

                    postedadsbyID.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            AlertDialog.Builder editJobAlert = new AlertDialog.Builder(itemView.getContext());
                            LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
                            alertView = inflater.inflate(R.layout.view_jobs_ads, null);

                            editJobAlert.setView(alertView)
                                    .setCancelable(false);
                            myDialog = editJobAlert.create();
                            myDialog.show();

                            jobDe = (TextView) alertView.findViewById(R.id.jobdesignationinviewad);
                            jobTy = (TextView) alertView.findViewById(R.id.jobtypeinviewad);
                            jobLo = (TextView) alertView.findViewById(R.id.joblocationinviewad);
                            qualifi = (TextView) alertView.findViewById(R.id.qualificationinviewad);
                            exper = (TextView) alertView.findViewById(R.id.experienceinviewad);
                            cna = (TextView) alertView.findViewById(R.id.comnameinviewad);
                            cem = (TextView) alertView.findViewById(R.id.compemailinviewad);
                            aplyBtn = (Button) alertView.findViewById(R.id.viewadAplyBtn);
                            rejBtn = (Button) alertView.findViewById(R.id.viewadDelBtn);

                            postedadsbyID.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    jobDe.setText(dataSnapshot.child("jobDesig").getValue(String.class));
                                    jobTy.setText(dataSnapshot.child("jobTyp").getValue(String.class));
                                    jobLo.setText(dataSnapshot.child("jobLoc").getValue(String.class));
                                    qualifi.setText(dataSnapshot.child("qualifica").getValue(String.class));
                                    exper.setText(dataSnapshot.child("experien").getValue(String.class));
                                    cna.setText(dataSnapshot.child("comname").getValue(String.class));
                                    cem.setText(dataSnapshot.child("comemail").getValue(String.class));


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            aplyBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    rootref = fbDB.getReference(cna.getText().toString());
                                    postedadsbyID = rootref.child(idd);
                                    appliedseekers = postedadsbyID.child("Applied Seekers");
                                    appliedseekerid = appliedseekers.child(mAuth.getUid());
                                    appliedseekerid.child("Result").setValue("Waiting List");
                                    userref = fbDB.getReference("Users");
                                    userID = userref.child(mAuth.getUid());
                                    jobsapplied = userID.child("Jobs Applied");
                                    appliedjobID = jobsapplied.child(idd);
                                    appliedjobID.child("Result").setValue("Waiting List");
                                    myDialog.cancel();
                                    Toast.makeText(itemView.getContext(), "Job Applied Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });

                            rejBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialog.cancel();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
    }
}