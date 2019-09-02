package in.fabits.fabits;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.fabits.fabits.adapters.HomeAdapter;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.IntentKeys;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.RequestManager;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.model.Block;
import in.fabits.fabits.model.Following;
import in.fabits.fabits.model.Posts;
import in.fabits.fabits.model.Profile;

public class ProfileActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener,
        HomeAdapter.ListItemClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.6f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private TextView mSubTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    String key;
    int INIT = 1;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading = false;

    List<Posts> mUserPostFeeds = new ArrayList<>();
    HomeAdapter profilePostAdapter;
    RecyclerView recyclerView;
    ImageView mWall;
    LinearLayout userFollowerList;
    LinearLayout userFollowingList;
    LinearLayout userFaceMatchList;
    CircleImageView mProfilePic;
    TextView mProfileName;
    TextView mProfileStatus;
    TextView mRelationship;
    TextView mFollowers;
    TextView mFollowing;
    TextView mViews;
    TextView mFaceMatch;
    TextView mLocation;
    TextView mPhone;
    TextView mBirthday;
    ImageLoader imageLoader;
    NestedScrollView nestedScroll;
    private boolean isDone = false;
    FloatingActionMenu fabMenu;
    private String userID;
    FloatingActionButton follow;
    FloatingActionButton chat;
    FloatingActionButton achat;
    SharedPreferences pref;
    private boolean isMyReload = false;
    private ImageView mLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        bindActivity();
        mAppBarLayout.addOnOffsetChangedListener(this);
        userID = getIntent().getStringExtra(IntentKeys.PROFILE_ID);
        key = Utils.profileSearch + userID;
        pref = getSharedPreferences(Preferences.POST, 0);
        pref.registerOnSharedPreferenceChangeListener(this);


        if (userID.equals(ApiUtil.getUserId()))
            mToolbar.inflateMenu(R.menu.profile_menu_setting);

        else {

            int stat = 0;
            for (int i = 0; i < Utils.blocks.size(); i++) {
                if (userID.equals("" + Utils.blocks.get(i).getUser_id()))
                    stat = 1;
            }
            if (stat == 1) {
                mToolbar.inflateMenu(R.menu.profile_menu_unblock);


            } else {
                mToolbar.inflateMenu(R.menu.profile_menu_block);

            }


        }

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.profile_setting) {
                    Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.profile_block) {
                    BlockUser(Integer.parseInt(userID), 1);
                    return true;
                } else if (id == R.id.profile_unblock) {
                    BlockUser(Integer.parseInt(userID), 0);
                    return true;
                }
                return false;
            }
        });


        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
        startAlphaAnimation(mSubTitle, 0, View.INVISIBLE);
        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions opts = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(opts).build();
        ImageLoader.getInstance().init(config);
        try {

            getProfile();
        } catch (URISyntaxException | MalformedURLException | JSONException e) {
            e.printStackTrace();
        }
        recyclerView = (RecyclerView) findViewById(R.id.user_posts);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
