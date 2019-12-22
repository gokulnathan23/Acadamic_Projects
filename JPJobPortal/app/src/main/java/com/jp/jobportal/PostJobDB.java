package com.jp.jobportal;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class PostJobDB {


    String adID, jobDesig, jobTyp, jobLoc, qualifica, experien, comname, comemail;

    public PostJobDB() {
    }

    public PostJobDB(String adID, String jobDesig, String jobTyp, String jobLoc, String qualifica, String experien, String comname, String comemail) {
        this.adID = adID;
        this.jobDesig = jobDesig;
        this.jobTyp = jobTyp;
        this.jobLoc = jobLoc;
        this.qualifica = qualifica;
        this.experien = experien;
        this.comname = comname;
        this.comemail = comemail;
    }

    public String getAdID() {
        return adID;
    }

    public void setAdID(String adID) {
        this.adID = adID;
    }

    public String getJobDesig() {
        return jobDesig;
    }

    public void setJobDesig(String jobDesig) {
        this.jobDesig = jobDesig;
    }

    public String getJobTyp() {
        return jobTyp;
    }

    public void setJobTyp(String jobTyp) {
        this.jobTyp = jobTyp;
    }

    public String getJobLoc() {
        return jobLoc;
    }

    public void setJobLoc(String jobLoc) {
        this.jobLoc = jobLoc;
    }

    public String getQualifica() {
        return qualifica;
    }

    public void setQualifica(String qualifica) {
        this.qualifica = qualifica;
    }

    public String getExperien() {
        return experien;
    }

    public void setExperien(String experien) {
        this.experien = experien;
    }

    public String getComname() {
        return comname;
    }

    public void setComname(String comname) {
        this.comname = comname;
    }

    public String getComemail() {
        return comemail;
    }

    public void setComemail(String comemail) {
        this.comemail = comemail;
    }
}
