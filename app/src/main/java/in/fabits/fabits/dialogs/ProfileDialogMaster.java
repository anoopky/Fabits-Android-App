package in.fabits.fabits.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.HashMap;
import java.util.Map;

import in.fabits.fabits.ChatActivity;
import in.fabits.fabits.ProfileActivity;
import in.fabits.fabits.R;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.IntentKeys;
import in.fabits.fabits.api.RequestManager;
import in.fabits.fabits.model.Online;

public class ProfileDialogMaster {

    private ImageLoader imageLoader;

    ImageView profile;

    public ProfileDialogMaster(Context context) {
        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions opts = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(opts).build();
        ImageLoader.getInstance().init(config);


    }

    public void showDialog(final Context context, final String image
            , final String userID , final String name){

        final Dialog dialog = new Dialog(context);
        dialog.getWindow()
                .getAttributes().windowAnimations =  R.style.dialog_slide_animation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_profile_dialog);
        profile = (ImageView) dialog.findViewById(R.id.image);
        ImageView gotoProfile =(ImageView) dialog.findViewById(R.id.gotoProfile);
        ImageView gotochat =(ImageView) dialog.findViewById(R.id.gotoChat);
        imageLoader.displayImage(image, profile);
        dialog.show();
        loadBigImage(context,userID);

        gotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra(IntentKeys.PROFILE_ID, userID);
                context.startActivity(intent);
                dialog.dismiss();

            }
        });

        gotochat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(IntentKeys.CHAT, userID);
                intent.putExtra(IntentKeys.CHAT_NAME, name);
                intent.putExtra(IntentKeys.CHAT_SEEN, "");
                intent.putExtra(IntentKeys.CHAT_IMAGE, image);
                context.startActivity(intent);
                dialog.dismiss();

            }
        });
    }


    void loadBigImage(final Context context, final String userID){

        String url = ApiUtil.getBigImage();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        imageLoader.displayImage(response, profile);

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
        RequestManager.getInstance(context).addToRequestQueue(stringRequest);



    }



}
