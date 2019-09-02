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
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.IntentKeys;
import in.fabits.fabits.ChatActivity;
import in.fabits.fabits.R;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.dialogs.ProfileDialogMaster;
import in.fabits.fabits.model.Online;

public class OnlineListAdapter extends RecyclerView.Adapter<OnlineListAdapter.NumberViewHolder> {

    private static final String TAG = OnlineListAdapter.class.getSimpleName();
    private List<Online> allPosts = new ArrayList<>();
    private int mNumberItems;
    private ImageLoader imageLoader = null;
//    private Typeface typeFace;
//    private Typeface typeFace1;

    public OnlineListAdapter(List<Online> allPosts, int numberOfItems) {
        mNumberItems = numberOfItems;
        this.allPosts = allPosts;
    }


    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.users_online;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions opts = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(opts).build();
        ImageLoader.getInstance().init(config);

//        typeFace=Typeface.createFromAsset(context.getAssets(),"fonts/Ubuntu-Medium.ttf");
//        typeFace1=Typeface.createFromAsset(context.getAssets(),"fonts/Ubuntu-Regular.ttf");

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }


    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView image;
        TextView name;
        TextView last;
        TextView time;
        TextView onlineStatus;

        Context helperContext;

        NumberViewHolder(View itemView) {
            super(itemView);
            helperContext = itemView.getContext();
            image = (CircleImageView) itemView.findViewById(R.id.chat_image);
            name = (TextView) itemView.findViewById(R.id.name);
            last = (TextView) itemView.findViewById(R.id.intro);
            time = (TextView) itemView.findViewById(R.id.online_time);
            onlineStatus = (TextView) itemView.findViewById(R.id.isOnline);
            itemView.setOnClickListener(this);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProfileDialog();
                }
            });



        }

        void showProfileDialog() {
            ProfileDialogMaster alert = new ProfileDialogMaster(helperContext);
            alert.showDialog(helperContext, allPosts.get(getAdapterPosition()).getUser_picture().toString(),
                    String.valueOf(allPosts.get(getAdapterPosition()).getId()),
                    String.valueOf(allPosts.get(getAdapterPosition()).getUser_name())
            );
        }

        void bind(int listIndex) {

//            name.setTypeface(typeFace);
//            last.setTypeface(typeFace);
//            time.setTypeface(typeFace1);

            Online p = allPosts.get(listIndex);
            image.setImageResource(R.drawable.face);
            imageLoader.displayImage(String.valueOf(p.getUser_picture()), image);
            name.setText(p.getUser_name());
            last.setText(p.getIntro());
            String lastSeen = p.getLastseen();

            if(Utils.isNetworkAvailable(helperContext)) {
                if (lastSeen.equals("online")) {
                    onlineStatus.setVisibility(View.VISIBLE);
                    time.setVisibility(View.GONE);
                } else {
                    time.setVisibility(View.VISIBLE);
                    time.setText(lastSeen);
                    onlineStatus.setVisibility(View.GONE);

                }
            }
            else{
                time.setVisibility(View.GONE);
                onlineStatus.setVisibility(View.GONE);
            }
        }


        @Override
        public void onClick(View v) {

            Intent intent = new Intent(helperContext, ChatActivity.class);
            int pos = getAdapterPosition();
            Online p = allPosts.get(pos);
            intent.putExtra(IntentKeys.CHAT, String.valueOf(p.getId()));
            intent.putExtra(IntentKeys.CHAT_NAME, p.getUser_name());
            intent.putExtra(IntentKeys.CHAT_SEEN, p.getLastseen());
            intent.putExtra(IntentKeys.CHAT_IMAGE, p.getUser_picture().toString());
            helperContext.startActivity(intent);

        }

    }
}
