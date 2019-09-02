package in.fabits.fabits;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.widget.IconButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pusher.client.channel.SubscriptionEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.fabits.fabits.adapters.NotificationAdapter;
import in.fabits.fabits.adapters.SectionsPagerAdapter;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.IntentKeys;
import in.fabits.fabits.api.NotificationUtils;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.RequestManager;
import in.fabits.fabits.api.Socket;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.dialogs.FaceMatch;
import in.fabits.fabits.model.Notification;
import in.fabits.fabits.placeholder.PlaceholderFragment;

import static in.fabits.fabits.ChatActivity.Conversation_ID;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SharedPreferences.OnSharedPreferenceChangeListener
//        ,SharedPreferences , SharedPreferences.OnSharedPreferenceChangeListener
{
    NotificationAdapter notificationAdapter;
    RecyclerView recyclerView;
    public static List<String> onlineConversation = new ArrayList<>();
    List<Notification> mNotification = new ArrayList<>();
    FloatingActionButton mTextPost;
    FloatingActionMenu fabMenu;
    FloatingActionButton mImagePost;
    private CircleImageView user_image;
    private ImageLoader imageLoader;
    Activity mActivity;
    private Thread t;
    private SwipeRefreshLayout swipeRefreshLayoutT1;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences shared = getSharedPreferences(Preferences.LIST, 0);
        Iconify
                .with(new FontAwesomeModule());
        shared.registerOnSharedPreferenceChangeListener(this);

        Bundle b = new Bundle();
        b.putString("MESSAGE", ApiUtil.getNewMessage());
        b.putString("NOTIFICATION", ApiUtil.getNewNotification());
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(true)
                .setTrigger(Trigger.executionWindow(0, 60))
                .setExtras(b)
                .setTag("my-unique-tag")
                .build();
        dispatcher.mustSchedule(myJob);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > 1)
                    fabMenu.setVisibility(View.GONE);
                else
                    fabMenu.setVisibility(View.VISIBLE);




               if (position == 0)
                    Preferences.saveCount(Preferences.HCOUNT, "0", mActivity);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTextPost = (FloatingActionButton) findViewById(R.id.textPost);
        fabMenu = (FloatingActionMenu) findViewById(R.id.menu);
        mImagePost = (FloatingActionButton) findViewById(R.id.imagePost);

        mImagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PostImageActivity.class);
                intent.putExtra(IntentKeys.CHAT_NAME, "POST");
                startActivity(intent);
            }
        });

        mTextPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PostTextActivity.class);
                startActivity(intent);
            }
        });

