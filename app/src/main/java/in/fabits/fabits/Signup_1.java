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

public class Signup_1 extends AppCompatActivity {

    Button save;
    Button skip;
    EditText pass;
    EditText Cpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_1);
        save = (Button) findViewById(R.id.next);
        skip = (Button) findViewById(R.id.skip);
        pass = (EditText) findViewById(R.id.newPassword);
        Cpass = (EditText) findViewById(R.id.newPasswordC);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkAvailable(getBaseContext())) {
                    saveStep(pass.getText().toString(), Cpass.getText().toString());
                } else {
                    Toast.makeText(getBaseContext(), "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkAvailable(getBaseContext())) {
                    skipStep();
                } else {
                    Toast.makeText(getBaseContext(), "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void skipStep() {

        String url = ApiUtil.getSignUpPasswordSkip();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("1")) {
                            Intent intent = new Intent(Signup_1.this, Signup_2.class);
                            startActivity(intent);
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("Account", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putInt("status", 1);
                            editor.apply();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        RequestManager.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void saveStep(final String newPassword, final String confirmPassword) {
        String url = ApiUtil.getSignUpPassword();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("1")) {
                            Intent intent = new Intent(Signup_1.this, Signup_2.class);
                            startActivity(intent);
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("Account", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putInt("status", 1);
                            editor.apply();
                        } else
                            Toast.makeText(getBaseContext(), response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("password", newPassword);
                params.put("confirm_password", confirmPassword);
                return params;
            }
        };
        RequestManager.getInstance(this).addToRequestQueue(stringRequest);

    }


    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

}
