package com.jp.jobportal;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;



public class Intro_Activity extends AppCompatActivity {

    public Intro_Activity() {
    }

    private FloatingActionButton jobseeker;
    private FloatingActionButton jobprovider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);

        jobseeker = (FloatingActionButton)findViewById(R.id.intro_job_seeker_btn);
        jobprovider = (FloatingActionButton)findViewById(R.id.intro_job_provider_btn);

        jobseeker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seekerlogin = new Intent(Intro_Activity.this, Seeker_Login_Activity.class);
                startActivity(seekerlogin);
            }
        });
        jobprovider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent providerlogin = new Intent(Intro_Activity.this, Provider_Login_Activity.class);
                startActivity(providerlogin);
            }
        });
    }
}

