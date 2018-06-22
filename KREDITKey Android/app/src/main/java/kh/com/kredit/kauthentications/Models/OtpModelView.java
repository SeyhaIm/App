package kh.com.kredit.kauthentications.Models;

import kh.com.kredit.kauthentications.Models.OtpModels.Totp;

public class OtpModelView {
    private int id;
    private String issue;
    private String otp;
    private String acc;
    private Totp tp;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIssue() {
        return issue;
    }

    public String getOtp() {
        return tp.now();
    }

    public String getAcc() {
        return acc;
    }

    public int getRemind() {
        return tp.getRemainingSeconds();
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }


    public OtpModelView(int id, String issue, String token, String acc) {
        tp = new Totp(token);
        this.id = id;
        this.issue = issue;
        this.acc = acc;
    }
}