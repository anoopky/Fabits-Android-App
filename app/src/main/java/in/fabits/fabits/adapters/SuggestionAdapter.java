/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package in.fabits.fabits.adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.fabits.fabits.R;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.RequestManager;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.dialogs.ProfileDialogMaster;
import in.fabits.fabits.model.Following;
import in.fabits.fabits.model.UsersList;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder> {

    private List<UsersList> mPostImages = new ArrayList<>();
    private ImageLoader imageLoader;


    public SuggestionAdapter(List<UsersList> PostImages) {
        this.mPostImages = PostImages;


    }


    public void setUserList(List<UsersList> PostImages) {
        this.mPostImages = PostImages;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.suggestion_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions opts = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(opts).build();
        ImageLoader.getInstance().init(config);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mPostImages.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView name;
        TextView intro;
        Button follow;
        Context helperContext;

        ViewHolder(View itemView) {
            super(itemView);
            helperContext = itemView.getContext();
            image = (CircleImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            intro = (TextView) itemView.findViewById(R.id.intro);
            follow = (Button) itemView.findViewById(R.id.follow);
//            itemView.setOnClickListener(this);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProfileDialog();
                }
            });

        }

        void showProfileDialog() {
            ProfileDialogMaster alert = new ProfileDialogMaster(helperContext);
            alert.showDialog(helperContext, mPostImages.get(getAdapterPosition()).getUserPicture().toString(),
                    String.valueOf(mPostImages.get(getAdapterPosition()).getUserID()),
                    String.valueOf(mPostImages.get(getAdapterPosition()).getName()));
        }


        private void followingStyle() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                follow.setTextColor(helperContext.getResources().getColor(R.color.views, null));
                follow.setBackground(helperContext.getResources().getDrawable(R.drawable.white_button_border, helperContext.getApplicationContext().getTheme()));

            } else {
                follow.setTextColor(helperContext.getResources().getColor(R.color.views));
                follow.setBackgroundDrawable(helperContext.getResources().getDrawable(R.drawable.white_button_border));

            }

            follow.setText("Following");
        }


        private void followStyle() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                follow.setTextColor(helperContext.getResources().getColor(R.color.white, null));
                follow.setBackground(helperContext.getResources().getDrawable(R.drawable.blue_button, helperContext.getApplicationContext().getTheme()));

            } else {
                follow.setTextColor(helperContext.getResources().getColor(R.color.white));
                follow.setBackgroundDrawable(helperContext.getResources().getDrawable(R.drawable.blue_button));

            }

            follow.setText("Follow");
        }

        private void followUser(final int userID) {

            YoYo.with(Techniques.BounceIn)
                    .duration(150)
                    .repeat(0)
                    .playOn(follow);

            String url = ApiUtil.getFollow();

            if (follow.getText().toString().equals("Follow")) {

                followingStyle();
            } else {
                followStyle();

            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            JSONArray arr1 = new JSONArray();
                            String myfollowing = Preferences.getSavedFollowing(helperContext);
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
                                if(response.equals("1")){
                                    Utils.following.add(new Following(userID));
                                    arr1.put(new JSONObject().put("user_id2", String.valueOf(userID)));
                                }

                                Preferences.saveFollowing(arr1.toString(), helperContext);

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

            RequestManager.getInstance(helperContext).addToRequestQueue(stringRequest);

        }



        void bind(int listIndex) {

            final UsersList p = mPostImages.get(listIndex);
            name.setText(p.getName());
            intro.setText(p.getTime());
            image.setImageResource(R.drawable.face);
            imageLoader.displayImage(p.getUserPicture().toString(), image);
            int stat = 0;
            for (int i = 0; i < Utils.following.size(); i++) {
                if (Utils.following.get(i).getUser_id() == p.getUserID())
                    stat = 1;
            }
            if (stat == 1) {
                followingStyle();

            } else {
                followStyle();
            }
            follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    followUser(p.getUserID());
                }
            });


        }
    }


//        @Override
//        public void onClick(View v) {
//
//            int pos = getAdapterPosition();
//            mOnClickListener.onListItemClick(pos);
//
//        }

}

