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
import in.fabits.fabits.ChatActivity;
import in.fabits.fabits.HomeActivity;
import in.fabits.fabits.PostSingleActivity;
import in.fabits.fabits.ProfileActivity;
import in.fabits.fabits.R;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.IntentKeys;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.model.Notification;
import in.fabits.fabits.model.Online;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NumberViewHolder> {

    private static final String TAG = NotificationAdapter.class.getSimpleName();

    private List<Notification> mNotification = new ArrayList<>();

    private ImageLoader imageLoader = null;


    public NotificationAdapter(List<Notification> Notification) {

        this.mNotification = Notification;
    }

    public void setNotification(List<Notification> Notification) {

        this.mNotification = Notification;
    }


    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.notification_item;
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
        return mNotification.size();
    }


    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView image;
        TextView message;
        TextView time;


        Context helperContext;

        NumberViewHolder(View itemView) {
            super(itemView);
            helperContext = itemView.getContext();
            image = (CircleImageView) itemView.findViewById(R.id.image);
            message = (TextView) itemView.findViewById(R.id.message);
//            time = (TextView) itemView.findViewById(R.id.online_time);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            Notification p = mNotification.get(listIndex);
            imageLoader.displayImage(String.valueOf(p.getImage()), image);
            message.setText(p.getMessage());
        }


        @Override
        public void onClick(View v) {

            Notification p = mNotification.get(getAdapterPosition());
            if (p.getType() == 0 && p.getActivity_type() > 0) {

                Intent intent = new Intent(helperContext, PostSingleActivity.class);
                intent.putExtra(IntentKeys.SEARCH, p.getSource_id());
                intent.putExtra(IntentKeys.TYPE, Utils.PostSearch);
                intent.putExtra(IntentKeys.TITLE, "Notification");
                intent.putExtra(IntentKeys.SUBTITLE, "");
                helperContext.startActivity(intent);
            } else {

                Intent intent = new Intent(helperContext, ProfileActivity.class);
                intent.putExtra(IntentKeys.PROFILE_ID, p.getSource_id());
                helperContext.startActivity(intent);

            }
        }

    }
}