//        recyclerView.getLayoutManager().setAutoMeasureEnabled(true);
        recyclerView.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        mUserPostFeeds.clear();
        getUserPosts(1);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    loading = true;
                    INIT++;
                    if (mUserPostFeeds.size() > 0 && !isDone)
                        getUserPosts(INIT);
                }
            }

        });
        userFollowerList = (LinearLayout) findViewById(R.id.userFollowerList);
        userFollowingList = (LinearLayout) findViewById(R.id.userFollowingList);
        userFaceMatchList = (LinearLayout) findViewById(R.id.userFaceMatchList);

        userFollowerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userFollowerListfun();
            }
        });

        userFollowingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userFollowingListfun();

            }
        });

        userFaceMatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userFaceMatchListfun();

            }
        });


        fabMenu = (FloatingActionMenu) findViewById(R.id.fabmenu);
        fabMenu.setIconAnimated(false);
        follow = (FloatingActionButton) findViewById(R.id.follow);
        chat = (FloatingActionButton) findViewById(R.id.chat);
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followUser(Integer.parseInt(userID));

            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoChat();

            }
        });
        if (userID.equals(ApiUtil.getUserId()))
            fabMenu.setVisibility(View.GONE);
        else {

            int stat = 0;
            for (int i = 0; i < Utils.following.size(); i++) {
                if (userID.equals("" + Utils.following.get(i).getUser_id()))
                    stat = 1;
            }
            if (stat == 1) {
                followingStyle();

            } else {
                followStyle();
            }
        }


        mProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ImageZoomActivity.class);
                intent.putExtra(IntentKeys.IMAGE_URL, p.getProfileBig().toString());
                if (ApiUtil.getUserId().equals(String.valueOf(p.getId()))) {
                    intent.putExtra(IntentKeys.IMAGE_TITLE, "Change profile pic");

                    intent.putExtra(IntentKeys.AUTH, 1);
                } else
                    intent.putExtra(IntentKeys.IMAGE_TITLE, p.getName() + "'s profile pic");

                startActivity(intent);
            }
        });

        mWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ImageZoomActivity.class);
                intent.putExtra(IntentKeys.IMAGE_URL, p.getWallBig().toString());
                intent.putExtra(IntentKeys.IMAGE_TITLE, "Change wall");
                if (ApiUtil.getUserId().equals(String.valueOf(p.getId()))) {
                    intent.putExtra(IntentKeys.IMAGE_TITLE, "Change profile Wall");

                    intent.putExtra(IntentKeys.AUTH, 2);
                } else
                    intent.putExtra(IntentKeys.IMAGE_TITLE, p.getName() + "'s wall");


                startActivity(intent);
            }
        });

    }

    private void userFaceMatchListfun() {
        Intent intent = new Intent(ProfileActivity.this, PeopleList.class);
        intent.putExtra(IntentKeys.SOURCEID, p.getId());
        intent.putExtra(IntentKeys.SUBTITLE, p.getFaceMatch());
        intent.putExtra(IntentKeys.TITLE, "FaceMatch");
        intent.putExtra(IntentKeys.TYPE, 2);
        startActivity(intent);

    }

    private void userFollowingListfun() {
        Intent intent = new Intent(ProfileActivity.this, PeopleList.class);
        intent.putExtra(IntentKeys.SOURCEID, p.getId());
        intent.putExtra(IntentKeys.SUBTITLE, p.getFollowing() + " following");
        intent.putExtra(IntentKeys.TITLE, "Following");
        intent.putExtra(IntentKeys.TYPE, 1);
        startActivity(intent);

    }

    private void userFollowerListfun() {
        Intent intent = new Intent(ProfileActivity.this, PeopleList.class);
        intent.putExtra(IntentKeys.SOURCEID, p.getId());
        intent.putExtra(IntentKeys.SUBTITLE, p.getFollowers() + " followers");
        intent.putExtra(IntentKeys.TITLE, "Followers");
        intent.putExtra(IntentKeys.TYPE, 0);
        startActivity(intent);

    }


    private void getProfile() throws MalformedURLException, JSONException, URISyntaxException {
        String profileData = Preferences.getSavedProfiles(userID, this);

        if (Utils.isNetworkAvailable(this)) {
            if (profileData != null && !profileData.equals("[]")) {
                try {
                    getOfflineProfile(profileData);
                } catch (URISyntaxException | MalformedURLException | JSONException e) {
                    e.printStackTrace();
                }
            }
            getOnlineProfile();
        } else {
            if (profileData != null) {
                getOfflineProfile(profileData);
            } else {
                Toast.makeText(this, getString(R.string.ErrorInternet), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void getOnlineProfile() throws URISyntaxException, JSONException, MalformedURLException {


        String url = ApiUtil.getProfileUrl(userID);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Preferences.saveProfiles(userID, response, getBaseContext());
                        try {
                            getOfflineProfile(response);

                        } catch (MalformedURLException | JSONException | URISyntaxException e) {
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

    Profile p;

    private void getOfflineProfile(String response) throws MalformedURLException, JSONException, URISyntaxException {
        p = profileHelper(response);
        profileBuilder(p);
    }


    private Profile profileHelper(String response) throws JSONException, URISyntaxException, MalformedURLException {
        JSONObject obj = new JSONObject(response);
        int id = obj.getInt("id");
        String username = obj.getString("username");
        String name = obj.getString("name");
        String intro = obj.getString("intro");
        String dob = obj.getString("dob");
        String pic = (String) obj.get("wall_picture_big");
        URI uri = new URI(pic);
        URL wallBig = uri.toURL();
        String pic1 = (String) obj.get("wall_picture_small");
        URI uri1 = new URI(pic1);
        URL wallSmall = uri1.toURL();
        String pic2 = (String) obj.get("profile_picture_big");
        URI uri2 = new URI(pic2);
        URL profileBig = uri2.toURL();
        String pic3 = (String) obj.get("profile_picture_small");
        URI uri3 = new URI(pic3);
        URL profileSmall = uri3.toURL();
        String college = obj.getString("college");
        String branch = obj.getString("branch");
        String year = obj.getString("year");
        int followers = obj.getInt("followers");
        int following = obj.getInt("following");
        int profile_views = obj.getInt("profile_views");
        int posts = obj.getInt("posts");
        int isBlocked = obj.getInt("isBlock");
        int isFollow = obj.getInt("isFollow");
        String faceMatch = obj.getString("faceMatch_Rating");
        String relationship = obj.getString("relationship");
        String location = obj.getString("location");
        long phone = obj.getLong("phone");
        return new Profile(id, username, name, intro, dob, wallBig, wallSmall, profileBig, profileSmall, college, branch, year, followers, following, profile_views, posts, isBlocked, isFollow, faceMatch, relationship, phone, location);
    }

    void profileBuilder(Profile p) {
        mTitle.setText(p.getName());
        if (p.getId() == Integer.parseInt(ApiUtil.getUserId())) {

            mSubTitle.setText(p.getFollowers() + " Followers");

        } else {
            mSubTitle.setText(p.getCollege() + "-" + p.getBranch() + "-" + p.getYear());
        }
        mProfileName.setText(p.getName());
        mProfileStatus.setText(Html.fromHtml(p.getIntro()).toString());
        imageLoader.displayImage(String.valueOf(p.getProfileBig()), mProfilePic);
        imageLoader.displayImage(String.valueOf(p.getWallBig()), mWall);
        mRelationship.setText(p.getRelationship());
        mFollowers.setText(String.valueOf(p.getFollowers()));
        mFollowing.setText(String.valueOf(p.getFollowing()));
        mViews.setText(String.valueOf(p.getProfile_views()));
        mFaceMatch.setText(p.getFaceMatch());
        mLocation.setText(p.getLocation());
        if (p.getPhone() == -1) {
            mPhone.setVisibility(View.GONE);
            mLock.setVisibility(View.VISIBLE);
        } else {
            mPhone.setVisibility(View.VISIBLE);
            mLock.setVisibility(View.GONE);
            mPhone.setText(String.valueOf(p.getPhone()));
        }
        mBirthday.setText(p.getDob());
    }

    void getUserPosts(int init) {

        String profileData = Preferences.getSavedSearchPosts(key, this);

        JSONArray arr = getJsonArray(init, profileData);

        if (Utils.isNetworkAvailable(this)) {

            if ((profileData != null && !profileData.equals("[]")) && init == 1) {
                UserOfflinePosts(arr.toString());
            }
            UserOnlinePosts(init);

        } else {

            if (profileData != null && !profileData.equals("[]")) {
                UserOfflinePosts(arr.toString());
                UserOfflinePosts("[]");

            } else {
                Toast.makeText(this, getString(R.string.ErrorInternet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private JSONArray getJsonArray(int init, String profileData) {
        JSONArray response = null;
        try {
            JSONArray arr = new JSONArray(profileData);
            int limit = init * Utils.POST_LIMIT;
            int start = (init - 1) * Utils.POST_LIMIT;
            if (limit > arr.length()) {
                limit = arr.length();
                isDone = true;
                if (profilePostAdapter != null)
                    profilePostAdapter.Done();
            }
            response = new JSONArray();
            for (int i = start; i < limit; i++) {
                response.put(arr.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    void UserOnlinePosts(final int init) {

        String url = ApiUtil.getSearch(userID, Utils.profileSearch, init);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            isMyReload = true;
                            if (init == 1) {
                                mUserPostFeeds.clear();
                                Preferences.saveSearchPosts(new JSONArray(response), key, getBaseContext());

                            } else {
                                Preferences.addSearchPosts(new JSONArray(response), key, getBaseContext());

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loading = false;

                        UserOfflinePosts(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestManager.getInstance(this).addToRequestQueue(stringRequest);
    }

    void UserOfflinePosts(String trending) {

        try {
            JSONArray arr = new JSONArray(trending);
            for (int i = 0; i < arr.length(); i++) {

                mUserPostFeeds.add(Utils.postHelper(arr, i, true));
            }
            if (arr.length() > 0) {
                updateProfilePostAdapter();
            }

            if (arr.length() < 5) {
                if (mUserPostFeeds.size() > 0) {
                    isDone = true;
                    profilePostAdapter.Done();
                    updateProfilePostAdapter();
                }
            }

        } catch (JSONException | MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void updateProfilePostAdapter() {
        if (recyclerView.getAdapter() == null) {
            Utils.SEARCH_ADDRESS = key;
            profilePostAdapter = new HomeAdapter(mUserPostFeeds, Utils.SEARCH_ADDRESS, this);
            recyclerView.setAdapter(profilePostAdapter);
        } else {
            profilePostAdapter.setPosts(mUserPostFeeds);
            profilePostAdapter.notifyDataSetChanged();
        }
    }

    private void followingStyle() {
        follow.setColorNormalResId(R.color.white);
        follow.setColorPressed(R.color.white);
        follow.setImageResource(R.drawable.ic_follow_black);
        follow.setLabelText("Following");
    }

    void gotoChat() {

        Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
        intent.putExtra(IntentKeys.CHAT, String.valueOf(p.getId()));
        intent.putExtra(IntentKeys.CHAT_NAME, p.getName());
        intent.putExtra(IntentKeys.CHAT_SEEN, "");
        intent.putExtra(IntentKeys.CHAT_IMAGE, p.getProfileSmall().toString());
        startActivity(intent);
    }


    private void followStyle() {

        follow.setColorNormalResId(R.color.loginBackground);
        follow.setColorPressed(R.color.colorPrimaryDark);
        follow.setImageResource(R.drawable.ic_follow);
        follow.setLabelText("Follow");
    }

    private void followUser(final int userID) {

        String url = ApiUtil.getFollow();

        if (follow.getLabelText().equals("Follow")) {
            mFollowers.setText("" + (Integer.parseInt(mFollowers.getText().toString()) + 1));
            followingStyle();
        } else {
            mFollowers.setText("" + (Integer.parseInt(mFollowers.getText().toString()) - 1));

            followStyle();

        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        JSONArray arr1 = new JSONArray();
                        String myfollowing = Preferences.getSavedFollowing(getBaseContext());
                        try {
                            JSONArray arr = new JSONArray(myfollowing);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = new JSONObject(String.valueOf(arr.get(i)));
                                int id = obj.getInt("user_id2");
                                if (id == userID) continue;
                                arr1.put(obj);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            Utils.following.clear();
                            for (int i = 0; i < arr1.length(); i++) {
                                Utils.following.add(Utils.FollowingHelper(arr1, i));
                            }
                            if (response.equals("1")) {
                                Utils.following.add(new Following(userID));
                                arr1.put(new JSONObject().put("user_id2", String.valueOf(userID)));
                            }

                            Preferences.saveFollowing(arr1.toString(), getBaseContext());

                        } catch (JSONException | URISyntaxException | MalformedURLException e) {
                            e.printStackTrace();
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
                params.put("user_id", String.valueOf(userID));
                return params;
            }
        };

        RequestManager.getInstance(this).addToRequestQueue(stringRequest);

    }


    private void BlockUser(final int userID, int menu) {
        mToolbar.getMenu().clear();

        if (menu == 1) {
            mToolbar.inflateMenu(R.menu.profile_menu_unblock);
        } else {
            mToolbar.inflateMenu(R.menu.profile_menu_block);
        }
        Utils.block(userID, this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(mSubTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;


                mToolbar.setBackgroundColor(
                        getResources().getColor(R.color.colorPrimary));


            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(mSubTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;


                mToolbar.setBackgroundColor(Color.TRANSPARENT);

            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void bindActivity() {
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar99);
        mTitle = (TextView) findViewById(R.id.profile_title);
        mSubTitle = (TextView) findViewById(R.id.profile_subtitle);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        mProfileName = (TextView) findViewById(R.id.profile_name);
        mProfileStatus = (TextView) findViewById(R.id.profile_status);
        mProfilePic = (CircleImageView) findViewById(R.id.profilePic);
        mWall = (ImageView) findViewById(R.id.wall);
        mRelationship = (TextView) findViewById(R.id.relationship);
        mFollowers = (TextView) findViewById(R.id.followers);
        mFollowing = (TextView) findViewById(R.id.following);
        mViews = (TextView) findViewById(R.id.profile_views);
        mFaceMatch = (TextView) findViewById(R.id.facematch);
        mLocation = (TextView) findViewById(R.id.location);
        mPhone = (TextView) findViewById(R.id.phoneNo);
        mLock = (ImageView) findViewById(R.id.lock);
        mBirthday = (TextView) findViewById(R.id.birthday);
        nestedScroll = (NestedScrollView) findViewById(R.id.nestedScroll);
    }

    @Override
    public void onListItemClickPost(final int id, final String Address) {

        CharSequence[] newItem;
        Posts ActionPost = null;
        if (Address.equals(key)) {
            ActionPost = mUserPostFeeds.get(id);
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
                    Posts post = mUserPostFeeds.get(id);
                    final String postID = String.valueOf(post.getId());
                    String url = ApiUtil.getPostDelete();
                    postControls(postID, url);

                    if (Address.equals(key)) {
                        mUserPostFeeds.remove(id);
                        isMyReload = true;
                        String res = Preferences.getSavedSearchPosts(key, getBaseContext());
                        try {
                            JSONArray arr = new JSONArray(res);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                arr.remove(id);
                            }
                            Preferences.saveSearchPosts(arr, key, getBaseContext());

                            updateProfilePostAdapter();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } else if (items[item].equals("UnFollow")) {
                    Posts post = mUserPostFeeds.get(id);
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
                mUserPostFeeds.clear();
                UserOfflinePosts(PostLatest);
            } else
                isMyReload = false;
        }


    }

}