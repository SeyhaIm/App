package kh.com.kredit.kauthentications.Models.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kh.com.kredit.kauthentications.Models.OtpModelView;

public class DbQuery {
    private SQLiteDatabase mSQLite;
    private DBHandler mHelper;


    public DbQuery(Context c) {
        mHelper = new DBHandler(c);
        mSQLite = mHelper.getWritableDatabase();

    }

    public boolean saveData(OtpDataModel data) {
        ContentValues cv = new ContentValues();
        Cursor c = mSQLite.query("otp", null, null, null, null, null, null);
        long i;
        if (c.getCount() == 0) {
            cv.put("id", 1);
            cv.put("issuer", data.getIssuer());
            cv.put("account", data.getAccount());
            cv.put("token", data.getToken());
            i = mSQLite.insert("otp", null, cv);
        } else {
            c.moveToLast();
            int l = Integer.valueOf(c.getString(c.getColumnIndex("id")));
            cv.put("id", l + 1);
            cv.put("issuer", data.getIssuer());
            cv.put("account", data.getAccount());
            cv.put("token", data.getToken());
            i = mSQLite.insert("otp", null, cv);
        }
        if (i == -1)
            return false;
        return true;
    }

    public List<OtpDataModel> loadData() {
        List<OtpDataModel> OtpList = new ArrayList<>();
        Cursor c1 = mSQLite.query("otp", null, null, null, null, null, null);
        while (c1.moveToNext()) {
            OtpList.add(new OtpDataModel(Integer.valueOf(c1.getString(c1.getColumnIndex("id"))),
                    c1.getString(c1.getColumnIndex("issuer")),
                    c1.getString(c1.getColumnIndex("account")),
                    c1.getString(c1.getColumnIndex("token"))));
        }
        return OtpList;
    }

    public void deleteData(int id) {
        mSQLite.delete("otp", "id =" + id, null);
    }

}