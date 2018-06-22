package kh.com.kredit.kauthentications.Models;

import android.widget.Toast;

public class UrlSpliter {
    private String issuer;
    private String account;
    private String token;

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UrlSpliter(String url) {

        String[] s = url.split("/");
        String[] t =s[3].split(":");
        setIssuer(t[0]);

        s = t[1].split("\\?");
        setAccount(s[0]);

        t = s[1].split("=");
        s = t[1].split("&");
        setToken(s[0]);

    }
}