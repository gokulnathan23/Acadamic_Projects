package com.jp.jobportal;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class Seeker_Mainpage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment = null;
    private FirebaseAuth mAuth;
    public FirebaseUser mFirebaseUser;
    public DatabaseReference mFirebaseDatabase;
    public DatabaseReference user;
    public AlertDialog.Builder logoutAlert;
    public TextView seekerNameInNav, seekerEmailInNav;
    private ImageView proimg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_mainpage);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        user = mFirebaseDatabase.child("Users").child(mFirebaseUser.getUid());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View nHV = navigationView.getHeaderView(0);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.seekerhome));

        proimg = (ImageView) nHV.findViewById(R.id.seekerproimginnav);
        seekerNameInNav = (TextView) nHV.findViewById(R.id.seekernameinnav);
        seekerEmailInNav = (TextView) nHV.findViewById(R.id.seekeremailinnav);

        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    seekerNameInNav.setText(dataSnapshot.child("sFname").getValue(String.class));
                    seekerEmailInNav.setText(dataSnapshot.child("sEmail").getValue(String.class));

                    if(dataSnapshot.child("ProImgUri").exists())
                    {
                        String ur = dataSnapshot.child("ProImgUri").getValue().toString();
                        Uri urrr = Uri.parse(ur);
                        Glide.with(Seeker_Mainpage.this).load(urrr).fitCenter().into(proimg);
                    }
                    else
                    {
                        proimg.setImageResource(R.drawable.shmpgdefproimg);
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        navigationView.setNavigationItemSelectedListener(this);


        logoutAlert = new AlertDialog.Builder(this);
        logoutAlert.setMessage("Are You Sure Want To Logout")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent seekerRetunLogin = new Intent(Seeker_Mainpage.this, Intro_Activity.class);
                        startActivity(seekerRetunLogin);
                        mAuth.signOut();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.seekerhome)
        {
            fragment = new Seeker_Home();
        }
        if (id == R.id.seekereditprofile) {
            fragment = new Seeker_EditProfile();

        } else if (id == R.id.seekerjobstatus) {
            fragment = new Seeker_JobStatus();

        } else if (id == R.id.seekerjobads) {
            fragment = new Seeker_JobAds();

        } else if (id == R.id.seekerremainder) {
            fragment = new Seeker_Remainder();

        } else if (id == R.id.seekerlogout) {
            logoutAlert.create().show();
        }
        if(fragment != null)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.seekerhomepagearea, fragment);
            ft.commit();
        }
        item.setChecked(true);
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
