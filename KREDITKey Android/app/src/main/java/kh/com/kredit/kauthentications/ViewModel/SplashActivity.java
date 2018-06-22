package kh.com.kredit.kauthentications.ViewModel;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kh.com.kredit.kauthentications.Models.DB.DbQuery;
import kh.com.kredit.kauthentications.Models.DB.OtpDataModel;
import kh.com.kredit.kauthentications.Models.EncryptionAES;
import kh.com.kredit.kauthentications.Models.OtpModelView;
import kh.com.kredit.kauthentications.R;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        Window win = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            win.setStatusBarColor(Color.WHITE);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getDataCount() == 0){
                    Intent intent = new Intent(getApplicationContext(),InfoActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(),MainActivityAuth.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                finish();

            }
        },SPLASH_TIME_OUT);

    }

    // get data from local database
    public int getDataCount() {
        List<OtpModelView> OtpList = new ArrayList<>();
        DbQuery qr = new DbQuery(this);
        List<OtpDataModel> _otp = qr.loadData();
        return _otp.size();
    }



}
