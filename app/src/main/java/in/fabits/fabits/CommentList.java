package in.fabits.fabits;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

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
import java.util.Random;

import in.fabits.fabits.adapters.EmojieSectionsPagerAdapter;
import in.fabits.fabits.adapters.PeopleListAdapter;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.EmojiesV1;
import in.fabits.fabits.api.IntentKeys;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.RequestManager;
import in.fabits.fabits.adapters.CommentAdapter;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.model.Comment;
import in.fabits.fabits.model.Posts;
import in.fabits.fabits.placeholder.EmojiesPlaceholderFragment;
import in.fabits.fabits.placeholder.PlaceholderFragment;

public class CommentList extends AppCompatActivity implements CommentAdapter.ListItemClickListener,
        EmojiesPlaceholderFragment.ListItemClickListener {
    private RecyclerView recyclerView;
    List<Comment> mComments = new ArrayList<>();
    EditText commentText;
    CommentAdapter commentAdapter;
    int postId;
    private int visibleThreshold = 8;
    private int PlastVisibleItem, PtotalItemCount;
    private boolean Ploading;
    public int P_INIT = 1;
    int commentCount;
    LinearLayout smilesList;
    ImageView smiles;
    private TabLayout tabLayout;
    private boolean isSmilesOpen = false;
    Button send;
    private String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        smiles = (ImageView) findViewById(R.id.smiles);
        send = (Button) findViewById(R.id.send);
        Toolbar mToolbar;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        postId = getIntent().getIntExtra(IntentKeys.COMMENTS, -1);
        address = getIntent().getStringExtra(IntentKeys.ADDRESS);
        commentCount = getIntent().getIntExtra(IntentKeys.COMMENT_COUNTS, -1);
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
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setSubtitle(commentCount + " Comments");
        commentText = (EditText) findViewById(R.id.commentText);
        recyclerView = (RecyclerView) findViewById(R.id.commentRecycler);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

        String pId = String.valueOf(postId);
        String commentData = Preferences.getSavedCommentList(pId, this);
        try {
            OfflineComments(commentData);
        } catch (JSONException | URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }

        OnlineComments(1);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                PtotalItemCount = layoutManager.getItemCount();
                PlastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!Ploading && PtotalItemCount <= (PlastVisibleItem + visibleThreshold)) {
                    Ploading = true;
                    P_INIT++;
                    if (mComments.size() > 0)
                        OnlineComments(P_INIT);
                }
            }

        });

        EmojiesSetup();
    }

    private void commentIt() throws MalformedURLException, URISyntaxException, JSONException {

        String messageData = Html.toHtml(commentText.getText());
        String data = Utils.EmojiEncoder(messageData);
        URI uri = new URI(ApiUtil.getProfileSmall());
        URL image = uri.toURL();
        int userID = Integer.parseInt(ApiUtil.getUserId());
        Comment comment = new Comment(postId, -1, userID, ApiUtil.getName(),
                ApiUtil.getUserName(), data, "1sec", image);
        mComments.add(comment);
        CommentsApi(data);

        if (recyclerView.getAdapter() == null) {
            commentAdapter = new CommentAdapter(mComments, this);
            recyclerView.setAdapter(commentAdapter);
        } else {
            commentAdapter.setCommentList(mComments);
            commentAdapter.notifyDataSetChanged();
        }
        commentAdapter.Done();
    }

    @Override
    public void onBackPressed() {
        if (isSmilesOpen) {
            hideSmiles();
        } else {
            super.onBackPressed();
        }
    }


    void updateHomePosts(int commentId, String commentData, String commentTime) {
        String posts;
        if (address.equals(Utils.TREND_ADDRESS))
            posts = Preferences.getSavedTrendingPosts(this);
        else if (address.equals(Utils.POST_ADDRESS))
            posts = Preferences.getSavedHomePosts(this);
        else
            posts = Preferences.getSavedSearchPosts(address, this);

        try {
            JSONArray arr1 = new JSONArray(posts);
            JSONArray comment = new JSONArray();
            JSONObject commentObj = new JSONObject();

            commentObj.put("post_id", postId);
            commentObj.put("comment_id", commentId);
            commentObj.put("user_id", ApiUtil.getUserId());
            commentObj.put("user_name", ApiUtil.getName());
            commentObj.put("username", ApiUtil.getUserName());
            commentObj.put("comment", commentData);
            commentObj.put("comment_time", commentTime);
            commentObj.put("user_picture", ApiUtil.getProfileSmall());
            comment.put(commentObj);
            for (int i = 0; i < arr1.length(); i++) {

                JSONObject ob = arr1.getJSONObject(i);
                if (ob.get("post_id").equals(postId)) {
                    ob.put("post_comment", comment);
                    int count = ob.getInt("iscommented");

                    ob.put("iscommented", count + 1);
                    ob.put("comments", ob.getInt("comments") + 1);
                }

            }
            if (address.equals(Utils.TREND_ADDRESS))
                Preferences.saveTrendingPosts(arr1, this);
            else if (address.equals(Utils.POST_ADDRESS))
                Preferences.saveHomePosts(arr1, this);
            else
                Preferences.saveSearchPosts(arr1, address, this);


            getSupportActionBar().setSubtitle(++commentCount + " Comments");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private JSONArray getJSONArray(String id, String messageData) throws JSONException {
        JSONArray arr;
        JSONObject json = new JSONObject();
        json.put("comment_id", id);
        json.put("post_id", postId);
        json.put("user_id", ApiUtil.getUserId());
        json.put("user_name", ApiUtil.getName());
        json.put("username", ApiUtil.getUserName());
        json.put("comment", messageData);
        json.put("user_picture", ApiUtil.getProfileSmall());
        json.put("comment_time", "1sec");
        arr = new JSONArray();
        arr.put(json);
        return arr;
    }

    void OnlineComments(final int init) {

        String url = ApiUtil.getCommentUrl(postId, init);
        final String pId = String.valueOf(postId);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);

                            if (init == 1) {
                                mComments.clear();
                                Preferences.saveCommentList(arr, pId, getBaseContext());
                            } else
                                Preferences.addCommentList(arr, pId, getBaseContext());

                            OfflineComments(response);
                            if (arr.length() > 0)
                                Ploading = false;


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


    void CommentsApi(final String comment) {

        String url = ApiUtil.getCommentPostUrl();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray arr = getJSONArray(obj.getString("id"), comment);
                            Preferences.addCommentList(arr, String.valueOf(postId), getBaseContext());
                            mComments.remove(mComments.size() - 1);
                            OfflineComments(arr.toString());
                            int id = obj.getInt("id");
                            String time = obj.getString("comment_time");
                            updateHomePosts(id, comment, time);
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
                params.put("post_id", String.valueOf(postId));
                params.put("comment", comment);
                return params;
            }
        };
        RequestManager.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void OfflineComments(String response) throws JSONException, MalformedURLException, URISyntaxException {

        JSONArray arr = new JSONArray(response);
        for (int i = 0; i < arr.length(); i++) {
            mComments.add(CommentHelper(arr, i));
        }

        if (arr.length() > 0) {
            if (recyclerView.getAdapter() == null) {
                commentAdapter = new CommentAdapter(mComments, this);
                recyclerView.setAdapter(commentAdapter);
            } else {
                commentAdapter.setCommentList(mComments);
                commentAdapter.notifyDataSetChanged();
            }
        }
        if (arr.length() < 10)
            if (recyclerView.getAdapter() != null) {
                commentAdapter.Done();
            }


    }

    Comment CommentHelper(JSONArray arr, int i) throws JSONException, URISyntaxException, MalformedURLException {

        JSONObject obj = new JSONObject(String.valueOf(arr.get(i)));
        int id = obj.getInt("comment_id");
        int postId = obj.getInt("post_id");
        int userId = obj.getInt("user_id");
        String name = obj.getString("user_name");
        String username = obj.getString("username");
        String comment = obj.getString("comment");
        String img = obj.getString("user_picture");
        URI uri = new URI(img);
        URL image = uri.toURL();
        String time = obj.getString("comment_time");
        return new Comment(postId, id, userId, name, username, comment, time, image);

    }

    private void EmojiesSetup() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        EmojieSectionsPagerAdapter emojiSectionsPagerAdapter = new EmojieSectionsPagerAdapter(getSupportFragmentManager(), this);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(emojiSectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        int pic1 = getResources().getIdentifier(EmojiesV1.people[0], "drawable", getPackageName());
        int pic2 = getResources().getIdentifier(EmojiesV1.nature[0], "drawable", getPackageName());
        int pic3 = getResources().getIdentifier(EmojiesV1.food[0], "drawable", getPackageName());
        int pic4 = getResources().getIdentifier(EmojiesV1.activity[0], "drawable", getPackageName());
        int pic5 = getResources().getIdentifier(EmojiesV1.travel[0], "drawable", getPackageName());
        int pic6 = getResources().getIdentifier(EmojiesV1.objects[0], "drawable", getPackageName());
        int pic7 = getResources().getIdentifier(EmojiesV1.symbols[0], "drawable", getPackageName());
        int pic8 = getResources().getIdentifier(EmojiesV1.flags[0], "drawable", getPackageName());
        int pic9 = getResources().getIdentifier(EmojiesV1.diversity[0], "drawable", getPackageName());
        tabLayout.getTabAt(0).setIcon(pic1);
        tabLayout.getTabAt(1).setIcon(pic2);
        tabLayout.getTabAt(2).setIcon(pic3);
        tabLayout.getTabAt(3).setIcon(pic4);
        tabLayout.getTabAt(4).setIcon(pic5);
        tabLayout.getTabAt(5).setIcon(pic6);
        tabLayout.getTabAt(6).setIcon(pic7);
        tabLayout.getTabAt(7).setIcon(pic8);
        tabLayout.getTabAt(8).setIcon(pic9);
        smiles = (ImageView) findViewById(R.id.smiles);
        smilesList = (LinearLayout) findViewById(R.id.smilesList);

        smiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSmilesOpen)
                    showSmiles();
                else {
                    hideSmiles();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow(commentText.getApplicationWindowToken(),
                            InputMethodManager.SHOW_IMPLICIT, 0);
                    commentText.requestFocus();
                }

            }
        });
        commentText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                hideSmiles();
                return false;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if(Utils.isNetworkAvailable(getBaseContext())) {
                        String messageData = Html.toHtml(commentText.getText());
                        messageData = messageData.trim();
                        if (messageData.length() > 0) {
                            YoYo.with(Techniques.BounceIn)
                                    .duration(150)
                                    .repeat(0)
                                    .playOn(send);
                            commentIt();
                            commentText.setText("");
                        }
                    }
                    else{
                        Toast.makeText(getBaseContext(),"No Internet", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException | URISyntaxException | MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onListItemClick(String id) {
        String htmlText = Html.toHtml(commentText.getText());

        htmlText = Utils.EmojiEncoder(htmlText);
        htmlText = Utils.EmojiDecoder(htmlText);
        htmlText += "<img src=\"" + id + "\">";
        htmlText = htmlText.trim();
        commentText.setText("");
        commentText.append(Html.fromHtml(htmlText, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                int resourceId = getResources().getIdentifier(source, "drawable",
                        getPackageName());
                Drawable drawable = getResources().getDrawable(resourceId);
                drawable.setBounds(0, 0, 45, 45);
                return drawable;
            }
        }, null));
        commentText.append(" ");
    }


    private void showSmiles() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            smiles.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard, getApplicationContext().getTheme()));

        } else {
            smiles.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard));
        }


        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param1.weight = 1.0f;
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                60,
                r.getDisplayMetrics()
        );

        param1.setMargins(0, 0, 0, px);
        smilesList.setLayoutParams(params);
        recyclerView.setLayoutParams(param1);
        isSmilesOpen = true;
    }

    private void hideSmiles() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            smiles.setImageDrawable(getResources().getDrawable(R.drawable.ic_mood, getApplicationContext().getTheme()));

        } else {
            smiles.setImageDrawable(getResources().getDrawable(R.drawable.ic_mood));
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 0.0f;
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                60,
                r.getDisplayMetrics()
        );

        params.setMargins(0, 0, 0, px);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param1.weight = 2.0f;
        smilesList.setLayoutParams(param1);
        recyclerView.setLayoutParams(params);

        isSmilesOpen = false;
    }

    @Override
    public void onListItemClick(final int id) {

        CharSequence[] newItem;
        if (ApiUtil.getUserId().equals(String.valueOf(mComments.get(id).getUser_id()))) {
            newItem = new CharSequence[]{"Copy", "Delete"};
        } else {
            newItem = new CharSequence[]{"Copy"};
        }
        final CharSequence[] items = newItem;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //        builder.setTitle("Select The Action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (item == 0) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", mComments.get(id).getComment());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getBaseContext(), "Text Copied.", Toast.LENGTH_SHORT).show();
                }
                if (item == 1) {
                    Comment comment = mComments.get(id);
                    final String comment_ID = String.valueOf(comment.getComment_id());
                    String url = ApiUtil.getCommentDelete();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {


//                                    void updateHomePosts(int commentId, String commentData,String commentTime) {
                                    String posts;
                                    if (address.equals(Utils.TREND_ADDRESS))
                                        posts = Preferences.getSavedTrendingPosts(getBaseContext());
                                    else if (address.equals(Utils.POST_ADDRESS))
                                        posts = Preferences.getSavedHomePosts(getBaseContext());
                                    else
                                        posts = Preferences.getSavedSearchPosts(address, getBaseContext());

                                    JSONArray arr1 = null;
                                    try {
                                        arr1 = new JSONArray(posts);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    if (id >= 1) {

                                        if (id == mComments.size()) {
                                            Comment comment1 = mComments.get(id - 1);
                                            try {
                                                JSONArray comment = new JSONArray();
                                                JSONObject commentObj = new JSONObject();

                                                commentObj.put("post_id", postId);
                                                commentObj.put("comment_id", comment1.getComment_id());
                                                commentObj.put("user_id", comment1.getUser_id());
                                                commentObj.put("user_name", comment1.getUser_name());
                                                commentObj.put("username", comment1.getUsername());
                                                commentObj.put("comment", comment1.getComment());
                                                commentObj.put("comment_time", comment1.getComment_time());
                                                commentObj.put("user_picture", comment1.getUser_picture());
                                                comment.put(commentObj);
                                                for (int i = 0; i < (arr1 != null ? arr1.length() : 0); i++) {

                                                    JSONObject ob = arr1.getJSONObject(i);
                                                    if (ob.get("post_id").equals(postId)) {
                                                        ob.put("post_comment", comment);
                                                        ob.put("iscommented", ob.getInt("iscommented") - 1);
                                                        ob.put("comments", ob.getInt("comments") - 1);
                                                    }

                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {

                                            try {
                                                for (int i = 0; i < (arr1 != null ? arr1.length() : 0); i++) {
                                                    JSONObject ob = arr1.getJSONObject(i);
                                                    if (ob.get("post_id").equals(postId)) {
                                                        int count = ob.getInt("iscommented");
                                                        ob.put("iscommented", ob.getInt("iscommented") - 1);
                                                        ob.put("comments", ob.getInt("comments") - 1);
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {

                                        try {
                                            for (int i = 0; i < (arr1 != null ? arr1.length() : 0); i++) {

                                                JSONObject ob = arr1.getJSONObject(i);
                                                if (ob.get("post_id").equals(postId)) {
                                                    ob.put("post_comment", new JSONArray());
                                                    ob.put("iscommented", ob.getInt("iscommented") - 1);
                                                    ob.put("comments", ob.getInt("comments") - 1);
                                                }

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    if (address.equals(Utils.TREND_ADDRESS))
                                        Preferences.saveTrendingPosts(arr1, getBaseContext());
                                    else if (address.equals(Utils.POST_ADDRESS))
                                        Preferences.saveHomePosts(arr1, getBaseContext());
                                    else
                                        Preferences.saveSearchPosts(arr1, address, getBaseContext());
                                    getSupportActionBar().setSubtitle(--commentCount + " Comments");

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                         }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("post_id", String.valueOf(postId));
                            params.put("id", comment_ID);
                            return params;
                        }
                    };
                    RequestManager.getInstance(getBaseContext()).addToRequestQueue(stringRequest);


                    mComments.remove(id);
                    commentAdapter.setCommentList(mComments);
                    commentAdapter.notifyDataSetChanged();

                    String res = Preferences.getSavedCommentList(String.valueOf(postId), getBaseContext());
                    try {
                        JSONArray arr = new JSONArray(res);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            arr.remove(id);
                        }

                        Preferences.saveCommentList(arr, String.valueOf(postId), getBaseContext());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        builder.show();

    }
}

