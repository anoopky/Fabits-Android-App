package in.fabits.fabits.placeholder;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.fabits.fabits.R;
import in.fabits.fabits.adapters.ChatListAdapter;
import in.fabits.fabits.adapters.HomeAdapter;
import in.fabits.fabits.adapters.NotificationAdapter;
import in.fabits.fabits.adapters.OnlineListAdapter;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.RequestManager;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.model.ChatList;
import in.fabits.fabits.model.Online;
import in.fabits.fabits.model.Posts;


public class PlaceholderFragment extends Fragment
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        ChatListAdapter.ListItemLongClickListener,
        HomeAdapter.ListItemClickListener {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static int T_INIT = -1;
    public static int P_INIT = -1;
    public static int C_INIT = -1;

    List<Posts> mNewsFeeds = new ArrayList<>();
    List<Posts> mTrending = new ArrayList<>();
    List<ChatList> mChatList = new ArrayList<>();
    List<Online> mOnline = new ArrayList<>();
    HomeAdapter homeAdapter;
    HomeAdapter TrendAdapter;
    ChatListAdapter chatListAdapter;
    OnlineListAdapter onlineAdapter;

    RecyclerView recyclerViewT1;
    RecyclerView recyclerViewT2;
    RecyclerView recyclerViewT3;
    RecyclerView recyclerViewT4;
    private SwipeRefreshLayout swipeRefreshLayoutT1;
    private SwipeRefreshLayout swipeRefreshLayoutT2;
    private SwipeRefreshLayout swipeRefreshLayoutT3;
    private SwipeRefreshLayout swipeRefreshLayoutT4;

    private int visibleThreshold = 2;
    private int PlastVisibleItem, PtotalItemCount;
    private boolean Ploading;
    private boolean isMyReload = false;

    private int TlastVisibleItem, TtotalItemCount;
    private boolean Tloading;

    SharedPreferences pref;

    SharedPreferences shared;


    boolean isMe = false;
    boolean isReady = false;
    private boolean isMyReloadT = false;

    Thread t;


    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Bundle args = new Bundle(getArguments());
        int TabID = args.getInt(ARG_SECTION_NUMBER);
        pref = getContext().getSharedPreferences(Preferences.POST, 0);
        shared = getContext().getSharedPreferences(Preferences.LIST, 0);


        if (TabID == 1) {

            recyclerViewT1 = (RecyclerView) rootView.findViewById(R.id.rv_numbers);
            swipeRefreshLayoutT1 = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerViewT1.setLayoutManager(layoutManager);
            recyclerViewT1.setHasFixedSize(false);

            getPosts();


            swipeRefreshLayoutT1.setColorSchemeColors(getResources().getColor(R.color.loginBackground));
            swipeRefreshLayoutT1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    try {
                        refreshContentT1();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            recyclerViewT1.addOnScrollListener(new RecyclerView.OnScrollListener() {

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


        } else if (TabID == 2) {

            swipeRefreshLayoutT2 = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
            recyclerViewT2 = (RecyclerView) rootView.findViewById(R.id.rv_numbers);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerViewT2.setLayoutManager(layoutManager);
            recyclerViewT2.setHasFixedSize(true);
            getTrend();


            swipeRefreshLayoutT2.setColorSchemeColors(getResources().getColor(R.color.loginBackground));
            swipeRefreshLayoutT2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshContentT2();
                }
            });

            recyclerViewT2.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    TtotalItemCount = layoutManager.getItemCount();
                    TlastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    if (!Tloading && TtotalItemCount <= (TlastVisibleItem + visibleThreshold)) {
                        Tloading = true;
                        T_INIT++;
                        if (mTrending.size() > 0)
                            OnlineTrend(T_INIT);
                    }
                }

            });


        } else if (TabID == 3) {
            swipeRefreshLayoutT3 = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
            recyclerViewT3 = (RecyclerView) rootView.findViewById(R.id.rv_numbers);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerViewT3.setLayoutManager(layoutManager);
            recyclerViewT3.setHasFixedSize(true);
            getChatList();
            swipeRefreshLayoutT3.setColorSchemeColors(getResources().getColor(R.color.loginBackground));
            swipeRefreshLayoutT3.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshContentT3();
                }
            });

        } else {

//            getChatList();
            swipeRefreshLayoutT4 = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
            recyclerViewT4 = (RecyclerView) rootView.findViewById(R.id.rv_numbers);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerViewT4.setLayoutManager(layoutManager);
            recyclerViewT4.setHasFixedSize(true);
            getOnlineList();


            swipeRefreshLayoutT4.setColorSchemeColors(getResources().getColor(R.color.loginBackground));
            swipeRefreshLayoutT4.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshContentT4();
                }
            });

        }

        defaultSetup();

        if (t == null) {
            t = new Thread(new Runnable() {
                public void run() {
                    for (; ; ) {
                        OnlineListThread();
                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            t.start();
        }
        return rootView;
    }

    private void refreshContentT4() {

        OnlineList();
    }

    private void refreshContentT3() {
        isNewMessage();

        OnlineChatList();


    }

    private void refreshContentT2() {

        T_INIT = 1;
        OnlineTrend(T_INIT);

    }

    private void refreshContentT1() throws JSONException {

        P_INIT = 1;
        OnlinePosts(P_INIT);
    }

    void getPosts() {

        String latest = Preferences.getSavedHomePosts(getContext());

        if (Utils.isNetworkAvailable(getContext())) {

            if (latest != null && !latest.equals("[]")) {
                if (P_INIT == -1 || recyclerViewT1.getAdapter()==null) {
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
                if (P_INIT == -1 || recyclerViewT1.getAdapter()==null) {
                    P_INIT = 1;
                    swipeRefreshLayoutT1.setRefreshing(false);
                    mNewsFeeds.clear();
                    OnlinePosts(1);
                } else {
                    updateHomeAdapter();
                }
            } else {

                Toast.makeText(getContext(), getString(R.string.ErrorInternet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    void OnlinePosts(int init) {

        if (Utils.isNetworkAvailable(getContext())) {

            swipeRefreshLayoutT1.setRefreshing(true);
            String url = ApiUtil.getPostUrl(init);
            final int init1 = init;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            swipeRefreshLayoutT1.setRefreshing(false);

                            try {
                                isMyReload = true;

                                if (init1 == 1) {
                                    Preferences.saveCount(Preferences.HCOUNT, "0", getContext());


                                    mNewsFeeds.clear();
                                    Preferences.saveHomePosts(new JSONArray(response), getContext());

                                } else {
                                    Preferences.addHomePosts(new JSONArray(response), getContext());

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            OfflinePosts(response);
                            Ploading = false;
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    swipeRefreshLayoutT1.setRefreshing(false);


                }
            });
            RequestManager.getInstance(getContext()).addToRequestQueue(stringRequest);
        } else {

            String posts = Preferences.getSavedHomePosts(getContext());
            try {
                JSONArray arr1 = new JSONArray(posts);
                mNewsFeeds.clear();
                int limit = init * 5;
                if (limit > arr1.length())
                    limit = arr1.length();
                JSONArray response = new JSONArray();
                for (int i = 0; i < limit; i++) {
                    response.put(arr1.get(i));
                }
                OfflinePosts(response.toString());
                Ploading = false;

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    void OfflinePosts(String trending) {
        try {
            JSONArray arr = new JSONArray(trending);
            for (int i = 0; i < arr.length(); i++) {

                mNewsFeeds.add(Utils.postHelper(arr, i, true));
            }
            if (arr.length() > 0) {
                updateHomeAdapter();
            }

        } catch (JSONException | MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void updateHomeAdapter() {
        if (recyclerViewT1.getAdapter() == null) {
            homeAdapter = new HomeAdapter(mNewsFeeds, Utils.POST_ADDRESS, this);
            recyclerViewT1.setAdapter(homeAdapter);
//            SnapHelper helper = new LinearSnapHelper();
//            helper.attachToRecyclerView(recyclerViewT1);
        } else {
            homeAdapter.setPosts(mNewsFeeds);
            homeAdapter.notifyDataSetChanged();
        }
    }

    void getTrend() {

        String trending = Preferences.getSavedTrendingPosts(getContext());

        if (Utils.isNetworkAvailable(getContext())) {

            if (trending != null && !trending.equals("[]")) {
                if (T_INIT == -1 || recyclerViewT2.getAdapter()==null) {
                    mTrending.clear();
                    OfflineTrend(trending);
                } else {
                    updateTrendAdapter();
                }
            }
            if (T_INIT == -1) {
                T_INIT = 1;
                OnlineTrend(T_INIT);
            }

        } else {

            if (trending != null && !trending.equals("[]")) {
                if (T_INIT == -1 || recyclerViewT2.getAdapter()==null) {
                    T_INIT = 1;
                    swipeRefreshLayoutT2.setRefreshing(false);
                    mTrending.clear();
                    OnlineTrend(T_INIT);

                } else {
                    updateTrendAdapter();
                }
            } else {

                Toast.makeText(getContext(), getString(R.string.ErrorInternet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    void OnlineTrend(int init) {

        if (Utils.isNetworkAvailable(getContext())) {
            swipeRefreshLayoutT2.setRefreshing(true);
            String url = ApiUtil.getTrendUrl(init);

            final int init1 = init;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            swipeRefreshLayoutT2.setRefreshing(false);
                            try {
                                isMyReloadT = true;

                                if (init1 == 1) {
                                    mTrending.clear();
                                    Preferences.saveTrendingPosts(new JSONArray(response), getContext());

                                } else {
                                    Preferences.addTrendingPosts(new JSONArray(response), getContext());

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            OfflineTrend(response);
                            if (!response.equals("[]")) {
                                Tloading = false;
                                TrendAdapter.Done();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    swipeRefreshLayoutT2.setRefreshing(false);

                }
            });
            RequestManager.getInstance(getContext()).addToRequestQueue(stringRequest);
        } else {

            String trending = Preferences.getSavedTrendingPosts(getContext());
            try {
                JSONArray arr1 = new JSONArray(trending);
                mTrending.clear();
                int limit = init * 5;
                if (limit > arr1.length()) {
                    limit = arr1.length();
                    TrendAdapter.Done();
                }
                JSONArray response = new JSONArray();
                for (int i = 0; i < limit; i++) {

                    response.put(arr1.get(i));
                }
                OfflineTrend(response.toString());
                Tloading = false;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    void OfflineTrend(String trending) {
        try {
            JSONArray arr = new JSONArray(trending);
            for (int i = 0; i < arr.length(); i++) {
                mTrending.add(Utils.postHelper(arr, i, true));
            }
            if (arr.length() > 0) {
                updateTrendAdapter();
            } else {
                if (TrendAdapter != null)
                    TrendAdapter.Done();
                updateTrendAdapter();

            }
        } catch (JSONException | MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void updateTrendAdapter() {
        if (recyclerViewT2.getAdapter() == null) {
            TrendAdapter = new HomeAdapter(mTrending, Utils.TREND_ADDRESS, this);
            recyclerViewT2.setAdapter(TrendAdapter);
        } else {
            TrendAdapter.setPosts(mTrending);
            TrendAdapter.notifyDataSetChanged();
        }
    }

    void getChatList() {

        String chatList = Preferences.getSavedConversationList(getContext());
        if (Utils.isNetworkAvailable(getContext())) {
            OfflineChatList(chatList);
            if (C_INIT == -1) {
                isNewMessage();
                OnlineChatList();
                C_INIT = 1;
            }
        } else {
            OfflineChatList(chatList);
        }
    }

    void OfflineChatList(String chatList) {
        try {
            JSONArray arr = new JSONArray(chatList);
            mChatList.clear();
            int count=0;
            for (int i = 0; i < arr.length(); i++) {
                mChatList.add(Utils.helperChatList(arr, i));
                count += Utils.helperChatCount(arr, i);
            }
            Preferences.saveCount(Preferences.CCOUNT, String.valueOf(count), getContext());


            if (recyclerViewT3.getAdapter() == null) {
                chatListAdapter = new ChatListAdapter(mChatList, this);
                recyclerViewT3.setAdapter(chatListAdapter);
            } else {
                chatListAdapter.notifyDataSetChanged();
            }
        } catch (JSONException | URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    void OnlineChatList() {
        swipeRefreshLayoutT3.setRefreshing(true);
        String url = ApiUtil.getChatListUrl();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        isMe = true;
                        swipeRefreshLayoutT3.setRefreshing(false);
                        try {
                            JSONArray arr = new JSONArray(response);
                            Preferences.saveConversationList(arr, getContext());
                            OfflineChatList(arr.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayoutT3.setRefreshing(false);

            }
        });
        RequestManager.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    void isNewMessage() {
        String url = ApiUtil.getNewMessage();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = new JSONObject(String.valueOf(arr.get(i)));
                                String conversation_ID = (String) obj.get("conversation_id");
                                JSONArray msg = new JSONArray();
                                msg.put(arr.get(i));
                                Preferences.saveMessages(conversation_ID, msg, getContext());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestManager.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    void getOnlineList() {

        SharedPreferences pref1 = getContext().getSharedPreferences(Preferences.LIST, 0);
        String online = pref1.getString(Preferences.ONLINE, null);

        if (online != null && !online.equals("[]")) {
            swipeRefreshLayoutT4.setRefreshing(false);

            OfflineList(online);
        } else {

            OnlineList();
        }

    }

    void OfflineList(String chatList) {
        try {
            JSONArray arr = new JSONArray(chatList);

            mOnline.clear();
            for (int i = 0; i < arr.length(); i++) {
                mOnline.add(Utils.helperOnlineList(arr, i));
            }

            if (recyclerViewT4.getAdapter() == null) {
                onlineAdapter = new OnlineListAdapter(mOnline, mOnline.size());
                recyclerViewT4.setAdapter(onlineAdapter);
            } else
                onlineAdapter.notifyDataSetChanged();


        } catch (JSONException | URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    void OnlineList() {

        swipeRefreshLayoutT4.setRefreshing(true);

        String url = ApiUtil.getOnlineListUrl();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        swipeRefreshLayoutT4.setRefreshing(false);
                        SharedPreferences pref = getContext().getSharedPreferences(Preferences.LIST, 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(Preferences.ONLINE, response);
                        editor.apply();
                        OfflineList(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayoutT4.setRefreshing(false);

            }
        });
        RequestManager.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


    void OnlineListThread() {


        String url = ApiUtil.getOnlineListUrl();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        SharedPreferences pref = getContext().getSharedPreferences(Preferences.LIST, 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(Preferences.ONLINE, response);
                        editor.apply();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestManager.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


    private void defaultSetup() {

        shared.registerOnSharedPreferenceChangeListener(this);
        pref.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Preferences.CHAT_LIST)) {
            String chatList = Preferences.getSavedConversationList(getContext());
            if (recyclerViewT3 != null)
                OfflineChatList(chatList);
        } else if (key.equals(Preferences.POST_LATEST)) {
            if (recyclerViewT1 != null && !isMyReload) {
                String PostLatest = Preferences.getSavedHomePosts(getContext());
                mNewsFeeds.clear();
                OfflinePosts(PostLatest);
            } else
                isMyReload = false;
        } else if (key.equals(Preferences.POST_TRENDING)) {
            if (recyclerViewT2 != null && !isMyReloadT) {
                String trendingPosts = Preferences.getSavedTrendingPosts(getContext());
                mTrending.clear();
                OfflineTrend(trendingPosts);
            } else
                isMyReloadT = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        shared.unregisterOnSharedPreferenceChangeListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();
        if (recyclerViewT1 != null && recyclerViewT1.getAdapter() != null)
            getPosts();
        if (recyclerViewT2 != null && recyclerViewT2.getAdapter() != null)
            getTrend();
        if (recyclerViewT3 != null && recyclerViewT3.getAdapter() != null)
            getChatList();
        if (recyclerViewT4 != null && recyclerViewT4.getAdapter() != null)
            getOnlineList();
    }

    @Override
    public void onListItemClick(final int id) {
        final CharSequence[] items = {"Delete",};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

//        builder.setTitle("Select The Action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {

                    String url = ApiUtil.getConversationDelete();
                    final String FinalConversation = String.valueOf(mChatList.get(id).getConversation_id());
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
                            params.put("conversation_Id", FinalConversation);
                            return params;
                        }
                    };
                    RequestManager.getInstance(getContext()).addToRequestQueue(stringRequest);

                    mChatList.remove(id);
                    String res = Preferences.getSavedConversationList(getContext());
                    try {
                        JSONArray arr = new JSONArray(res);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            arr.remove(id);
                        }
                        Preferences.saveConversationList(arr, getContext());
                        OfflineChatList(arr.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        builder.show();

    }

    @Override
    public void onListItemClickPost(final int id, final String Address) {

        CharSequence[] newItem;
        Posts ActionPost = null;
        if (Address.equals(Utils.POST_ADDRESS)) {
            ActionPost = mNewsFeeds.get(id);
        } else if (Address.equals(Utils.TREND_ADDRESS)) {
            ActionPost = mTrending.get(id);
        }
        if (ApiUtil.getUserId().equals(String.valueOf(ActionPost != null ? ActionPost.getUser_id() : 0))) {
            newItem = new CharSequence[]{"Delete", "UnFollow"};
        } else {
            newItem = new CharSequence[]{"UnFollow"};
        }
        final CharSequence[] items = newItem;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

//        builder.setTitle("Select The Action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Delete")) {
                    Posts post = mNewsFeeds.get(id);
                    final String postID = String.valueOf(post.getId());
                    String url = ApiUtil.getPostDelete();
                    postControls(postID, url);

                    if (Address.equals(Utils.POST_ADDRESS)) {
                        mNewsFeeds.remove(id);
                        String res = Preferences.getSavedHomePosts(getContext());
                        try {
                            JSONArray arr = new JSONArray(res);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                arr.remove(id);
                            }
                            isMyReload = true;
                            Preferences.saveHomePosts(arr, getContext());
                            getPosts();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (Address.equals(Utils.TREND_ADDRESS)) {
                        mTrending.remove(id);
                        String res = Preferences.getSavedTrendingPosts(getContext());
                        try {
                            JSONArray arr = new JSONArray(res);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                arr.remove(id);
                            }
                            isMyReloadT = true;
                            Preferences.saveTrendingPosts(arr, getContext());
                            getTrend();
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
        RequestManager.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

}