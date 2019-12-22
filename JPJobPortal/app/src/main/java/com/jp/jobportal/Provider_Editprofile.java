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

import static android.app.Activity.RESULT_OK;

public class Provider_Editprofile extends Fragment implements View.OnClickListener{

    public EditText pcname,pemail,pmobile,pcid;
    public String pCname,pEmail,pMobile,pCid;
    public Button saveInEditProfile,editinEditprofile;
    private static final int SELECT_PHOTO = 100;
    Uri proImgURI;
    public ImageView proimg;
    public UploadTask uploadTask;
    private FirebaseAuth mAuth;
    public FirebaseUser mFirebaseUser;
    public DatabaseReference mFirebaseDatabase;
    public DatabaseReference user;
    public FirebaseStorage mFirebaseStorage;
    public StorageReference mStorageRef,imageRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.provider_editprofile, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        user = mFirebaseDatabase.child("Users").child(mFirebaseUser.getUid());
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageRef = mFirebaseStorage.getReference();
        imageRef = mStorageRef.child(mAuth.getCurrentUser().getUid()).child("proimg");

        proimg = (ImageView) view.findViewById(R.id.seekerproimgineditprofile);


        saveInEditProfile = (Button) view.findViewById(R.id.psaveprofile);
        saveInEditProfile.setOnClickListener(this);
        editinEditprofile = (Button) view.findViewById(R.id.peditprofile);
        editinEditprofile.setOnClickListener(this);

        pcname = (EditText) view.findViewById(R.id.providercompanynameineditprofile);
        pemail = (EditText) view.findViewById(R.id.provideremailineditprofile);

        pcid = (EditText) view.findViewById(R.id.providercompanyidineditprofile);
        pmobile = (EditText) view.findViewById(R.id.providermobnumineditprofile);

        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pCname = dataSnapshot.child("pCname").getValue(String.class);
                pcname.setText(pCname);
                pEmail = dataSnapshot.child("pEmail").getValue(String.class);
                pemail.setText(pEmail);
                pCid = dataSnapshot.child("pCID").getValue(String.class);
                pcid.setText(pCid);
                pMobile = dataSnapshot.child("mobileNum").getValue(String.class);
                pmobile.setText(pMobile);
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
                   proimg.setImageResource(R.drawable.intro_job_provider_img);
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
    public void onClick(View view) {
        if(view == saveInEditProfile )
        {
            pCname = pcname.getText().toString();
            pEmail = pemail.getText().toString().trim();
            pCid = pcid.getText().toString();
            pMobile = pmobile.getText().toString();

            user.child("pCname").setValue(pCname);
            user.child("pEmail").setValue(pEmail);
            user.child("pCID").setValue(pCid);
            user.child("mobileNum").setValue(pMobile);

            pcname.clearFocus();
            pemail.clearFocus();
            pcid.clearFocus();
            pmobile.clearFocus();

        }
        if(view == editinEditprofile)
        {
            selectImage(view);
        }
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

