package com.jp.jobportal;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

final public class Seeker_EditProfile extends Fragment implements View.OnClickListener{

    public EditText sname,semail,smobile;
    public TextView sdob,sgender,seducation,sworkexp,spreloc;
    public String sName,sEmail,sMobile,sDob,sGender,sEducation,sWorkexp,sPreloc;
    public Button saveInEditProfile,seekerproimageedit;
    String gen;
    public SimpleDateFormat dateFormatter;
    public Calendar c;
    public DatePickerDialog selectDob;
    public int mYear,mMonth,mDay;
    private static final int SELECT_PHOTO = 100;
    Uri proImgURI;
    String proImgUri;
    public ImageView proimg;
    public UploadTask uploadTask;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFDB;
    public FirebaseUser mFirebaseUser;
    public DatabaseReference mFirebaseDatabase;
    public DatabaseReference user, userDB;
    public FirebaseStorage mFirebaseStorage;
    public StorageReference mStorageRef,imageRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.seeker_editprofile, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        mFDB = FirebaseDatabase.getInstance();
        user = mFirebaseDatabase.child("Users").child(mFirebaseUser.getUid());


        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageRef = mFirebaseStorage.getReference();
        imageRef = mStorageRef.child(mAuth.getCurrentUser().getUid()).child("proimg");

        proimg = (ImageView) view.findViewById(R.id.seekerproimgineditprofile);


        saveInEditProfile = (Button) view.findViewById(R.id.saveprofile);
        saveInEditProfile.setOnClickListener(this);
        seekerproimageedit = (Button) view.findViewById(R.id.editproimgineditprofile);
        seekerproimageedit.setOnClickListener(this);

        sname = (EditText) view.findViewById(R.id.seekernameineditprofile);
        semail = (EditText) view.findViewById(R.id.seekeremailineditprofile);

