package kh.com.kredit.kauthentications.Models;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kh.com.kredit.kauthentications.R;

public class ListviewAdapter extends BaseAdapter {
    private List<OtpModelView> mOtpList;
    private Activity a;
    TextView txtIssuer;
    TextView txtOtp;
    TextView txtAcc;
    TextView txtRemind;

    public ListviewAdapter(List<OtpModelView> lst, Activity activity) {
        this.mOtpList = lst;
        this.a = activity;
    }

    @Override
    public int getCount() {
        return mOtpList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = a.getLayoutInflater().inflate(R.layout.itemlist, null);
        txtIssuer = (TextView) convertView.findViewById(R.id.txtIssuer);
        txtOtp = (TextView) convertView.findViewById(R.id.txtOtp);
        txtAcc = (TextView) convertView.findViewById(R.id.txtAcc);
        txtRemind = (TextView) convertView.findViewById(R.id.txtRemind);

        txtAcc.setText(mOtpList.get(position).getAcc());
        txtIssuer.setText(mOtpList.get(position).getIssue());
        txtOtp.setText(mOtpList.get(position).getOtp());
        txtRemind.setText(mOtpList.get(position).getRemind() + "");

        int i = mOtpList.get(position).getRemind();
        if (i <= 6) {
            if ((i % 2) == 1) {
                txtOtp.setTextColor(Color.parseColor("#F44336"));
            }
        }
        return convertView;
    }

}