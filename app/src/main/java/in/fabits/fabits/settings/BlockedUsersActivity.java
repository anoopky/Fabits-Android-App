package in.fabits.fabits.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import in.fabits.fabits.R;
import in.fabits.fabits.adapters.BlockPeopleListAdapter;
import in.fabits.fabits.adapters.PeopleListAdapter;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.RequestManager;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.model.UsersList;

public class BlockedUsersActivity extends AppCompatActivity implements  BlockPeopleListAdapter.ListItemClickListener{

    private RecyclerView recyclerView;
    List<UsersList> mUsersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_users);
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
        getSupportActionBar().setTitle("Blocked Accounts");

        recyclerView = (RecyclerView) findViewById(R.id.blockRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        OnlineBlocks();
    }

    void OnlineBlocks() {

        String url = ApiUtil.getMy_blocks_list();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            OfflineComments(arr);
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

    private void OfflineComments(JSONArray arr) throws MalformedURLException, JSONException, URISyntaxException {
        for (int i = 0; i < arr.length(); i++) {
            mUsersList.add(Utils.UserListHelper(arr, i));
        }

        if (arr.length() > 0) {
            BlockPeopleListAdapter peopleListAdapter= new BlockPeopleListAdapter(mUsersList, this);
            recyclerView.setAdapter(peopleListAdapter);
        }
    }

    @Override
    public void onListItemClick(int id) {

        int uid = mUsersList.get(id).getUserID();
        Utils.block(uid, this);
        mUsersList.remove(id);
        BlockPeopleListAdapter peopleListAdapter= new BlockPeopleListAdapter(mUsersList, this);
        recyclerView.setAdapter(peopleListAdapter);
    }
}
