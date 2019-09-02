package in.fabits.fabits;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.RequestManager;
import in.fabits.fabits.api.Utils;

public class Signup_3 extends AppCompatActivity {

    private int year = -1;
    private int month = -1;
    private int day = -1;
    private int DATE_PICKER_ID = 90;

    ImageView male;
    ImageView female;
    int gender = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_3);

        male = (ImageView) findViewById(R.id.male);
        female = (ImageView) findViewById(R.id.female);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkAvailable(getBaseContext())) {
                    gender = 1;
                    SelectDate();
                } else {
                    Toast.makeText(getBaseContext(), "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.isNetworkAvailable(getBaseContext())) {
                    gender = 0;
                    SelectDate();
                } else {
                    Toast.makeText(getBaseContext(), "No Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void SelectDate() {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        showDialog(90);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 90:

                return new DatePickerDialog(this, pickerListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            String url = ApiUtil.getSignUpGenderDob();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.equals("1")) {
                                Intent intent = new Intent(Signup_3.this, Signup_4.class);
                                startActivity(intent);
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("Account", 0);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putInt("status", 3);
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
                    params.put("gender", String.valueOf(gender));
                    params.put("birthday_day", String.valueOf(day));
                    params.put("birthday_month", String.valueOf(month));
                    params.put("birthday_year", String.valueOf(year));
                    return params;
                }
            };
            RequestManager.getInstance(getBaseContext()).addToRequestQueue(stringRequest);

        }
    };


}
