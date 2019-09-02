package in.fabits.fabits;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.fabits.fabits.adapters.RandomPagerAdapter;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.IntentKeys;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.RequestManager;

public class goRandomActivity extends AppCompatActivity {

    private ViewPager pager = null;
    private RandomPagerAdapter pagerAdapter = null;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_random);
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
        getSupportActionBar().setTitle("Random Profiles");
        pagerAdapter = new RandomPagerAdapter();
        pager = (ViewPager) findViewById (R.id.view_pager);
        pager.setAdapter (pagerAdapter);

        pager.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev & next page
        pager.setPadding(0, 0, 20, 0);
        // sets a margin b/w individual pages to ensure that there is a gap b/w them
//        pager.setPageMargin(20);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                 if(pagerAdapter.getCount()-2 == position){
                    init++;
                    randomList();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions opts = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(opts).build();
        ImageLoader.getInstance().init(config);

                randomList();
    }

int init = 1;
    void randomList() {
        String url = ApiUtil.getRandomProfiles(init);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setRandomData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestManager.getInstance(getBaseContext()).addToRequestQueue(stringRequest);
    }

    private void setRandomData(String response) {
        try {
            final JSONArray arr = new JSONArray(response);
            LayoutInflater inflater = getLayoutInflater();

            for (int i =0;i<arr.length();i++){
                RelativeLayout v0 = (RelativeLayout) inflater.inflate (R.layout.random_item, null);
                ImageView im = (ImageView) v0.findViewById(R.id.image);
                TextView name = (TextView) v0.findViewById(R.id.name);
                TextView intro = (TextView) v0.findViewById(R.id.intro);
                Button btn = (Button) v0.findViewById(R.id.visitProfile);
                imageLoader.displayImage(String.valueOf(((JSONObject)arr.get(i)).get("profile_picture_big")), im);
                name.setText(((JSONObject)arr.get(i)).getString("name"));
                intro.setText(((JSONObject)arr.get(i)).getString("intro"));

                final int finalI = i;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(goRandomActivity.this, ProfileActivity.class);
                        try {
                            intent.putExtra(IntentKeys.PROFILE_ID, ((JSONObject)arr.get(finalI)).getString("id"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }
                });

                pagerAdapter.addView (v0);
            }
            pagerAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void addView (View newPage)
    {
        int pageIndex = pagerAdapter.addView (newPage);
        // You might want to make "newPage" the currently displayed page:
        pager.setCurrentItem (pageIndex, true);
    }

    public void removeView (View defunctPage)
    {
        int pageIndex = pagerAdapter.removeView (pager, defunctPage);
        // You might want to choose what page to display, if the current page was "defunctPage".
        if (pageIndex == pagerAdapter.getCount())
            pageIndex--;
        pager.setCurrentItem (pageIndex);
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to get the currently displayed page.
    public View getCurrentPage ()
    {
        return pagerAdapter.getView (pager.getCurrentItem());
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to set the currently displayed page.  "pageToShow" must
    // currently be in the adapter, or this will crash.
    public void setCurrentPage (View pageToShow)
    {
        pager.setCurrentItem (pagerAdapter.getItemPosition (pageToShow), true);
    }
}
