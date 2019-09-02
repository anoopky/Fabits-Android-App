package in.fabits.fabits;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import in.fabits.fabits.adapters.EmojieSectionsPagerAdapter;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.EmojiesV1;
import in.fabits.fabits.api.RequestManager;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.placeholder.EmojiesPlaceholderFragment;
import in.fabits.fabits.placeholder.PlaceholderFragment;

public class PostTextActivity extends AppCompatActivity implements
        EmojiesPlaceholderFragment.ListItemClickListener {

    LinearLayout smilesList;
    LinearLayout text;
    ImageView smiles;
    private TabLayout tabLayout;
    private boolean isSmilesOpen = false;
    EditText textPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_text);
        smilesList = (LinearLayout) findViewById(R.id.smilesList);
        text = (LinearLayout) findViewById(R.id.text);
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
        getSupportActionBar().setTitle("Post");
        textPost = (EditText) findViewById(R.id.postTextData);
        EmojiesSetup();
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
                    inputMethodManager.toggleSoftInputFromWindow(textPost.getApplicationWindowToken(),
                            InputMethodManager.SHOW_IMPLICIT, 0);
                    textPost.requestFocus();
                }

            }
        });
        textPost.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                hideSmiles();
                return false;
            }
        });

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
        text.setLayoutParams(param1);
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
        text.setLayoutParams(params);

        isSmilesOpen = false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_post, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.postText:


                String MessageData = Html.toHtml(textPost.getText());
                MessageData = MessageData.trim();
                if (MessageData.length() > 0) {
                    MessageData = Utils.EmojiEncoder(MessageData);
                    final String messageData = MessageData;
                    String url = ApiUtil.getPostTextUrl();
                    if (Utils.isNetworkAvailable(getBaseContext())) {
                        item.setEnabled(false);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        PlaceholderFragment.P_INIT = -1;
                                        onBackPressed();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                item.setEnabled(false);

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("text", messageData);
                                return params;
                            }
                        };
                        RequestManager.getInstance(this).addToRequestQueue(stringRequest);
                    } else {
                        Toast.makeText(getBaseContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    }
                }


                break;

            default:
                break;
        }

        return true;
    }


    @Override
    public void onListItemClick(String id) {

        String htmlText = Html.toHtml(textPost.getText());

        htmlText = Utils.EmojiEncoder(htmlText);
        htmlText = Utils.EmojiDecoder(htmlText);
        htmlText += "<img src=\"" + id + "\">";
        htmlText = htmlText.trim();
        textPost.setText("");
        textPost.append(Html.fromHtml(htmlText, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                int resourceId = getResources().getIdentifier(source, "drawable",
                        getPackageName());
                Drawable drawable = getResources().getDrawable(resourceId);
                drawable.setBounds(0, 0, 45, 45);
                return drawable;
            }
        }, null));
        textPost.append(" ");
    }
}
