package com.jp.jobportal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Provider_Signup_Activity extends AppCompatActivity
{

    EditText pCname;
    EditText pEmail;
    EditText pCID;
    EditText pPswd;
    EditText pCpswd;
    String cname;
    String pcid;
    String email;
    String pswd;
    String cpswd, mobilenum;
    ImageButton pSignUP_Btn;
    ImageButton pLogIN_Btn;
    public String role,proImgUri;
    public String providerID;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_signup_activity);

        pCname = (EditText) findViewById(R.id.psignup_cname_txtbox);
        pCID = (EditText) findViewById(R.id.psignup_company_id_txtbox);
        pEmail = (EditText) findViewById(R.id.psignup_email_id_txtbox);
        pPswd = (EditText) findViewById(R.id.psignup_pswd_txtbox);
        pCpswd = (EditText) findViewById(R.id.psignup_cpswd_txtbox);

        pSignUP_Btn = (ImageButton) findViewById(R.id.psignup_signup_btn);
        pLogIN_Btn = (ImageButton) findViewById(R.id.psignup_signin);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mFirebaseDatabase.getReference("Users");

        pLogIN_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Provider_Signup_Activity.this, Provider_Login_Activity.class));
                finish();
            }
        });

        pSignUP_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                providerUser();;
            }
        });
    }
    private void providerUser()
    {
        cname = pCname.getText().toString();
        email = pEmail.getText().toString().trim();
        pcid = pCID.getText().toString().trim();
        pswd = pPswd.getText().toString().trim();
        cpswd = pCpswd.getText().toString().trim();
        role = "Provider";


        if (cname.isEmpty()) {
            pEmail.setError("Name Is Required");
            pEmail.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            pEmail.setError("Email Is Required");
            pEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            pEmail.setError("Please Enter A Valid Email Address!");
            pEmail.requestFocus();
            return;
        }

        if (pcid.isEmpty()) {
            pEmail.setError("Company-ID Is Required");
            pEmail.requestFocus();
            return;
        }

        if (pswd.isEmpty()) {
            pPswd.setError("Password Is Required!");
            pPswd.requestFocus();
            return;
        }
        if (cpswd.isEmpty()) {
            pCpswd.setError("Confirm Password Is Required!");
            pCpswd.requestFocus();
            return;
        }
        if (pswd.length() < 6) {
            pPswd.setError("Minimum Length Of Password Is 6 Characters!");
            pPswd.requestFocus();
            return;
        }
        if (!cpswd.equals(pswd)) {
            pCpswd.setError("Passwords Doesn't Match!");
            pCpswd.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    finish();
                    ProvidersDB provider = new ProvidersDB(cname,email,pcid,mobilenum,pswd,role,proImgUri);
                    providerID = mAuth.getUid();
                    if (mDatabaseRef.child(providerID).equals(providerID)) {
                        pEmail.setError("Email Is Already Registered In Job Seeker");
                        pEmail.requestFocus();
                        return;
                    }
                    else {

                        mDatabaseRef.child(providerID).setValue(provider);
                        startActivity(new Intent(Provider_Signup_Activity.this, Provider_Mainpage.class));
                    }
                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        pEmail.setError("Emai-ID Is Already Registerd");
                        pEmail.requestFocus();
                    }
                }
            }
        });
    }
}
