package in.fabits.fabits;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.IntentKeys;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.RequestManager;
import in.fabits.fabits.api.Utils;

public class Signup_4 extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    CircleImageView image;
    Button save;
    Button skip;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_4);
        save = (Button) findViewById(R.id.next);
        skip = (Button) findViewById(R.id.skip);
        image = (CircleImageView) findViewById(R.id.image);


        SharedPreferences shared = getSharedPreferences("Account", 0);

        shared.registerOnSharedPreferenceChangeListener(this);

        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions opts = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(opts).build();
        ImageLoader.getInstance().init(config);

        imageLoader.displayImage(ApiUtil.getProfileSmall(), image);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.isNetworkAvailable(getBaseContext())) {
                    Intent intent = new Intent(Signup_4.this, PostImageActivity.class);
                    intent.putExtra(IntentKeys.CHAT_NAME, "S_PROFILE");
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkAvailable(getBaseContext())) {
                    saveStep(1);
                } else {
                    Toast.makeText(getBaseContext(), "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void saveStep(final int i) {
        String url = ApiUtil.getSignUpProfilePicSkip();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("1")) {
                            Intent intent = new Intent(Signup_4.this, HomeActivity.class);
                            startActivity(intent);
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("Account", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putInt("status", 4);
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
                Map<String, String> params = new HashMap<>();
                params.put("step", String.valueOf(i));
                return params;
            }
        };
        RequestManager.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals("profileSmall")) {
            imageLoader.displayImage(ApiUtil.getProfileSmall(), image);

        }
    }
}
