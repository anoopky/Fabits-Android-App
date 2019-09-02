package in.fabits.fabits;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

import in.fabits.fabits.adapters.HomeAdapter;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.IntentKeys;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.RequestManager;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.model.Posts;

public class PostSingleActivity extends AppCompatActivity implements HomeAdapter.ListItemClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener {
    int postID;
    List<Posts> mNewsFeeds = new ArrayList<>();
    HomeAdapter homeAdapter;
    RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public int P_INIT = -1;

    private int visibleThreshold = 2;
    private int PlastVisibleItem, PtotalItemCount;
    private boolean Ploading;
    private String type;
    private String search;
    private String key;

    SharedPreferences pref;
    private boolean isMyReload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_single);

        type = getIntent().getStringExtra(IntentKeys.TYPE);
        search = getIntent().getStringExtra(IntentKeys.SEARCH);
        String title = getIntent().getStringExtra(IntentKeys.TITLE);
        String subtitle = getIntent().getStringExtra(IntentKeys.SUBTITLE);
        key = type + search;
        pref = getSharedPreferences(Preferences.POST, 0);


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
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(subtitle);

        recyclerView = (RecyclerView) findViewById(R.id.postList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.loginBackground));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshContent();

            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                PtotalItemCount = layoutManager.getItemCount();
                PlastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!Ploading && PtotalItemCount <= (PlastVisibleItem + visibleThreshold)) {
                    Ploading = true;
                    P_INIT++;
                    if (mNewsFeeds.size() > 0)
                        OnlinePosts(P_INIT);
                }
            }

        });

        getPosts();
        pref.registerOnSharedPreferenceChangeListener(this);

    }

    private void refreshContent() {
        mNewsFeeds.clear();
        P_INIT = 1;
        OnlinePosts(P_INIT);
    }


    void getPosts() {

        String latest = Preferences.getSavedSearchPosts(key, this);

        if (Utils.isNetworkAvailable(this)) {

            if (latest != null && !latest.equals("[]")) {
                if (P_INIT == -1) {
                    mNewsFeeds.clear();
                    OfflinePosts(latest);
                } else {

                    updateHomeAdapter();
                }
            }
            if (P_INIT == -1) {
                P_INIT = 1;
                OnlinePosts(1);
            }

        } else {

            if (latest != null && !latest.equals("[]")) {
                if (P_INIT == -1) {
                    P_INIT = 1;
                    swipeRefreshLayout.setRefreshing(false);
                    mNewsFeeds.clear();
                    OnlinePosts(1);
                } else {
                    updateHomeAdapter();
                }
            } else {

                Toast.makeText(PostSingleActivity.this, getString(R.string.ErrorInternet), Toast.LENGTH_SHORT).show();
            }
        }
    }


    void OnlinePosts(int init) {

        swipeRefreshLayout.setRefreshing(true);
        String url = ApiUtil.getSearch(search, type, init);
        final int init1 = init;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        swipeRefreshLayout.setRefreshing(false);

                        try {
                            isMyReload = true;
                            if (init1 == 1) {
                                mNewsFeeds.clear();
                                Preferences.saveSearchPosts(new JSONArray(response), key, getBaseContext());

                            } else {
                                Preferences.addSearchPosts(new JSONArray(response), key, getBaseContext());

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        OfflinePosts(response);
                        Ploading = false;

                        JSONArray arr = null;
                        try {
                            arr = new JSONArray(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if ((arr != null ? arr.length() : 0) < 5) {
                            homeAdapter.Done();
                            Ploading = true;
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestManager.getInstance(this).addToRequestQueue(stringRequest);

    }

    void OfflinePosts(String response) {
        try {
            JSONArray arr = new JSONArray(response);
            for (int i = 0; i < arr.length(); i++) {

                mNewsFeeds.add(Utils.postHelper(arr, i, true));
            }
            if (arr.length() > 0) {
                updateHomeAdapter();
            }
            if (arr.length() < 5) {
                homeAdapter.Done();
                Ploading = true;

            }

        } catch (JSONException | MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }


    private void updateHomeAdapter() {
        if (recyclerView.getAdapter() == null) {
            Utils.SEARCH_ADDRESS = key;
            homeAdapter = new HomeAdapter(mNewsFeeds, Utils.SEARCH_ADDRESS, this);
            recyclerView.setAdapter(homeAdapter);
        } else {
            homeAdapter.setPosts(mNewsFeeds);
            homeAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onListItemClickPost(final int id, final String Address) {

        CharSequence[] newItem;
        Posts ActionPost = null;
        if (Address.equals(key)) {
            ActionPost = mNewsFeeds.get(id);
        } else return;

        if (ApiUtil.getUserId().equals(String.valueOf(ActionPost != null ? ActionPost.getUser_id() : 0))) {
            newItem = new CharSequence[]{"Delete", "UnFollow"};
        } else {
            newItem = new CharSequence[]{"UnFollow"};
        }
        final CharSequence[] items = newItem;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

//        builder.setTitle("Select The Action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Delete")) {
                    Posts post = mNewsFeeds.get(id);
                    final String postID = String.valueOf(post.getId());
                    String url = ApiUtil.getPostDelete();
                    postControls(postID, url);

                    if (Address.equals(key)) {
                        mNewsFeeds.remove(id);
                        isMyReload = true;
                        String res = Preferences.getSavedSearchPosts(key, getBaseContext());
                        try {
                            JSONArray arr = new JSONArray(res);
                            arr.remove(id);
                            Preferences.saveSearchPosts(arr, key,  getBaseContext());

                            updateHomeAdapter();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } else if (items[item].equals("UnFollow")) {
                    Posts post = mNewsFeeds.get(id);
                    final String postID = String.valueOf(post.getId());
                    String url = ApiUtil.getUnFollowPost();
                    postControls(postID, url);
                }

            }
        });
        builder.show();

    }

    private void postControls(final String postID, final String url) {
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
                params.put("post_id", postID);
                return params;
            }
        };
        RequestManager.getInstance(this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String P_key) {

        if (P_key.equals(key)) {
            if (recyclerView != null && !isMyReload) {
                String PostLatest = Preferences.getSavedSearchPosts(key, this);
                mNewsFeeds.clear();
                OfflinePosts(PostLatest);
            } else
                isMyReload = false;
        }


    }
}




