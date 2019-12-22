package com.jp.jobportal;

public class ProvidersDB {
    String pCname;
    String pEmail;
    String pCID;
    String mobileNum;
    String pPswd;
    String userRole;
    String proImgUri;

    public ProvidersDB(String pCname, String pEmail, String pCID, String mobileNum, String pPswd, String userRole, String proImgUri) {
        this.pCname = pCname;
        this.pEmail = pEmail;
        this.pCID = pCID;
        this.mobileNum = mobileNum;
        this.pPswd = pPswd;
        this.userRole = userRole;
        this.proImgUri = proImgUri;
    }

    public String getpCname() {
        return pCname;
    }

    public void setpCname(String pCname) {
        this.pCname = pCname;
    }

    public String getpEmail() {
        return pEmail;
    }

    public void setpEmail(String pEmail) {
        this.pEmail = pEmail;
    }

    public String getpCID() {
        return pCID;
    }

    public void setpCID(String pCID) {
        this.pCID = pCID;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getpPswd() {
        return pPswd;
    }

    public void setpPswd(String pPswd) {
        this.pPswd = pPswd;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getProImgUri() {
        return proImgUri;
    }

    public void setProImgUri(String proImgUri) {
        this.proImgUri = proImgUri;
    }
}
