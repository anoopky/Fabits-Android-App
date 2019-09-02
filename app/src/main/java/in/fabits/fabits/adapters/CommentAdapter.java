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
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.fabits.fabits.R;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.dialogs.ProfileDialogMaster;
import in.fabits.fabits.model.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private static final String TAG = CommentAdapter.class.getSimpleName();
    private List<Comment> mComments = new ArrayList<>();

    private ImageLoader imageLoader = null;

    public interface ListItemClickListener {

        void onListItemClick(int id);
    }

    private ListItemClickListener mOnClickListener;

    public CommentAdapter(List<Comment> comments, ListItemClickListener OnClickListener) {
        this.mComments = comments;
        this.mOnClickListener = OnClickListener;
    }

    public void setComments(List<Comment> comment) {
        this.mComments = comment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.comment_item;
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
        return mComments.size() + 1;
    }

    public void setCommentList(List<Comment> userList) {
        mComments = userList;
    }

    boolean isDone = false;

    public void Done() {
        isDone = true;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView image;
        TextView name;
        ImageView control_1;
        TextView last;
        TextView user_comment_name;
        Context mContext;
        LinearLayout info;
        LinearLayout control;
        LinearLayout root;
        ProgressBar loading;


        ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            image = (CircleImageView) itemView.findViewById(R.id.commentImage);
            name = (TextView) itemView.findViewById(R.id.comment);
            user_comment_name = (TextView) itemView.findViewById(R.id.user_comment_name);
            last = (TextView) itemView.findViewById(R.id.commentTime);
            info = (LinearLayout) itemView.findViewById(R.id.info);
            root = (LinearLayout) itemView.findViewById(R.id.root);
            control = (LinearLayout) itemView.findViewById(R.id.control);
            loading = (ProgressBar) itemView.findViewById(R.id.loading);
            control_1 = (ImageView) itemView.findViewById(R.id.control_1);
//            itemView.setOnClickListener(this);

            control.setOnClickListener(this);



            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProfileDialog();
                }
            });



        }

        void showProfileDialog() {
            ProfileDialogMaster alert = new ProfileDialogMaster(mContext);
            alert.showDialog(mContext, mComments.get(getAdapterPosition()).getUser_picture().toString(),
                    String.valueOf(mComments.get(getAdapterPosition()).getUser_id()),
                    String.valueOf(mComments.get(getAdapterPosition()).getUser_name())
                    );
        }
        void bind(int listIndex) {

            root.setVisibility(View.VISIBLE);

            if (listIndex == mComments.size()) {

                image.setVisibility(View.GONE);
                info.setVisibility(View.GONE);
                control.setVisibility(View.GONE);
                if (!isDone) {
                    loading.setVisibility(View.VISIBLE);
                }
                else {
                    root.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                }

            } else {
                loading.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
                info.setVisibility(View.VISIBLE);
                control.setVisibility(View.VISIBLE);
                Comment p = mComments.get(listIndex);
                if(p.getComment_id() == -1){
                    control_1.setVisibility(View.GONE);
                }
                else{
                    control_1.setVisibility(View.VISIBLE);
                }
                user_comment_name.setText(p.getUser_name());
                imageLoader.displayImage(String.valueOf(p.getUser_picture()), image);
                last.setText(p.getComment_time());
                name.setText(Html.fromHtml(Utils.EmojiDecoder(p.getComment()), new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {
                        int resourceId = mContext.getResources().getIdentifier(source, "drawable",
                                mContext.getPackageName());
                        Drawable drawable = mContext.getResources().getDrawable(resourceId);
                        drawable.setBounds(0, 0, 45, 45);
                        return drawable;
                    }
                }, null));
            }
        }


        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            mOnClickListener.onListItemClick(pos);
        }

    }
}
