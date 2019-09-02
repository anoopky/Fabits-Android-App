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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import in.fabits.fabits.api.IntentKeys;
import in.fabits.fabits.ChatActivity;
import in.fabits.fabits.R;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.dialogs.ProfileDialogMaster;
import in.fabits.fabits.model.ChatList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private static final String TAG = ChatListAdapter.class.getSimpleName();
    private List<ChatList> mChatLists = new ArrayList<>();

    public interface ListItemLongClickListener {

        void onListItemClick(int id);
    }

    private ListItemLongClickListener mOnClickListener;

    private ImageLoader imageLoader = null;

    public ChatListAdapter(List<ChatList> ChatLists, ListItemLongClickListener listen) {
        this.mChatLists = ChatLists;
        mOnClickListener = listen;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.message_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions opts = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(opts).build();
        ImageLoader.getInstance().init(config);

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mChatLists.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        CircleImageView image;
        TextView name;
        TextView last;
        TextView time;
        TextView newMessageCount;

        Context helperContext;

        ViewHolder(final View itemView) {
            super(itemView);
            helperContext = itemView.getContext();
            image = (CircleImageView) itemView.findViewById(R.id.chat_image);
            name = (TextView) itemView.findViewById(R.id.name);
            last = (TextView) itemView.findViewById(R.id.last_message);
            time = (TextView) itemView.findViewById(R.id.chat_time);
            newMessageCount = (TextView) itemView.findViewById(R.id.newMessageCount);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProfileDialog();
                }
            });



        }

        void showProfileDialog() {
            ProfileDialogMaster alert = new ProfileDialogMaster(helperContext);
            alert.showDialog(helperContext, mChatLists.get(getAdapterPosition()).getImage().toString(),
                    String.valueOf(mChatLists.get(getAdapterPosition()).getUserID()),
                    String.valueOf(mChatLists.get(getAdapterPosition()).getName())
            );
        }

        void bind(int listIndex) {
            ChatList p = mChatLists.get(listIndex);
            imageLoader.displayImage(String.valueOf(p.getImage()), image);
            name.setText(p.getName());
            if(p.getMessage().contains("[IMAGE]")){
                last.setText("[IMAGE]");
            }else
            last.setText(Html.fromHtml(Utils.EmojiDecoder(p.getMessage()), new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String source) {
                    int resourceId = helperContext.getResources().getIdentifier(source, "drawable",
                            helperContext.getPackageName());
                    Drawable drawable = helperContext.getResources().getDrawable(resourceId);
                    drawable.setBounds(0, 0, 30, 30);
                    return drawable;
                }
            }, null));
            time.setText(p.getTime());
            int count = p.getCount();
            if (count > 0) {
                newMessageCount.setVisibility(View.VISIBLE);
                newMessageCount.setText(String.valueOf(count));
            } else {
                newMessageCount.setVisibility(View.GONE);
            }
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(helperContext, ChatActivity.class);
            int pos = getAdapterPosition();
            ChatList p = mChatLists.get(pos);
            intent.putExtra(IntentKeys.CONVERSATION_ID, String.valueOf(p.getConversation_id()));
            intent.putExtra(IntentKeys.AUTH, String.valueOf(p.getAuth()));
            intent.putExtra(IntentKeys.TYPE, String.valueOf(p.getType()));
            helperContext.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            int pos = getAdapterPosition();
            mOnClickListener.onListItemClick(pos);
            return true;
        }
    }
}
