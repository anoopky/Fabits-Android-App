package in.fabits.fabits.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import in.fabits.fabits.Signup_2;
import in.fabits.fabits.Signup_3;
import in.fabits.fabits.dialogs.OtpDialog;
import in.fabits.fabits.R;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.RequestManager;

public class changePhone extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{
    EditText phone;
    Button save;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        SharedPreferences shared = getSharedPreferences(Preferences.getDefaultSharedPreferencesName(this), 0);

        shared.registerOnSharedPreferenceChangeListener(this);
        Toolbar mToolbar;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Phone Number");
        phone = (EditText) findViewById(R.id.phone);
        phone.setText(Preferences.getSavedSetting(Preferences.S_PHONE, this));
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNum = phone.getText().toString();
                if(phoneNum.length()== 10) {
                    sendOtp(phoneNum);

                } else {
                    Toast.makeText(getBaseContext(), "Please Enter 10 digit phone Number", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }


    void sendOtp(final String number) {
        String url = ApiUtil.getOtp();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        OtpDialog alert = new OtpDialog();
                        alert.showDialog(changePhone.this, 0);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", number);
                return params;
            }
        };
        RequestManager.getInstance(this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(Preferences.S_OTP)) {
            Preferences.saveSetting(Preferences.S_PHONE, phone.getText().toString(), this);

        }

    }
}
