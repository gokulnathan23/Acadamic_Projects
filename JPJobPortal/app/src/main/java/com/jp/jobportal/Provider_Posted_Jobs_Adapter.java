package com.jp.jobportal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

class Provider_Posted_Jobs_Adapter extends RecyclerView.Adapter<Provider_Posted_Jobs_Adapter.MyViewHolder>
{
    public TextView jobDe, jobTy, jobLo, qualifi, exper;
    List<PostJobDB> listArray;
    private View alertView;


    public Provider_Posted_Jobs_Adapter(List list) {
        this.listArray = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.provider_posted_jobs_recycler, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        PostJobDB data = listArray.get(position);
        holder.jobDesig.setText(data.getJobDesig());
        holder.experien.setText(data.getExperien());
        holder.loc.setText(data.getJobLoc());
        holder.cn.setText(data.getComname());
        holder.id.setText(data.getAdID());
    }
    @Override
    public int getItemCount()
    {
        return listArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView jobDesig, experien, loc, cn, id;
        public Button editBtn, deleteBtn;
        public AlertDialog myDialog;
        String idd;

        public FirebaseDatabase fbDB;
        public DatabaseReference rootref, postedadsbyID, rootref1, postedAdsByCompany;


        public MyViewHolder(final View itemView) {
            super(itemView);

            jobDesig = (TextView) itemView.findViewById(R.id.jobDesiginpostedads);
            experien = (TextView) itemView.findViewById(R.id.expinpostedads);
            loc = (TextView) itemView.findViewById(R.id.joblocinpostedads);
            cn = (TextView) itemView.findViewById(R.id.cninpostedads);
            id = (TextView) itemView.findViewById(R.id.idinpostedads);
            editBtn = (Button) itemView.findViewById(R.id.editBtninpostedads);
            deleteBtn = (Button) itemView.findViewById(R.id.deleteBtninpostedads);

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    idd = id.getText().toString();

                    fbDB = FirebaseDatabase.getInstance();
                    rootref = fbDB.getReference("Posted Jobs");
                    postedadsbyID = rootref.child(idd);

                    postedadsbyID.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            AlertDialog.Builder editJobAlert = new AlertDialog.Builder(itemView.getContext());
                            LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
                            alertView = inflater.inflate(R.layout.edit_posted_job, null);

                            editJobAlert.setView(alertView)
                                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            postedadsbyID.child("jobDesig").setValue(jobDe.getText());
                                            postedadsbyID.child("jobTyp").setValue(jobTy.getText());
                                            postedadsbyID.child("jobLoc").setValue(jobLo.getText());
                                            postedadsbyID.child("qualifica").setValue(qualifi.getText());
                                            postedadsbyID.child("experien").setValue(exper.getText());

                                        }
                                    });
                            editJobAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            editJobAlert.setCancelable(false);
                            myDialog = editJobAlert.create();
                            myDialog.show();

                            jobDe = (TextView) alertView.findViewById(R.id.jobdesignationineditad);
                            jobDe.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    jobDesignation();
                                }
                            });
                            jobTy = (TextView) alertView.findViewById(R.id.jobtypeineditad);
                            jobTy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    jobType();
                                }
                            });
                            jobLo = (TextView) alertView.findViewById(R.id.joblocationineditad);
                            jobLo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    jobLocation();
                                }
                            });
                            qualifi = (TextView) alertView.findViewById(R.id.qualificationineditad);
                            qualifi.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    qualification();
                                }
                            });
                            exper = (TextView) alertView.findViewById(R.id.experienceineditad);
                            exper.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    experience();
                                }
                            });

                            jobDe.setText(dataSnapshot.child("jobDesig").getValue(String.class));
                            jobTy.setText(dataSnapshot.child("jobTyp").getValue(String.class));
                            jobLo.setText(dataSnapshot.child("jobLoc").getValue(String.class));
                            qualifi.setText(dataSnapshot.child("qualifica").getValue(String.class));
                            exper.setText(dataSnapshot.child("experien").getValue(String.class));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idd = id.getText().toString();
                    fbDB = FirebaseDatabase.getInstance();
                    rootref = fbDB.getReference("Posted Jobs");
                    postedadsbyID = rootref.child(idd);

                    rootref1 = fbDB.getReference(cn.getText().toString());
                    postedAdsByCompany = rootref1.child(idd);

                    postedAdsByCompany.removeValue();
                    postedadsbyID.removeValue();
                    AlertDialog.Builder adB = new AlertDialog.Builder(itemView.getContext());
                    adB.setView(alertView)
                            .setTitle("Job Delete")
                            .setMessage("Posted Job Removed Succesfully")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setCancelable(false);
                    myDialog = adB.create();
                    myDialog.show();

                }
            });
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
        final android.support.v7.app.AlertDialog.Builder jobDesigBuilder = new android.support.v7.app.AlertDialog.Builder(alertView.getContext());
        jobDesigBuilder.setTitle("Select Designation")
                .setItems(jobDesigseq, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        jobDe.setText(jobDesigseq[item]);
                    }
                });
        android.support.v7.app.AlertDialog alertDialog = jobDesigBuilder.create();
        alertDialog.show();
    }

    public void jobType()
    {

        List<String> jobTypeItems = new ArrayList<String>();
        jobTypeItems.add("Part-Time");
        jobTypeItems.add("Full-Time");
        final CharSequence[] jobTypeseq = jobTypeItems.toArray(new String[jobTypeItems.size()]);
        final android.support.v7.app.AlertDialog.Builder jobDesigBuilder = new android.support.v7.app.AlertDialog.Builder(alertView.getContext());
        jobDesigBuilder.setTitle("Select Job Type")
                .setItems(jobTypeseq, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        jobTy.setText(jobTypeseq[item]);
                    }
                });
        android.support.v7.app.AlertDialog alertDialog = jobDesigBuilder.create();
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
        final android.support.v7.app.AlertDialog.Builder jobLocBuilder = new android.support.v7.app.AlertDialog.Builder(alertView.getContext());
        jobLocBuilder.setTitle("Select Job Location")
                .setItems(jobLocseq, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        jobLo.setText(jobLocseq[item]);
                    }
                });
        android.support.v7.app.AlertDialog alertDialog = jobLocBuilder.create();
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
        final android.support.v7.app.AlertDialog.Builder qualificationBuilder = new android.support.v7.app.AlertDialog.Builder(alertView.getContext());
        qualificationBuilder.setTitle("Select Qualification")
                .setItems(qualificationSeq, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        qualifi.setText(qualificationSeq[item]);
                    }
                });
        android.support.v7.app.AlertDialog alertDialog = qualificationBuilder.create();
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
        final android.support.v7.app.AlertDialog.Builder experienceBuilder = new android.support.v7.app.AlertDialog.Builder(alertView.getContext());
        experienceBuilder.setTitle("Select Experience")
                .setItems(experienceseq, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        exper.setText(experienceseq[item]);
                    }
                });
        android.support.v7.app.AlertDialog alertDialog = experienceBuilder.create();
        alertDialog.show();
    }
}