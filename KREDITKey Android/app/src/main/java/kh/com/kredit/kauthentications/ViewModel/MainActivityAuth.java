package kh.com.kredit.kauthentications.ViewModel;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.List;

import kh.com.kredit.kauthentications.Models.AESECB;
import kh.com.kredit.kauthentications.Models.DB.DbQuery;
import kh.com.kredit.kauthentications.Models.DB.OtpDataModel;
import kh.com.kredit.kauthentications.Models.ListviewAdapter;
import kh.com.kredit.kauthentications.Models.OtpModelView;
import kh.com.kredit.kauthentications.Models.UrlSpliter;
import kh.com.kredit.kauthentications.R;

public class MainActivityAuth extends AppCompatActivity {
    ListView lvOtp;

    Dialog dialog;
    FloatingActionButton fab ;
    public String selected;
    int p, index, top;
    public List<OtpModelView> mOtpList;
    private IntentIntegrator qrScan;
    private Intent info_screen;
    private static final int REQUEST_CODE = 101;
    private static final String EXTRA_SCREEN = "SCREEN";
    private static final int PERMISSION_REQUEST = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},PERMISSION_REQUEST);
        }

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        lvOtp = (ListView) findViewById(R.id.otpLst);
        lvOtp.setClickable(true);
        fab = (FloatingActionButton) findViewById(R.id.fabNew);

        mToolbar.setTitle(R.string.app_name);

        //toolbar Menu​​ implementation
        mToolbar.inflateMenu(R.menu.main_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.about:
                        Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),QrCodeSubfaceViewActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        // On click List item event and show pop up dialog
        lvOtp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                p = position;
                dialog = new Dialog(arg1.getContext());
                dialog.setContentView(R.layout.dialogalert);
                dialog.setTitle("Authentication");

                TextView dlHeader = (TextView) findViewById(R.id.dlHeader);
                TextView dlMaessage = (TextView) findViewById(R.id.dlMessage);

                Button btDelete = (Button) dialog.findViewById(R.id.btDel);
                Button btCopy = (Button) dialog.findViewById(R.id.btCopy);
                selected = ((TextView) arg1.findViewById(R.id.txtOtp)).getText().toString();


                btDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();

                        Dialog dialog_delete = new Dialog(getApplicationContext());
                        dialog.setContentView(R.layout.activity_delete_alret);
                        Button btYes = (Button) dialog.findViewById(R.id.btYes);
                        Button btNo = (Button) dialog.findViewById(R.id.btNo);
                        btNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                        btYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DbQuery qr = new DbQuery(getApplicationContext());
                                        qr.deleteData(Integer.valueOf(mOtpList.get(p).getId()));
                                        mOtpList = getListData();
                                        dialog.cancel();

                                        if (mOtpList.isEmpty()) {
                                            info_screen = new Intent(getApplicationContext(), InfoActivity.class);
                                            startActivity(info_screen);
                                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                            finish();
                                        }
                            }
                        });

                        dialog.show();
                    }
                });

                btCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        copyToClipboard(selected);
                        Toast.makeText(getApplicationContext(), "Copy: "+selected, Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                dialog.show();

            }
        });

        mOtpList = getListData();




        // Get scroll state
        lvOtp.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                index = lvOtp.getFirstVisiblePosition();
                View v = lvOtp.getChildAt(0);
                top = (v == null) ? 0 : (v.getTop() - lvOtp.getPaddingTop());
            }
        });

        refreshList();

    }

    // get data from local database
    public List<OtpModelView> getListData() {
        List<OtpModelView> OtpList = new ArrayList<>();
        DbQuery qr = new DbQuery(this);
        List<OtpDataModel> _otp = qr.loadData();
        AESECB aes = new AESECB();

        try {
            for (int i = 0; i < _otp.size(); i++) {
                // EncryptionAES.decrypt AES method decryption
                String plain = aes.stringDecrytion(_otp.get(i).getToken());
                OtpList.add(new OtpModelView(_otp.get(i).get_id(), _otp.get(i).getIssuer(), plain, _otp.get(i).getAccount()));
            }
        } catch (Exception e) {

        }
        return OtpList;
    }

    // refresh List item every 0.5s
    public void refreshList() {
        Thread t = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ListviewAdapter customAdapter;
                                customAdapter = new ListviewAdapter(mOtpList, MainActivityAuth.this);
                                lvOtp.setAdapter(customAdapter);
                                lvOtp.setSelectionFromTop(index, top);
                            }
                        });
                        Thread.sleep(500);
                        // MARK: clean allocated threading memory
                        System.gc();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }
        };
        t.start();
    }

    // MARK: clean allocated threading memory
    @Override
    protected void onPause() {
        super.onPause();
        System.gc();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    // Get the result from QR scanned
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
                    mOtpList = getListData();

                }catch (Exception e){
                    showAlert();
                }
            }
        }
    }

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
//
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    // Clip broad method
    public void copyToClipboard(String text) {
        ClipboardManager myClipboard;
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip;
        myClip = ClipData.newPlainText("otp", text);
        myClipboard.setPrimaryClip(myClip);

    }
}