//        DrawableCompat.setTint(myImageView.getDrawable(), ContextCompat.getColor(this, R.color.white));
//
        tabLayout.getTabAt(0).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_home)
                        .colorRes(R.color.white)
        );
        tabLayout.getTabAt(0).setCustomView(R.layout.badged_tab);

        tabLayout.getTabAt(1).setIcon(R.drawable.ic_trending);


        tabLayout.getTabAt(2).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_comments)
                        .colorRes(R.color.white)
        );
        tabLayout.getTabAt(2).setCustomView(R.layout.badged_tab);
        TabLayout.Tab tab1 = tabLayout.getTabAt(2);
        if (tab1 != null && tab1.getCustomView() != null) {
            TextView b1 = (TextView) tab1.getCustomView().findViewById(R.id.badge);
            if (b1 != null) {
                String notifications = "1";
                b1.setText(notifications + "");
            }
            View v = tab1.getCustomView().findViewById(R.id.badgeCotainer);
            if (v != null) {
                v.setVisibility(View.VISIBLE);
            }
        }

        tabLayout.getTabAt(3).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_group)
                        .colorRes(R.color.white)
        );

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        recyclerView = (RecyclerView) findViewById(R.id.notificationList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayoutT1 = (SwipeRefreshLayout) findViewById(R.id.refresh);

        swipeRefreshLayoutT1.setColorSchemeColors(getResources().getColor(R.color.loginBackground));
        swipeRefreshLayoutT1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                OnlineNotification();

            }
        });

        String notification = Preferences.getSavedNotification(this);
        OfflineNotification(notification);
        OnlineNotification();
        navigationView.setNavigationItemSelectedListener(this);

        if (Socket.myChannel == null) {
            new Socket();
            Socket.myChannel.bind("message", new SubscriptionEventListener() {
                @Override
                public void onEvent(String channelName, String eventName, final String data) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                JSONArray arr = new JSONArray(data);
                                JSONObject obb = new JSONObject(String.valueOf(arr.get(0)));
                                int Conversation_ID = obb.getInt("conversation_id");
                                int id = obb.getInt("id");
                                String messageData = obb.getString("message");
                                String img = obb.getString("image");

                                if (ChatActivity.Conversation_ID == Conversation_ID) {
                                } else {
                                    String abc = Preferences.getCount(Preferences.CCOUNT, getBaseContext());
                                    int hCount = 0;
                                    if (!abc.equals("[]"))
                                        hCount = Integer.parseInt(abc);

                                    Preferences.saveCount(Preferences.CCOUNT, String.valueOf(++hCount), getBaseContext());
                                    new NotificationUtils(getBaseContext(), messageData, "New Message", img).execute();

                                }

                                Recived(id, Conversation_ID);
                                Preferences.saveMessages(String.valueOf(Conversation_ID), arr, getBaseContext());
                                Utils.updateConversationListMessage(messageData, Conversation_ID, 1, getBaseContext());
                                Utils.updateConversationListMessage(messageData, Conversation_ID, 3, getBaseContext());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

            Socket.myChannel.bind("online_conversation", new SubscriptionEventListener() {
                @Override
                public void onEvent(String channelName, String eventName, final String data) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                JSONArray arr = new JSONArray(data);
                                JSONObject obb = new JSONObject(String.valueOf(arr.get(0)));
                                String Conversation_ID = obb.getString("conversation_id");
                                Preferences.saveGreenBarStatus(Conversation_ID, arr, getBaseContext());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });


            Socket.fabitsChannel.bind("posts", new SubscriptionEventListener() {
                @Override
                public void onEvent(String channelName, String eventName, final String data) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            String abc = Preferences.getCount(Preferences.HCOUNT, getBaseContext());
                            int hCount = 0;
                            if (!abc.equals("[]"))
                                hCount = Integer.parseInt(abc);

                            PlaceholderFragment.P_INIT = -1;
                            Preferences.saveCount(Preferences.HCOUNT, String.valueOf(++hCount), getBaseContext());

                        }
                    });
                }
            });


            Socket.myChannel.bind("SeenDeliverChange", new SubscriptionEventListener() {
                @Override
                public void onEvent(String channelName, String eventName, final String data) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                JSONArray arr = new JSONArray(data);
                                JSONObject obb = new JSONObject(String.valueOf(arr.get(0)));
                                String conversationId = obb.getString("conversation_id");

                                Preferences.saveSeen(conversationId, data, getBaseContext());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            });
        }
        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions opts = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(opts).build();
        ImageLoader.getInstance().init(config);

        View hView = navigationView.getHeaderView(0);
        user_image = (CircleImageView) hView.findViewById(R.id.nav_left_image);
        String userImage = ApiUtil.getProfileSmall();
        imageLoader.displayImage(userImage, user_image);
        TextView name = (TextView) hView.findViewById(R.id.name);
        TextView username = (TextView) hView.findViewById(R.id.username);

        name.setText(ApiUtil.getName());
        username.setText("@" + ApiUtil.getUserName());
        mActivity = this;
        checkFaceMatch();
        Suggestions(1);
