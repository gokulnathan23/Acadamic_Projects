package com.jp.jobportal;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Seeker_Job_Status_Adapter extends RecyclerView.Adapter<Seeker_Job_Status_Adapter.MyViewHolder> {

    List<PostJobDB> listArray;
    public List<String> listArray1;
    public TextToSpeech tTos;
    List<String> textToSpeech = new ArrayList<>();
    public String sentence;
    public int i=0;


    public Seeker_Job_Status_Adapter(List<PostJobDB> listArray, List<String> listArray1) {
        this.listArray = listArray;
        this.listArray1 = listArray1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seeker_job_status_recyler, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        PostJobDB data = listArray.get(position);
        holder.jobDesig.setText(data.getJobDesig());
        holder.experien.setText(data.getExperien());
        holder.loc.setText(data.getJobLoc());
        holder.cn.setText(data.getComname());
        holder.id.setText(data.getAdID());
        if(listArray1.get(i).equals("Selected"))
        {
            holder.result.setBackgroundColor(Color.GREEN);
            holder.result.setText(listArray1.get(i));
            holder.result.setTextColor(Color.BLACK);
            i++;
        }
        else if(listArray1.get(i).equals("Waiting List"))
        {
            holder.result.setBackgroundColor(Color.YELLOW);
            holder.result.setTextColor(Color.BLACK);
            holder.result.setText(listArray1.get(i));
            i++;
        }
        else if(listArray1.get(i).equals("Rejected"))
        {
            holder.result.setBackgroundColor(Color.RED);
            holder.result.setText(listArray1.get(i));
            holder.result.setTextColor(Color.BLACK);
            i++;
        }

        if(holder.result.getText().equals("Selected"))
        {
            sentence = "Company Name Is " + holder.cn.getText()+ "And You Applied For The Job" +holder.jobDesig.getText()+ "You Are Selected By The Company";
        }
        else if(holder.result.getText().equals("Waiting List"))
        {
            sentence = "Company Name Is " + holder.cn.getText()+ "And You Applied For The Job" +holder.jobDesig.getText()+ "You Are Still In Waiting List";
        }
        else if(holder.result.getText().equals("Rejected"))
        {
            sentence = "Company Name Is " + holder.cn.getText()+ "And You Applied For The Job" +holder.jobDesig.getText()+ "You Are Rejected By The Company";
        }
        textToSpeech.add(sentence);
    }

    @Override
    public int getItemCount() {
        return listArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView jobDesig, experien, loc, cn, id, result;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            jobDesig = (TextView) itemView.findViewById(R.id.jobDesiginjobstatus);
            experien = (TextView) itemView.findViewById(R.id.expinjobstatus);
            loc = (TextView) itemView.findViewById(R.id.joblocinjobstatus);
            cn = (TextView) itemView.findViewById(R.id.cninjobstatus);
            id = (TextView) itemView.findViewById(R.id.idinjobstatus);
            result = (TextView) itemView.findViewById(R.id.jobresultinjobstatus);

            tTos = new TextToSpeech(itemView.getContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status != TextToSpeech.ERROR) {
                        tTos.setLanguage(Locale.UK);
                    }
                }
            });

        }



    }
}
