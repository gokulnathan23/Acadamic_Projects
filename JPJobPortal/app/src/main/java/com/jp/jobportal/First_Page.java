package com.jp.jobportal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class First_Page extends Activity {

    private ProgressBar progress;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mFirebaseUser;
    public DatabaseReference mFirebaseDatabase;
    public DatabaseReference userDatabaseRef;

    public String userRole;
    public String suser = "Seeker";
    public String puser = "Provider";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        progress = (ProgressBar) findViewById(R.id.progressBarC);

        new Thread(new Runnable()
        {
            public void run()
            {
                handler.post(new Runnable() {
                    public void run() {

                        progress.setProgress(progressStatus);
                    }

                });
                try
                {
                    Thread.sleep(200);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();

        if (mFirebaseUser != null)
        {
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
                            Intent phmpg = new Intent(getApplicationContext(), Provider_Mainpage.class);
                            phmpg.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(phmpg);
                            finish();
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
            Intent introact = new Intent(First_Page.this, Intro_Activity.class);
            startActivity(introact);
            finish();
        }

    }
}
