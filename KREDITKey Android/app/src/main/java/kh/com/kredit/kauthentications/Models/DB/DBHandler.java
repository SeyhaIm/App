package kh.com.kredit.kauthentications.Models.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    public DBHandler(Context c) {
        super(c, "OtpDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE otp (id INTEGER,issuer VARCHAR(20),account VARCHAR(20),token VARCHAR(20))";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}