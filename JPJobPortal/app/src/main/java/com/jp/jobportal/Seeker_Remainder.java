package com.jp.jobportal;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class Seeker_Remainder extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;
    public FirebaseUser mFirebaseUser;
    public DatabaseReference mFirebaseDatabase;
    public DatabaseReference user;
    public String alarmTimee;
    public Button saveAlarm;
    public Calendar calSet;

    private TextView alarmTime;
    public Button setAlarm;
    private int mHour, mMinute;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.seeker_remainder, null);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        user = mFirebaseDatabase.child("Users").child(mFirebaseUser.getUid());

        setAlarm = (Button) view.findViewById(R.id.setAlarm);
        alarmTime = (TextView) view.findViewById(R.id.alarmTime);

        saveAlarm = (Button) view.findViewById(R.id.saveAlarm);

        setAlarm.setOnClickListener(this);
        saveAlarm.setOnClickListener(this);
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                alarmTimee = dataSnapshot.child("alarmTime").getValue(String.class);
                alarmTime.setText(alarmTimee);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == setAlarm) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);


            final TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String amPm;
                    if (hourOfDay >= 12) {
                        amPm = "PM";
                    } else {
                        amPm = "AM";
                    }
                    alarmTime.setText(String.format("%02d:%02d", hourOfDay, minute) + " " + amPm);

                    calSet = (Calendar) c.clone();
                    calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calSet.set(Calendar.MINUTE, minute);
                    calSet.set(Calendar.SECOND, 0);
                    calSet.set(Calendar.MILLISECOND, 0);
                    if(calSet.compareTo(c) <= 0){
                        calSet.add(Calendar.DATE, 1);
                    }

                }
            }, mHour, mMinute, false);
            timePickerDialog.show();
        }

        if(v == saveAlarm)
        {
            user.child("alarmTime").setValue(alarmTime.getText());
            Intent intent1 = new Intent(getContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
}


