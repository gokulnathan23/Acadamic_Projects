package com.jp.jobportal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Seeker_Login_Activity extends AppCompatActivity implements View.OnClickListener {


    EditText sEmail;
    EditText sPswd;


    private FirebaseAuth mAuth;
    public FirebaseUser mFirebaseUser;
    public DatabaseReference mFirebaseDatabase;
    public DatabaseReference userDatabaseRef;

    public String userRole;
    public String suser = "Seeker";
    public String puser = "Provider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_login_activity);

        mAuth = FirebaseAuth.getInstance();

        sEmail = (EditText) findViewById(R.id.slogin_email_id_txtbox);
        sPswd = (EditText) findViewById(R.id.slogin_pswd_txtbox);

        findViewById(R.id.slogin_sign_up).setOnClickListener(this);
        findViewById(R.id.slogin_signin_btn).setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.slogin_sign_up:
                finish();
                startActivity(new Intent(this, Seeker_Signup_Activity.class));
                break;

            case R.id.slogin_signin_btn:
                seekerUserLogin();
                break;
        }
    }
    private void seekerUserLogin()
    {
        String email = sEmail.getText().toString().trim();
        final String pswd = sPswd.getText().toString().trim();

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
        if (pswd.length() < 6) {
            sPswd.setError("Minimum Length Of Password Is 6 Characters!");
            sPswd.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    mFirebaseUser = mAuth.getCurrentUser();
                    mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
                    userDatabaseRef = mFirebaseDatabase.child("Users").child(mFirebaseUser.getUid()).child("userRole");

                    userDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                userRole = dataSnapshot.getValue(String.class);

                                if(userRole.length() == suser.length())
                                {
                                    Intent shmpg = new Intent(getApplicationContext(), Seeker_Mainpage.class);
                                    shmpg.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(shmpg);
                                    finish();
                                }
                                if(userRole.length() == puser.length())
                                {

                                    sEmail.setError("Email-ID is Already Registered In Job Provider");
                                    sEmail.requestFocus();
                                    mAuth.signOut();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else
                {
                    sEmail.setError(task.getException().getMessage());
                    sEmail.requestFocus();
                    return;
                }
            }
        });
    }
}
