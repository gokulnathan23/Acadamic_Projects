package com.jp.jobportal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Provider_PostNewJobs extends Fragment implements View.OnClickListener{

    public TextView jobDesig,jobTyp,jobLoc,qualification,experience,compname,compemail;
    public String AdID,jd,jt,jl,exp,quali,cn,ce;
    private FirebaseAuth mAuth;
    public FirebaseUser mFirebaseUser;
    public FirebaseDatabase mFirebasePostAdsDB;
    public DatabaseReference mFirebaseDatabase, user, postedAds, postedAdsByCompany;
    public Button postJob;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.provider_postnewjobs, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        user = mFirebaseDatabase.child("Users").child(mFirebaseUser.getUid());

        mFirebasePostAdsDB = FirebaseDatabase.getInstance();

        jobDesig = (TextView)view.findViewById(R.id.jobdesignation);
        jobDesig.setOnClickListener(this);
        jobTyp = (TextView)view.findViewById(R.id.jobtype);
        jobTyp.setOnClickListener(this);
        jobLoc = (TextView)view.findViewById(R.id.joblocation);
        jobLoc.setOnClickListener(this);
        qualification = (TextView)view.findViewById(R.id.qualification);
        qualification.setOnClickListener(this);
        experience = (TextView)view.findViewById(R.id.experience);
        experience.setOnClickListener(this);
        compname = (TextView)view.findViewById(R.id.compname);
        compemail = (TextView)view.findViewById(R.id.compemail);

        postJob = (Button) view.findViewById(R.id.postjobBtn);
        postJob.setOnClickListener(this);

        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                compname.setText(dataSnapshot.child("pCname").getValue(String.class));
                compemail.setText(dataSnapshot.child("pEmail").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {

        if(view == jobDesig)
            jobDesignation();

        if(view == jobTyp)
            jobType();

        if(view == jobLoc)
            jobLocation();

        if(view == qualification)
            qualification();

        if(view == experience)
            experience();

        if(view == postJob)
        {
            postJob();
        }

    }

    public void jobDesignation()
    {

        List<String> jobDesigItems = new ArrayList<String>();
        jobDesigItems.add("Manager");
        jobDesigItems.add("Senior Manager");
        jobDesigItems.add("Dot Net Developer");
        jobDesigItems.add("Java Developer");
        jobDesigItems.add("Android Developer");
        jobDesigItems.add("UI/UX Developer");
        jobDesigItems.add("Software Tester");
        final CharSequence[] jobDesigseq = jobDesigItems.toArray(new String[jobDesigItems.size()]);
        final AlertDialog.Builder jobDesigBuilder = new AlertDialog.Builder(getActivity());
        jobDesigBuilder.setTitle("Select Designation")
                .setItems(jobDesigseq, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        jobDesig.setText(jobDesigseq[item]);
                    }
                });
        AlertDialog alertDialog = jobDesigBuilder.create();
        alertDialog.show();
    }

    public void jobType()
    {

        List<String> jobTypeItems = new ArrayList<String>();
        jobTypeItems.add("Part-Time");
        jobTypeItems.add("Full-Time");
        final CharSequence[] jobTypeseq = jobTypeItems.toArray(new String[jobTypeItems.size()]);
        final AlertDialog.Builder jobDesigBuilder = new AlertDialog.Builder(getActivity());
        jobDesigBuilder.setTitle("Select Job Type")
                .setItems(jobTypeseq, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        jobTyp.setText(jobTypeseq[item]);
                    }
                });
        AlertDialog alertDialog = jobDesigBuilder.create();
        alertDialog.show();
    }

    public void jobLocation()
    {

        List<String> jobLocItems = new ArrayList<String>();

        jobLocItems.add("Kanchipuram");
        jobLocItems.add("Tiruvallur");
        jobLocItems.add("Cuddalore");
        jobLocItems.add("Villupuram");
        jobLocItems.add("Vellore");
        jobLocItems.add("Tiruvannamalai");
        jobLocItems.add("Salem");
        jobLocItems.add("Namakkal");
        jobLocItems.add("Dharmapuri");
        jobLocItems.add("Erode");
        jobLocItems.add("Coimbatore");
        jobLocItems.add("The Nilgiris");
        jobLocItems.add("Thanjavur");
        jobLocItems.add("Nagapattinam");
        jobLocItems.add("Tiruvarur");
        jobLocItems.add("Tiruchirappalli");
        jobLocItems.add("Karur");
        jobLocItems.add("Perambalur");
        jobLocItems.add("Pudukkottai");
        jobLocItems.add("Madurai");
        jobLocItems.add("Theni");
        jobLocItems.add("Dindigul");
        jobLocItems.add("Ramanathapuram");
        jobLocItems.add("Virudhunagar");
        jobLocItems.add("Sivagangai");
        jobLocItems.add("Tirunelveli");
        jobLocItems.add("Thoothukkudi");
        jobLocItems.add("Kanniyakumari");
        jobLocItems.add("Krishnagiri");
        jobLocItems.add("Ariyalur");
        jobLocItems.add("Tiruppur");


        final CharSequence[] jobLocseq = jobLocItems.toArray(new String[jobLocItems.size()]);
        final AlertDialog.Builder jobLocBuilder = new AlertDialog.Builder(getActivity());
        jobLocBuilder.setTitle("Select Job Location")
                .setItems(jobLocseq, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        jobLoc.setText(jobLocseq[item]);
                    }
                });
        AlertDialog alertDialog = jobLocBuilder.create();
        alertDialog.show();
    }

    public void qualification()
    {

        List<String> qualificationItems = new ArrayList<String>();
        qualificationItems.add("MCA");
        qualificationItems.add("M.Sc");
        qualificationItems.add("M.Tech");
        qualificationItems.add("MBA");
        qualificationItems.add("BCA");
        qualificationItems.add("B.Sc");
        qualificationItems.add("B.Tech");
        qualificationItems.add("BBA");
        final CharSequence[] qualificationSeq = qualificationItems.toArray(new String[qualificationItems.size()]);
        final AlertDialog.Builder qualificationBuilder = new AlertDialog.Builder(getActivity());
        qualificationBuilder.setTitle("Select Qualification")
                .setItems(qualificationSeq, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        qualification.setText(qualificationSeq[item]);
                    }
                });
        AlertDialog alertDialog = qualificationBuilder.create();
        alertDialog.show();
    }

    public void experience()
    {

        List<String> experienceItems = new ArrayList<String>();
        experienceItems.add("0 - 1 Year");
        experienceItems.add("1 - 2 Years");
        experienceItems.add("2 - 3 Years");
        experienceItems.add("3 - 4 Years");
        experienceItems.add("4 - 5 Years");
        experienceItems.add("More Than 5 Years");
        final CharSequence[] experienceseq = experienceItems.toArray(new String[experienceItems.size()]);
        final AlertDialog.Builder experienceBuilder = new AlertDialog.Builder(getActivity());
        experienceBuilder.setTitle("Select Experience")
                .setItems(experienceseq, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        experience.setText(experienceseq[item]);
                    }
                });
        AlertDialog alertDialog = experienceBuilder.create();
        alertDialog.show();
    }

    public void postJob()
    {
        postedAds = FirebaseDatabase.getInstance().getReference("Posted Jobs");
        postedAdsByCompany =  FirebaseDatabase.getInstance().getReference(compname.getText().toString());
        postedAds.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                AdID = postedAds.push().getKey();
                jd = jobDesig.getText().toString();
                jt = jobTyp.getText().toString();
                jl = jobLoc.getText().toString();
                quali = qualification.getText().toString();
                exp = experience.getText().toString();
                cn = compname.getText().toString();
                ce = compemail.getText().toString();



                PostJobDB postJobData = new PostJobDB(AdID,jd,jt,jl,quali,exp,cn,ce);
                postedAds.child(AdID).setValue(postJobData);

                postedAdsByCompany.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        jd = jobDesig.getText().toString();
                        jt = jobTyp.getText().toString();
                        jl = jobLoc.getText().toString();
                        quali = qualification.getText().toString();
                        exp = experience.getText().toString();
                        cn = compname.getText().toString();
                        ce = compemail.getText().toString();



                        PostJobDB postJobData = new PostJobDB(AdID,jd,jt,jl,quali,exp,cn,ce);
                        postedAdsByCompany.child(AdID).setValue(postJobData);

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