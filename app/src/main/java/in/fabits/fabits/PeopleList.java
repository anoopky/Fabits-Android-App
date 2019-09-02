package in.fabits.fabits;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.fabits.fabits.adapters.FacematchListAdapter;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.IntentKeys;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.RequestManager;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.adapters.PeopleListAdapter;
import in.fabits.fabits.model.UsersList;

public class PeopleList extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<UsersList> mUsersList = new ArrayList<>();
    public int P_INIT = 1;

    int sourceId;
    String mTitle;
    String mSubTitle;
    int likesCount;
    private int visibleThreshold = 8;
    private int PlastVisibleItem, PtotalItemCount;
    private boolean Ploading;
    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_list);
        sourceId = getIntent().getIntExtra(IntentKeys.SOURCEID, -1);
        mType = getIntent().getIntExtra(IntentKeys.TYPE, -1);
        mTitle = getIntent().getStringExtra(IntentKeys.TITLE);
        mSubTitle = getIntent().getStringExtra(IntentKeys.SUBTITLE);
        likesCount = getIntent().getIntExtra(IntentKeys.LIKES_COUNT, -1);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        String SourceID = String.valueOf(sourceId);

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
        getSupportActionBar().setTitle(mTitle);
        getSupportActionBar().setSubtitle(mSubTitle);

        String likeData = Preferences.getSavedLikeList(SourceID, mType, this);
        try {
            OfflineUserList(likeData);
        } catch (JSONException | URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }
        OnlineUserList(1);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                PtotalItemCount = layoutManager.getItemCount();
                PlastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!Ploading && PtotalItemCount <= (PlastVisibleItem + visibleThreshold)) {
                    Ploading = true;
                    P_INIT++;
                    if (mUsersList.size() > 0)
                        OnlineUserList(P_INIT);
                }
            }

        });
    }

    void OnlineUserList(final int init) {
        String url;

        if (mType == -1)
            url = ApiUtil.getLikesUrl(init);

        else
            url = ApiUtil.getProfileCountLists(init);

        final String likeID = String.valueOf(sourceId);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("-1")){

                            getSupportActionBar().setSubtitle(mSubTitle+" [Private]");


                        }
                        else {
                            try {
                                JSONArray arr = new JSONArray(response);
                                if (init == 1) {
                                    mUsersList.clear();
                                    Preferences.saveLikeList(arr, likeID, mType, getBaseContext());
                                } else
                                    Preferences.addLikeList(arr, likeID, mType, getBaseContext());

                                OfflineUserList(response);
                                if (arr.length() > 0)
                                    Ploading = false;

                            } catch (JSONException | URISyntaxException | MalformedURLException e) {
                                e.printStackTrace();
                            }
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
                params.put("source_id", String.valueOf(sourceId));
                params.put("type", String.valueOf(mType));
                return params;
            }
        };
        RequestManager.getInstance(this).addToRequestQueue(stringRequest);
    }

    PeopleListAdapter peopleListAdapter;
    FacematchListAdapter facematchListAdapter;

    private void OfflineUserList(String response) throws JSONException, MalformedURLException, URISyntaxException {

        JSONArray arr = new JSONArray(response);
        for (int i = 0; i < arr.length(); i++) {
            mUsersList.add(Utils.UserListHelper(arr, i));
        }

        if (arr.length() > 0) {
            if (recyclerView.getAdapter() == null) {
                if (mType == 2) {
                    facematchListAdapter = new FacematchListAdapter(mUsersList);
                    recyclerView.setAdapter(facematchListAdapter);
                }
                else {
                    peopleListAdapter = new PeopleListAdapter(mUsersList);
                    recyclerView.setAdapter(peopleListAdapter);
                }
            } else {
                if (mType == 2) {
                    facematchListAdapter.setUserList(mUsersList);
                    facematchListAdapter.notifyDataSetChanged();
                } else {
                    peopleListAdapter.setUserList(mUsersList);
                    peopleListAdapter.notifyDataSetChanged();
                }
            }
        }
        if (arr.length() < 10)
            if (recyclerView.getAdapter() != null) {
                if (mType == 2)
                    facematchListAdapter.Done();
                else
                    peopleListAdapter.Done();
            }

    }


}
