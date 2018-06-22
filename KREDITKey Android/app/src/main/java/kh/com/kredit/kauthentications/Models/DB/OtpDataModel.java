package kh.com.kredit.kauthentications.Models.DB;

public class OtpDataModel {
    private int _id;
    private String issuer;
    private String account;
    private String token;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

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

    public OtpDataModel(int id, String issuer, String account, String token) {
        this._id = id;
        this.issuer = issuer;
        this.account = account;
        this.token = token;
    }
}