package com.jp.jobportal;

import android.app.AlertDialog;
import android.app.Dialog;
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

public class Provider_Seekers_Applied_Adapter extends RecyclerView.Adapter<Provider_Seekers_Applied_Adapter.MyViewHolder> {

    List<SeekersDB> listArray;
    List<String> listArray1;
    private View alertView;
    public int i = 0;


    public Provider_Seekers_Applied_Adapter(List<SeekersDB> listArray, List<String> listArray1) {
        this.listArray = listArray;
        this.listArray1 = listArray1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.applied_seeker_recycler, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final SeekersDB data = listArray.get(position);

        holder.seekername.setText(data.getsFname());
        holder.qualifi.setText(data.getsEducation());
        holder.experi.setText(data.getsWorkExp());
        holder.preloc.setText(data.getsPreLoc());
        holder.userid.setText(data.getUserUid());
        holder.jobid.setText(listArray1.get(i));
        i++;
    }
    @Override
    public int getItemCount() {
        return listArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        public TextView seekername, qualifi, experi, preloc, userid, jobid;
        public TextView seN, seE, seMn, seEd, seGen, seExp, seDOB, sePreLoc, jobName;
        public Button viewProBtn, selectBtn, rejectBtn;
        public String seekerID, seNa, seEm, seMnu, seEdu, seGend, seExpe, seDobb, sePreLoca, jobadID ;
        public Dialog myDialog;

        public FirebaseDatabase mFirebaseDB;
        public DatabaseReference userRef, seekerPro, jobID, postedAdsRef, jobsapplied, appliedad, comnamee;

        public MyViewHolder(@NonNull final View itemView) {

            super(itemView);

            seekername = (TextView) itemView.findViewById(R.id.seekerNameeeinas);
            qualifi = (TextView) itemView.findViewById(R.id.seekerQualiinas);
            experi = (TextView) itemView.findViewById(R.id.seekerExperinas);
            preloc = (TextView) itemView.findViewById(R.id.seekerPreLocainas);
            userid = (TextView) itemView.findViewById(R.id.idinappliedSeekers);
            jobid = (TextView) itemView.findViewById(R.id.jobidinappliedSeekers);
            viewProBtn = (Button) itemView.findViewById(R.id.viewproBtninas);


            viewProBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    seekerID = userid.getText().toString();
                    jobadID = jobid.getText().toString();

                    mFirebaseDB = FirebaseDatabase.getInstance();
                    userRef = mFirebaseDB.getReference("Users");
                    seekerPro = userRef.child(seekerID);
                    jobsapplied = seekerPro.child("Jobs Applied");

                    AlertDialog.Builder viewProAlert = new AlertDialog.Builder(itemView.getContext());
                    LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
                    alertView = inflater.inflate(R.layout.view_profile, null);

                    viewProAlert.setView(alertView);

                    selectBtn = (Button) alertView.findViewById(R.id.viewProSelectBtn);
                    rejectBtn = (Button) alertView.findViewById(R.id.viewProRejectBtn);

                    jobName = (TextView) alertView.findViewById(R.id.jobDesigna);
                    seN = (TextView) alertView.findViewById(R.id.seekernameinviewpro);
                    seE = (TextView) alertView.findViewById(R.id.seekeremailinviewpro);
                    seMn = (TextView) alertView.findViewById(R.id.seekermobnuminviewpro);
                    seDOB = (TextView) alertView.findViewById(R.id.seekerdobinviewpro);
                    seGen = (TextView) alertView.findViewById(R.id.seekergenderinviewpro);
                    seEd = (TextView) alertView.findViewById(R.id.seekereducationinviewpro);
                    seExp = (TextView) alertView.findViewById(R.id.seekerexperienceinviewpro);
                    sePreLoc = (TextView) alertView.findViewById(R.id.seekerlocinviewpro);


                    seekerPro.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            seNa = dataSnapshot.child("sFname").getValue(String.class);
                            seEm = dataSnapshot.child("sEmail").getValue(String.class);
                            seMnu = dataSnapshot.child("sMobilenum").getValue(String.class);
                            seDobb = dataSnapshot.child("sDOB").getValue(String.class);
                            seGend = dataSnapshot.child("sGender").getValue(String.class);
                            seEdu = dataSnapshot.child("sEducation").getValue(String.class);
                            seExpe = dataSnapshot.child("sWorkExp").getValue(String.class);
                            sePreLoca = dataSnapshot.child("sPreLoc").getValue(String.class);

                            seN.setText(seNa);
                            seE.setText(seEm);
                            seMn.setText(seMnu);
                            seDOB.setText(seDobb);
                            seGen.setText(seGend);
                            seEd.setText(seEdu);
                            seExp.setText(seExpe);
                            sePreLoc.setText(sePreLoca);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    postedAdsRef = mFirebaseDB.getReference("Posted Jobs");
                    jobID = postedAdsRef.child(jobid.getText().toString());

                   jobID.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                           jobName.setText(dataSnapshot.child("jobDesig").getValue(String.class));
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });

                    viewProAlert.setCancelable(true);
                    myDialog = viewProAlert.create();
                    myDialog.show();

                    selectBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            appliedad = jobsapplied.child(jobid.getText().toString());

                            appliedad.child("Result").setValue("Selected");

                            postedAdsRef = mFirebaseDB.getReference("Posted Jobs");
                            jobID = postedAdsRef.child(jobid.getText().toString());

                            jobID.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String comname = dataSnapshot.child("comname").getValue(String.class);
                                    comnamee = mFirebaseDB.getReference(comname);
                                    comnamee.child(jobid.getText().toString()).child("Applied Seekers").child(userid.getText().toString()).setValue("Selected");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });

                    rejectBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            appliedad = jobsapplied.child(jobid.getText().toString());

                            appliedad.child("Result").setValue("Rejected");

                            postedAdsRef = mFirebaseDB.getReference("Posted Jobs");
                            jobID = postedAdsRef.child(jobid.getText().toString());

                            jobID.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String comna = dataSnapshot.child("comname").getValue(String.class);
                                    comnamee = mFirebaseDB.getReference(comna);
                                    comnamee.child(jobid.getText().toString()).child("Applied Seekers").child(userid.getText().toString()).removeValue();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                }
            });
        }
    }



}