        smobile = (EditText) view.findViewById(R.id.seekermobnumineditprofile);
        sdob = (TextView) view.findViewById(R.id.seekerdobineditprofile);
        sdob.setOnClickListener(this);
        sgender = (TextView) view.findViewById(R.id.seekergenderineditprofile);
        sgender.setOnClickListener(this);
        seducation = (TextView) view.findViewById(R.id.seekerqualificationineditprofile);
        seducation.setOnClickListener(this);
        sworkexp = (TextView) view.findViewById(R.id.seekerexperienceineditprofile);
        sworkexp.setOnClickListener(this);
        spreloc = (TextView) view.findViewById(R.id.seekerlocineditprofile);
        spreloc.setOnClickListener(this);

        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sName = dataSnapshot.child("sFname").getValue(String.class);
                sname.setText(sName);
                sEmail = dataSnapshot.child("sEmail").getValue(String.class);
                semail.setText(sEmail);
                sMobile = dataSnapshot.child("sMobilenum").getValue(String.class);
                smobile.setText(sMobile);
                sDob = dataSnapshot.child("sDOB").getValue(String.class);
                sdob.setText(sDob);
                sGender = dataSnapshot.child("sGender").getValue(String.class);
                sgender.setText(sGender);
                sEducation = dataSnapshot.child("sEducation").getValue(String.class);
                seducation.setText(sEducation);
                sWorkexp = dataSnapshot.child("sWorkExp").getValue(String.class);
                sworkexp.setText(sWorkexp);
                sPreloc = dataSnapshot.child("sPreLoc").getValue(String.class);
                spreloc.setText(sPreloc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        user.child("ProImgUri").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Uri urrr = Uri.parse(dataSnapshot.getValue().toString());
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


    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view)
    {
        if(view == saveInEditProfile )
        {
            sName = sname.getText().toString();
            sEmail = semail.getText().toString().trim();
            sMobile = smobile.getText().toString();
            sDob = sdob.getText().toString();
            sGender = sgender.getText().toString();
            sEducation = seducation.getText().toString();
            sWorkexp = sworkexp.getText().toString();
            sPreloc = spreloc.getText().toString();

            user.child("sFname").setValue(sName);
            user.child("sEmail").setValue(sEmail);
            user.child("sMobilenum").setValue(sMobile);
            user.child("sDOB").setValue(sDob);
            user.child("sGender").setValue(sGender);
            user.child("sEducation").setValue(sEducation);
            user.child("sWorkExp").setValue(sWorkexp);
            user.child("sPreLoc").setValue(sPreloc);

            sname.clearFocus();
            semail.clearFocus();
            smobile.clearFocus();

        }
        if(view == sgender)
        {
            genderSelect();
        }
        if(view == sdob)
        {
            dobSelect();
        }
        if(view == seducation)
        {
            eduSelect();
        }
        if(view == sworkexp)
        {
            expSelect();
        }
        if(view == spreloc)
        {
            prelocSelect();
        }
        if(view == seekerproimageedit)
        {
            selectImage(view);
        }


    }
    public void genderSelect()
    {

        List<String> genderItems = new ArrayList<String>();
        genderItems.add("Male");
        genderItems.add("Female");
        final CharSequence[] genderItemSeq = genderItems.toArray(new String[genderItems.size()]);
        AlertDialog.Builder genderSelect = new AlertDialog.Builder(getActivity());
        genderSelect.setTitle("Select Gender")
                .setItems(genderItemSeq, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        gen = genderItemSeq[item].toString();
                        sgender.setText(gen);
                    }
                });
        AlertDialog alertDialog = genderSelect.create();
        alertDialog.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void dobSelect()
    {
        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        selectDob = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                sdob.setText(mDay + "/" + (mMonth +1) + "/" + mYear);
            }
        },mYear,mMonth,mDay);
        selectDob.show();
    }

    public void expSelect()
    {

        List<String> genderItems = new ArrayList<String>();
        genderItems.add("0 - 1 Year");
        genderItems.add("1 - 2 Years");
        genderItems.add("2 - 3 Years");
        genderItems.add("3 - 4 Years");
        genderItems.add("4 - 5 Years");
        genderItems.add("More Than 5 Years");
        final CharSequence[] genderItemSeq = genderItems.toArray(new String[genderItems.size()]);
        AlertDialog.Builder expSelect = new AlertDialog.Builder(getActivity());
        expSelect.setTitle("Select Experience")
                .setItems(genderItemSeq, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        gen = genderItemSeq[item].toString();
                        sworkexp.setText(gen);
                    }
                });
        AlertDialog alertDialog = expSelect.create();
        alertDialog.show();
    }
    public void eduSelect()
    {

        List<String> eduItems = new ArrayList<String>();
        eduItems.add("ECE");
        eduItems.add("EEE");
        eduItems.add("CSE");
        eduItems.add("IT");
        eduItems.add("Mechanical");
        eduItems.add("Civil");
        final CharSequence[] eduItemsSeq = eduItems.toArray(new String[eduItems.size()]);
        AlertDialog.Builder eduSelect = new AlertDialog.Builder(getActivity());
        eduSelect.setTitle("Select Qualification")
                .setItems(eduItemsSeq, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        gen = eduItemsSeq[item].toString();
                        seducation.setText(gen);
                    }
                });
        AlertDialog alertDialog = eduSelect.create();
        alertDialog.show();
    }
    public void prelocSelect()
    {

        List<String> prelocItems = new ArrayList<String>();


        prelocItems.add("Kanchipuram");
        prelocItems.add("Tiruvallur");
        prelocItems.add("Cuddalore");
        prelocItems.add("Villupuram");
        prelocItems.add("Vellore");
        prelocItems.add("Tiruvannamalai");
        prelocItems.add("Salem");
        prelocItems.add("Namakkal");
        prelocItems.add("Dharmapuri");
        prelocItems.add("Erode");
        prelocItems.add("Coimbatore");
        prelocItems.add("The Nilgiris");
        prelocItems.add("Thanjavur");
        prelocItems.add("Nagapattinam");
        prelocItems.add("Tiruvarur");
        prelocItems.add("Tiruchirappalli");
        prelocItems.add("Karur");
        prelocItems.add("Perambalur");
        prelocItems.add("Pudukkottai");
        prelocItems.add("Madurai");
        prelocItems.add("Theni");
        prelocItems.add("Dindigul");
        prelocItems.add("Ramanathapuram");
        prelocItems.add("Virudhunagar");
        prelocItems.add("Sivagangai");
        prelocItems.add("Tirunelveli");
        prelocItems.add("Thoothukkudi");
        prelocItems.add("Kanniyakumari");
        prelocItems.add("Krishnagiri");
        prelocItems.add("Ariyalur");
        prelocItems.add("Tiruppur");


        final CharSequence[] prelocItemsSeq = prelocItems.toArray(new String[prelocItems.size()]);
        AlertDialog.Builder prelocSelect = new AlertDialog.Builder(getActivity());
        prelocSelect.setTitle("Select Experience")
                .setItems(prelocItemsSeq, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        gen = prelocItemsSeq[item].toString();
                        spreloc.setText(gen);
                    }
                });
        AlertDialog alertDialog = prelocSelect.create();
        alertDialog.show();
    }

    public void selectImage(View view)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    proImgURI = imageReturnedIntent.getData();
                    uploadImage();
                }
        }
    }
    public void uploadImage()
    {
        uploadTask = imageRef.putFile(proImgURI);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
            }
        });
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Uri urr = proImgURI;
                user.child("ProImgUri").setValue(urr.toString());
                Glide.with(getActivity()).load(urr).fitCenter().into(proimg);
            }
        });
    }
}