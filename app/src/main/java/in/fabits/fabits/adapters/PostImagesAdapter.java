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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.fabits.fabits.ChatActivity;
import in.fabits.fabits.R;
import in.fabits.fabits.api.IntentKeys;
import in.fabits.fabits.model.ChatList;

public class PostImagesAdapter extends RecyclerView.Adapter<PostImagesAdapter.ViewHolder> {

    private static final String TAG = PostImagesAdapter.class.getSimpleName();
    private List<String> mPostImages = new ArrayList<>();
    private ImageLoader imageLoader;

    private  ListItemClickListener mOnClickListener;


    public PostImagesAdapter(List<String> PostImages, ListItemClickListener listen) {
        this.mPostImages = PostImages;
        mOnClickListener = listen;


    }

    public interface ListItemClickListener{

        void onListItemClick(int id);
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.post_image_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        imageLoader = ImageLoader.getInstance();

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


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;


        Context helperContext;

        ViewHolder(View itemView) {
            super(itemView);
            helperContext = itemView.getContext();
            image = (ImageView) itemView.findViewById(R.id.Images);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {

            String p = mPostImages.get(listIndex);
            File imgFile = new File(p);
            image.setImageResource(android.R.color.transparent);

            if (imgFile.exists()) {
                String c = imgFile.getPath();
                imageLoader.displayImage("file:///"+c, image);

            }
        }


        @Override
        public void onClick(View v) {

            int pos = getAdapterPosition();
            mOnClickListener.onListItemClick(pos);

        }

    }
}
