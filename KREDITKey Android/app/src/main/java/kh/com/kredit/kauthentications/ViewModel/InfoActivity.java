package kh.com.kredit.kauthentications.ViewModel;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import kh.com.kredit.kauthentications.Models.AESECB;
import kh.com.kredit.kauthentications.Models.DB.DbQuery;
import kh.com.kredit.kauthentications.Models.DB.OtpDataModel;
import kh.com.kredit.kauthentications.Models.EncryptionAES;
import kh.com.kredit.kauthentications.Models.OtpModelView;
import kh.com.kredit.kauthentications.Models.OtpModels.Totp;
import kh.com.kredit.kauthentications.Models.UrlSpliter;
import kh.com.kredit.kauthentications.R;


public class InfoActivity extends AppCompatActivity {

    private Button btn_begin;
    private static final int REQUEST_CODE = 100;
    private static final int PERMISSION_REQUEST = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        btn_begin = (Button) findViewById(R.id.btnbegin);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},PERMISSION_REQUEST);
        }
        btn_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),QrCodeSubfaceViewActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if (data != null){
                try {
                    Barcode barcode = data.getParcelableExtra("Barcode");
                    String result =  barcode.displayValue;
                    UrlSpliter spliter = new UrlSpliter(result);


                    AESECB aes = new AESECB();
                    String cipher = spliter.getToken();
                    String plain = aes.stringDecrytion(cipher);

                    System.out.print("cipher: " + spliter.getToken());
                    System.out.print("PlainText: " + plain);

                    OtpModelView otp_model = new OtpModelView(0,spliter.getIssuer(),plain,spliter.getAccount());
                    String otp_text = otp_model.getOtp();

                    DbQuery qr = new DbQuery(this);
                    qr.saveData(new OtpDataModel(0, spliter.getIssuer(), spliter.getAccount(), cipher));

                    Intent intent = new Intent(getApplicationContext(),MainActivityAuth.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();

                }catch (Exception e){
                    showAlert();
                }
            }
        }
    }

    // Event press button back
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.getRepeatCount() == 0) {
//            event.startTracking();
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Alert!");
//            builder.setMessage("Are you sure to exit?");
//            builder.setCancelable(true);
//
//            builder.setPositiveButton(
//                    "Yes",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                            finish();
//                        }
//                    });
//
//            builder.setNegativeButton(
//                    "No",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//
//            AlertDialog alert = builder.create();
//            alert.show();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    // MAKE: Alert
    public void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert!");
        builder.setMessage("Unrecognized QR Code.");
        builder.setCancelable(true);
        builder.setNegativeButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
