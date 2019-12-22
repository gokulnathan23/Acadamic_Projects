package com.jp.jobportal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Provider_Login_Activity extends AppCompatActivity implements View.OnClickListener
{
    EditText pEmail;
    EditText pPswd;

    private FirebaseAuth mAuth;
    public FirebaseUser mFirebaseUser;
    public DatabaseReference mFirebaseDatabase;
    public DatabaseReference userDatabaseRef;

    public String userRole;
    public String suser = "Seeker";
    public String puser = "Provider";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_login_activity);

        pEmail = (EditText) findViewById(R.id.plogin_email_id_txtbox);
        pPswd = (EditText) findViewById(R.id.plogin_pswd_txtbox);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.plogin_sign_up).setOnClickListener(this);
        findViewById(R.id.psignin_signin_btn).setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.plogin_sign_up:
                finish();
                startActivity(new Intent(this, Provider_Signup_Activity.class));
                break;

            case R.id.psignin_signin_btn:
                providerUserLogin();
                break;
        }
    }
    private void providerUserLogin() {
        String email = pEmail.getText().toString().trim();
        String pswd = pPswd.getText().toString().trim();

        if (email.isEmpty()) {
            pEmail.setError("Email Is Required");
            pEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            pEmail.setError("Please Enter A Valid Email Address!");
            pEmail.requestFocus();
            return;
        }
        if (pswd.isEmpty()) {
            pPswd.setError("Password Is Required!");
            pPswd.requestFocus();
            return;
        }
        if (pswd.length() < 6) {
            pPswd.setError("Minimum Length Of Password Is 6 Characters!");
            pPswd.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
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

                                if(userRole.length() == puser.length())
                                {
                                    Intent shmpg = new Intent(getApplicationContext(), Provider_Mainpage.class);
                                    shmpg.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(shmpg);
                                    finish();
                                }
                                if(userRole.length() == suser.length())
                                {
                                    pEmail.setError("Email-ID is Already Registered In Job Seeker");
                                    pEmail.requestFocus();
                                    mAuth.signOut();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    pEmail.setError(task.getException().getMessage());
                    pEmail.requestFocus();
                    return;
                }
            }
        });
    }
}
