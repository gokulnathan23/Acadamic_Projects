package com.jp.jobportal;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;


public class Seeker_Signup_Activity extends AppCompatActivity{

   public EditText sFname;
   public EditText sEmail;
   public EditText sPswd;
   public EditText sCpswd;
   public String role;
   public String fname;
   public String email;
   public String pswd;
   public String cpswd,sMobile,sDob,sGender,sEducation,sWorkexp,sPreloc, uID;
   String alarmTime;
   String ProImgUri;
   public String seekerID;
   ImageButton sSignUP_Btn;
   ImageButton sLogIN_Btn;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRef, mUserDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_signup_activity);

        sFname = (EditText) findViewById(R.id.ssignup_fname_txtbox);
        sEmail = (EditText) findViewById(R.id.ssignup_email_id_txtbox);
        sPswd = (EditText) findViewById(R.id.ssignup_pswd_txtbox);
        sCpswd = (EditText) findViewById(R.id.ssignup_cpswd_txtbox);
        sSignUP_Btn = (ImageButton) findViewById(R.id.ssignup_btn);
        sLogIN_Btn = (ImageButton) findViewById(R.id.ssignup_signin);

        mAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mFirebaseDatabase.getReference("Users");



        sLogIN_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                startActivity(new Intent(Seeker_Signup_Activity.this, Seeker_Login_Activity.class));
            }
        });

        sSignUP_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                seekerUser();;
            }
        });

    }

    private void seekerUser()
    {
        fname = sFname.getText().toString();
        email = sEmail.getText().toString().trim();
        pswd = sPswd.getText().toString().trim();
        cpswd = sCpswd.getText().toString().trim();
        role = "Seeker";


        if (fname.isEmpty()) {
            sFname.setError("Name Is Required");
            sFname.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            sEmail.setError("Email Is Required");
            sEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            sEmail.setError("Please Enter A Valid Email Address!");
            sEmail.requestFocus();
            return;
        }
        if (pswd.isEmpty()) {
            sPswd.setError("Password Is Required!");
            sPswd.requestFocus();
            return;
        }
        if (cpswd.isEmpty()) {
            sCpswd.setError("Confirm Password Is Required!");
            sCpswd.requestFocus();
            return;
        }
        if (pswd.length() < 6) {
            sPswd.setError("Minimum Length Of Password Is 6 Characters!");
            sPswd.requestFocus();
            return;
        }
        if (!cpswd.equals(pswd)) {
            sCpswd.setError("Passwords Doesn't Match!");
            sCpswd.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {

                    uID = mAuth.getCurrentUser().getUid();

                    SeekersDB seeker = new SeekersDB(uID, fname, email, pswd, role, sMobile, sDob, sGender, sEducation, sWorkexp, sPreloc, alarmTime, ProImgUri);
                    seekerID = mAuth.getUid();
                    String sID = mAuth.getUid();


                    if (mDatabaseRef.child(sID).equals(seekerID)) {
                        sEmail.setError("Email Is Already Registered In Job Provider");
                        sEmail.requestFocus();
                        return;

                    } else{

                        mDatabaseRef.child(seekerID).setValue(seeker);
                        startActivity(new Intent(Seeker_Signup_Activity.this, Seeker_Mainpage.class));
                    }
                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        sEmail.setError("Emai-ID Is Already Registerd");
                        sEmail.requestFocus();
                    }
                }
            }
        });
    }


}