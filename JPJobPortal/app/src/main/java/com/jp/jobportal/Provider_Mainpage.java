package com.jp.jobportal;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class Provider_Mainpage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment = null;
    public AlertDialog.Builder logoutAlert;
    private FirebaseAuth mAuth;
    public FirebaseUser mFirebaseUser;
    public DatabaseReference mFirebaseDatabase;
    public DatabaseReference user;
    public TextView providerNameInNav, providerEmailInNav;
    private ImageView proimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_mainpage);

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
        navigationView.setNavigationItemSelectedListener(this);
        View nHV = navigationView.getHeaderView(0);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.providerhome));


        proimg = (ImageView) nHV.findViewById(R.id.providerimageinnav);
        providerNameInNav = (TextView) nHV.findViewById(R.id.providernameinnav);
        providerEmailInNav = (TextView) nHV.findViewById(R.id.provideremailinnav);


        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                providerNameInNav.setText(dataSnapshot.child("pCname").getValue(String.class));
                providerEmailInNav.setText(dataSnapshot.child("pEmail").getValue(String.class));

                if(dataSnapshot.child("ProImgUri").exists())
                {
                    String ur = dataSnapshot.child("ProImgUri").getValue().toString();
                    Uri urrr = Uri.parse(ur);
                    Glide.with(Provider_Mainpage.this).load(urrr).fitCenter().into(proimg);
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

        logoutAlert = new AlertDialog.Builder(this);
        logoutAlert.setMessage("Are You Sure Want To Logout")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent seekerRetunLogin = new Intent(Provider_Mainpage.this, Intro_Activity.class);
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

        if (id == R.id.providerhome) {
            fragment = new Provider_Home();
        } else if (id == R.id.providereditprofile) {
            fragment = new Provider_Editprofile();
        } else if (id == R.id.providerpostjobs) {
            fragment = new Provider_PostNewJobs();
        } else if (id == R.id.providerpostedjobs) {
            fragment = new Provider_PostedJobs();
        } else if (id == R.id.providerseekersapplied) {
            fragment = new Provider_SeekersApplied();
        } else if (id == R.id.providerlogout) {
            logoutAlert.create().show();
        }
        if(fragment != null)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.providerhomepagearea, fragment);
            ft.commit();
        }
        item.setChecked(true);
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
