package com.jp.jobportal;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

import java.sql.Time;
@IgnoreExtraProperties
public class SeekersDB {
    String userUid;
    String sFname;
    String sEmail;
    String sPswd;
    String userRole;
    String sMobilenum, sDOB, sGender, sEducation, sWorkExp, sPreLoc;
    String alarmTime;
    String proImgUri;

    public SeekersDB() {
    }

    public SeekersDB(String userUid, String sFname, String sEmail, String sPswd, String userRole, String sMobilenum, String sDOB, String sGender, String sEducation, String sWorkExp, String sPreLoc, String alarmTime, String proImgUri) {
        this.userUid = userUid;
        this.sFname = sFname;
        this.sEmail = sEmail;
        this.sPswd = sPswd;
        this.userRole = userRole;
        this.sMobilenum = sMobilenum;
        this.sDOB = sDOB;
        this.sGender = sGender;
        this.sEducation = sEducation;
        this.sWorkExp = sWorkExp;
        this.sPreLoc = sPreLoc;
        this.alarmTime = alarmTime;
        this.proImgUri = proImgUri;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getsFname() {
        return sFname;
    }

    public void setsFname(String sFname) {
        this.sFname = sFname;
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getsPswd() {
        return sPswd;
    }

    public void setsPswd(String sPswd) {
        this.sPswd = sPswd;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getsMobilenum() {
        return sMobilenum;
    }

    public void setsMobilenum(String sMobilenum) {
        this.sMobilenum = sMobilenum;
    }

    public String getsDOB() {
        return sDOB;
    }

    public void setsDOB(String sDOB) {
        this.sDOB = sDOB;
    }

    public String getsGender() {
        return sGender;
    }

    public void setsGender(String sGender) {
        this.sGender = sGender;
    }

    public String getsEducation() {
        return sEducation;
    }

    public void setsEducation(String sEducation) {
        this.sEducation = sEducation;
    }

    public String getsWorkExp() {
        return sWorkExp;
    }

    public void setsWorkExp(String sWorkExp) {
        this.sWorkExp = sWorkExp;
    }

    public String getsPreLoc() {
        return sPreLoc;
    }

    public void setsPreLoc(String sPreLoc) {
        this.sPreLoc = sPreLoc;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getProImgUri() {
        return proImgUri;
    }

    public void setProImgUri(String proImgUri) {
        this.proImgUri = proImgUri;
    }
}
