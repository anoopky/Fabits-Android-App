package in.fabits.fabits;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.Policy;
import java.util.ArrayList;
import java.util.List;

import in.fabits.fabits.R;
import in.fabits.fabits.adapters.CustomGrid;
import in.fabits.fabits.adapters.PoolCustomGrid;
import in.fabits.fabits.adapters.SearchAdapter;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.EmojiesV1;
import in.fabits.fabits.api.IntentKeys;
import in.fabits.fabits.api.RequestManager;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.model.Pool;

public class PoolActivity extends AppCompatActivity {

    List<Pool> mPools = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool);

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
        getSupportActionBar().setTitle("Pools");
        OnlinePools();
    }

    private void OnlinePools() {
        String url = ApiUtil.getPostsPool();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            mPools.clear();
                            for (int i = 0; i < arr.length(); i++) {
                                mPools.add(Utils.poolHelper(arr, i));
                            }
                            getSupportActionBar().setSubtitle(mPools.size()+" Colleges");
                            PoolCustomGrid adapter = new PoolCustomGrid(getBaseContext(), mPools);
                            GridView grid = (GridView) findViewById(R.id.grid);
                            grid.setAdapter(adapter);

                        } catch (JSONException | URISyntaxException | MalformedURLException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestManager.getInstance(this).addToRequestQueue(stringRequest);
    }
}
