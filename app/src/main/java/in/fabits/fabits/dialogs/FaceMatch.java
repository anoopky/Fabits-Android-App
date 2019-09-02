package in.fabits.fabits.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.fabits.fabits.R;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.RequestManager;

public class FaceMatch {

    private ImageLoader imageLoader;


    public void showDialog(final Activity activity, String img1, String img2, String name1, String name2,
                           final int uid1, final int uid2, final int fid) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.activity_face_match);
        CircleImageView user1 = (CircleImageView) dialog.findViewById(R.id.user1);
        CircleImageView user2 = (CircleImageView) dialog.findViewById(R.id.user2);
        ImageView cross = (ImageView) dialog.findViewById(R.id.cross);
        TextView Fname1 = (TextView) dialog.findViewById(R.id.name1);
        TextView Fname2 = (TextView) dialog.findViewById(R.id.name2);

        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions opts = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(activity)
                .defaultDisplayImageOptions(opts).build();
        ImageLoader.getInstance().init(config);

        imageLoader.displayImage(img1, user1);
        imageLoader.displayImage(img2, user2);
        Fname1.setText(name1);
        Fname2.setText(name2);

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        user1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceSelect(uid1, fid, activity.getBaseContext());
                dialog.dismiss();

            }
        });


        user2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceSelect(uid2, fid, activity.getBaseContext());
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    void faceSelect(final int userID, final int Fid, Context context) {


        String url = ApiUtil.getCheckFaceMatch();
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
                params.put("userID", String.valueOf(userID));
                params.put("fid", String.valueOf(Fid));
                return params;
            }
        };
        RequestManager.getInstance(context).addToRequestQueue(stringRequest);


    }
}
