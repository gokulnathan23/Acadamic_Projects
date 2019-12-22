package com.jp.jobportal;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {

    public List<String> textSpeech;
    public String spech;
    public TextToSpeech tts;
    public FirebaseAuth mAuth1;
    public FirebaseDatabase mFirebaseDB ;
    public DatabaseReference usersRef,userDB,userJobsApplied, postedJobs,jobinfo;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        tts=new TextToSpeech(context , new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });
        spech = "Alarm Started";
        tts.speak(spech, TextToSpeech.QUEUE_FLUSH, null);
    }
}
