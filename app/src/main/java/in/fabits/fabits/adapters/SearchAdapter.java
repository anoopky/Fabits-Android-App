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
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.fabits.fabits.HomeActivity;
import in.fabits.fabits.PostSingleActivity;
import in.fabits.fabits.ProfileActivity;
import in.fabits.fabits.R;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.IntentKeys;
import in.fabits.fabits.model.Notification;
import in.fabits.fabits.model.Search;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.NumberViewHolder> {

    private static final String TAG = SearchAdapter.class.getSimpleName();

    private List<Search> mSearches = new ArrayList<>();

    private ImageLoader imageLoader = null;


    public SearchAdapter(List<Search> Searches) {

        this.mSearches = Searches;
    }


    public void  setSearch(List<Search> Searches) {
        this.mSearches = Searches;
    }



    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.search_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions opts = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(opts).build();
        ImageLoader.getInstance().init(config);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mSearches.size();
    }


    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView image;
        TextView name;
        TextView username;
        TextView intro;
//        TextView follow;
//        TextView profileViews;
//        TextView facematch;
//        Button followit;
//        Button message;
        Context helperContext;

        NumberViewHolder(View itemView) {
            super(itemView);
            helperContext = itemView.getContext();
            image = (CircleImageView) itemView.findViewById(R.id.user_picture);
//            message = (Button) itemView.findViewById(R.id.message);
//            followit = (Button) itemView.findViewById(R.id.followit);
            name = (TextView) itemView.findViewById(R.id.name);
            username = (TextView) itemView.findViewById(R.id.username);
            intro = (TextView) itemView.findViewById(R.id.intro);
//            follow = (TextView) itemView.findViewById(R.id.userFollow);
//            profileViews = (TextView) itemView.findViewById(R.id.profileViews);
//            facematch = (TextView) itemView.findViewById(R.id.facematch);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            image.setImageResource(R.drawable.face);

            Search p = mSearches.get(listIndex);
            imageLoader.displayImage(String.valueOf(p.getImage()), image);
            name.setText(p.getName());
            username.setText("@"+p.getUsername());
            intro.setText(p.getIntro());
//            follow.setText(String.valueOf("Followers : "+p.getFollowers()));
//            profileViews.setText("Profile Views : "+String.valueOf(p.getProfileviews()));
//            facematch.setText("FaceMatch : "+p.getFacematch());
        }


        @Override
        public void onClick(View v) {
            Search p = mSearches.get(getAdapterPosition());
            Intent intent = new Intent(helperContext, ProfileActivity.class);
            intent.putExtra(IntentKeys.PROFILE_ID, String.valueOf(p.getId()));
            helperContext.startActivity(intent);

        }

    }
}