//        WindowManager.LayoutParams  prams = new WindowManager.LayoutParams();
//        prams.copyFrom(alert.)
//        getWindow().setAttributes(prams);

        if (t == null) {
            t = new Thread(new Runnable() {
                public void run() {
                    for (; ; ) {
                        NotificationListThread();
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

        shared.registerOnSharedPreferenceChangeListener(this);

        HomeCounter();

        NotificationCount();


        ConversationCounter();
    }


    private static PendingIntent contentIntent(Context context) {

        Intent startActivityIntent = new Intent(context, LoginActivity.class);

        return PendingIntent.getActivities(context,
                10002,
                new Intent[]{startActivityIntent},
                PendingIntent.FLAG_UPDATE_CURRENT);

    }

    void Suggestions(final int init) {

        String url = ApiUtil.getSuggestion(init);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray arr = new JSONArray(response);

                            Preferences.saveSuggestions(arr, getBaseContext());


                        } catch (JSONException e) {
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


    private void Recived(final int id, final int Conversation_ID) {

        if (id != -1) {
            final String url = ApiUtil.getReadConversationUrl();

            if (Conversation_ID != -1) {
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
                        params.put("conversationId", String.valueOf(Conversation_ID));
                        params.put("lastReceived", String.valueOf(id));
                        return params;
                    }
                };
                RequestManager.getInstance(this).addToRequestQueue(stringRequest);
            }
        }
    }

    void OnlineChatList() {
        String url = ApiUtil.getChatListUrl();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            Preferences.saveConversationList(arr, getBaseContext());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestManager.getInstance(getBaseContext()).addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.moveTaskToBack(true);
        }
    }

    MenuItem menuItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);

        menuItem = menu.findItem(R.id.action_notification);
        menuItem.setIcon(buildCounterDrawable(0, R.drawable.ic_notify));
        return true;
    }


    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.badge_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_notification) {
            Preferences.saveCount(Preferences.NCOUNT, "0", getBaseContext());

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.openDrawer(GravityCompat.END);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            intent.putExtra(IntentKeys.PROFILE_ID, ApiUtil.getUserId());
            startActivity(intent);

        } else if (id == R.id.nav_status) {

            Intent intent = new Intent(HomeActivity.this, StatusActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_random) {

            Intent intent = new Intent(HomeActivity.this, goRandomActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_search) {

            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {

            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_pool) {

            Intent intent = new Intent(HomeActivity.this, PoolActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void OnlineNotification() {
        String url = ApiUtil.getNotificationUrl();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            swipeRefreshLayoutT1.setRefreshing(false);
                            JSONArray arr = new JSONArray(response);
                            Preferences.saveNotification(arr, getBaseContext());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayoutT1.setRefreshing(false);

            }
        });
        RequestManager.getInstance(this).addToRequestQueue(stringRequest);
    }

    void NotificationListThread() {

        String url = ApiUtil.getNewNotification();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (!response.equals("[]")) {

                                String abc = Preferences.getCount(Preferences.NCOUNT, getBaseContext());
                                int hCount = 0;
                                if (!abc.equals("[]"))
                                    hCount = Integer.parseInt(abc);

                                Preferences.saveCount(Preferences.NCOUNT, String.valueOf(++hCount), getBaseContext());


                                JSONArray arr = new JSONArray(response);
                                JSONObject obb = arr.getJSONObject(0);
                                String img = obb.getString("image");
                                new NotificationUtils(getBaseContext(), obb.getString("message"), "New Notification", img).execute();
                                Preferences.addNotification(arr, getBaseContext());
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
        RequestManager.getInstance(this).addToRequestQueue(stringRequest);
    }

    void checkFaceMatch() {
        String url = ApiUtil.getCheckFaceMatch();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject arr = new JSONObject(response);
                            JSONObject arr1 = arr.getJSONObject("user_1");
                            JSONObject arr2 = arr.getJSONObject("user_2");
                            int uid1 = arr1.getInt("id");
                            int fid = arr1.getInt("fid");
                            int uid2 = arr2.getInt("id");
                            FaceMatch alert = new FaceMatch();
                            alert.showDialog(mActivity, arr1.getString("profile_picture_big"), arr2.getString("profile_picture_big"), arr1.getString("name"), arr2.getString("name"),
                                    uid1, uid2, fid);
                        } catch (JSONException e) {
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


    void OfflineNotification(String response) {

        try {
            JSONArray arr = new JSONArray(response);

            mNotification.clear();
            for (int i = 0; i < arr.length(); i++) {
                mNotification.add(Utils.helperNotificationList(arr, i));
            }
            if (recyclerView.getAdapter() == null) {
                notificationAdapter = new NotificationAdapter(mNotification);
                recyclerView.setAdapter(notificationAdapter);
            } else {
                notificationAdapter.setNotification(mNotification);
                notificationAdapter.notifyDataSetChanged();
            }

        } catch (JSONException | URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(Preferences.NOTIFICATION)) {
            String chatList = Preferences.getSavedNotification(this);
            if (recyclerView != null)
                OfflineNotification(chatList);

        } else if (key.equals(Preferences.HCOUNT)) {

            HomeCounter();

        } else if (key.equals(Preferences.NCOUNT)) {
            NotificationCount();

        } else if (key.equals(Preferences.CCOUNT)) {

            ConversationCounter();
        }


    }


    void HomeCounter() {

        String hCount = Preferences.getCount(Preferences.HCOUNT, this);

        TabLayout.Tab tab = tabLayout.getTabAt(0);

        if (tab != null && tab.getCustomView() != null) {
            TextView b1 = (TextView) tab.getCustomView().findViewById(R.id.badge);
            if (b1 != null) {
                b1.setText(hCount);
            }
            View v = tab.getCustomView().findViewById(R.id.badgeCotainer);
            if (v != null) {
                v.setVisibility(View.VISIBLE);
            }

            if (!hCount.equals("[]")) {

                if (Integer.parseInt(hCount) <= 0) {
                    if (v != null) {
                        v.setVisibility(View.GONE);
                    }
                }

            } else
                Preferences.saveCount(Preferences.HCOUNT, "0", getBaseContext());
        }
    }

    void NotificationCount() {
        String hCount = Preferences.getCount(Preferences.NCOUNT, this);

        if (!hCount.equals("[]")) {

            if (menuItem != null)
                menuItem.setIcon(buildCounterDrawable(
                        Integer.parseInt(hCount),
                        R.drawable.ic_notify));

        } else
            Preferences.saveCount(Preferences.HCOUNT, "0", getBaseContext());


    }

    void ConversationCounter() {
        String hCount = Preferences.getCount(Preferences.CCOUNT, this);

        TabLayout.Tab tab = tabLayout.getTabAt(2);
        if (tab != null && tab.getCustomView() != null) {
            TextView b1 = (TextView) tab.getCustomView().findViewById(R.id.badge);
            if (b1 != null) {
                b1.setText(hCount);
            }
            View v = tab.getCustomView().findViewById(R.id.badgeCotainer);
            if (v != null) {
                v.setVisibility(View.VISIBLE);
            }

            if (!hCount.equals("[]")) {

                if (Integer.parseInt(hCount) <= 0) {
                    if (v != null) {
                        v.setVisibility(View.GONE);
                    }
                }

            } else
                Preferences.saveCount(Preferences.CCOUNT, "0", getBaseContext());
        }

    }
}
