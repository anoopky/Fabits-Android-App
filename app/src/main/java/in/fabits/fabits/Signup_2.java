package in.fabits.fabits;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.RequestManager;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.dialogs.OtpDialog;
import in.fabits.fabits.settings.changePhone;

public class Signup_2 extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    Button save;
    EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_2);

        SharedPreferences shared = getSharedPreferences(Preferences.getDefaultSharedPreferencesName(this), 0);

        shared.registerOnSharedPreferenceChangeListener(this);

        save = (Button) findViewById(R.id.next);
        phone = (EditText) findViewById(R.id.phone);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNum = phone.getText().toString();

                if (Utils.isNetworkAvailable(getBaseContext())) {
                    if(phoneNum.length()== 10) {
                        sendOtp(phone.getText().toString());
                        OtpDialog alert = new OtpDialog();
                        alert.showDialog(Signup_2.this, 1);
                    } else {
                        Toast.makeText(getBaseContext(), "Please Enter 10 digit phone Number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "No Internet", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(Preferences.S_OTP)) {

            Intent intent = new Intent(Signup_2.this, Signup_3.class);
            startActivity(intent);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("Account", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("status", 2);
            editor.apply();
            Preferences.saveSetting(Preferences.S_PHONE, phone.getText().toString(), this);

        }

    }
}
