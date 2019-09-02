package in.fabits.fabits.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Html;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.fabits.fabits.ImageZoomActivity;
import in.fabits.fabits.dialogs.ProfileDialogMaster;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.IntentKeys;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.RequestManager;
import in.fabits.fabits.PeopleList;
import in.fabits.fabits.R;
import in.fabits.fabits.CommentList;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.model.Like;
import in.fabits.fabits.model.Posts;
import in.fabits.fabits.model.UsersList;

import static in.fabits.fabits.placeholder.PlaceholderFragment.P_INIT;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.PostViewHolder> {

    private static final String TAG = ChatListAdapter.class.getSimpleName();
    private List<Posts> allPosts = new ArrayList<>();
    private ImageLoader imageLoader;

    public interface ListItemClickListener {

        void onListItemClickPost(int id, String Address);
    }

    private ListItemClickListener mOnClickListener;
    String mAddress;

    public HomeAdapter(List<Posts> allPosts, String Address, ListItemClickListener OnClickListener) {
        this.allPosts = allPosts;
        this.mOnClickListener = OnClickListener;
        this.mAddress = Address;

    }

    public void setPosts(List<Posts> allPosts) {
        this.allPosts = allPosts;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.post_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        PostViewHolder viewHolder = new PostViewHolder(view);
        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions opts = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(opts).build();
        ImageLoader.getInstance().init(config);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return allPosts.size() + 1;
    }

    boolean isDone = false;

    public void Done() {
        isDone = true;
    }


    class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int visibleThreshold = 3;
        private int PlastVisibleItem, PtotalItemCount;
        private boolean Ploading;
        ImageView mLike;
        LinearLayout control;
        ImageView mDislike;
        ImageView mComment;
        ImageView LikeDT;
        TextView user_name;
        TextView post_time;
        TextView post_text;
        TextView likeCount;
        TextView dislikeCount;
        TextView commentCount;
        TextView user_comment_name;
        TextView comment;
        TextView comment_time;
        TextView post_data;

        CircleImageView comment_pic;
        LinearLayout commentBar;
        LinearLayout allPostContainer;
        LinearLayout likeList;
        CircleImageView user_image;
        ImageView post_image;
        ProgressBar loadmore;
        FrameLayout imageFrame;
        CircleImageView like0;
        ImageView play;
        CircleImageView like1;
        CircleImageView like2;
        CircleImageView like3;
        Button allLike;
        View helpview;
        RecyclerView recyclerView;

        Context mContext;
        private List<UsersList> mUsersList = new ArrayList<>();

        PostViewHolder(final View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            helpview = itemView;
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            user_comment_name = (TextView) itemView.findViewById(R.id.user_comment_name);
            post_time = (TextView) itemView.findViewById(R.id.post_time);
            post_text = (TextView) itemView.findViewById(R.id.post_text);
            likeCount = (TextView) itemView.findViewById(R.id.likeCount);
            dislikeCount = (TextView) itemView.findViewById(R.id.dislikeCount);
            commentCount = (TextView) itemView.findViewById(R.id.commentCount);
            post_data = (TextView) itemView.findViewById(R.id.post_data);
            comment = (TextView) itemView.findViewById(R.id.user_comment);
            comment_time = (TextView) itemView.findViewById(R.id.user_comment_time);
            control = (LinearLayout) itemView.findViewById(R.id.control);
            LikeDT = (ImageView) itemView.findViewById(R.id.LikeDT);
            comment_pic = (CircleImageView) itemView.findViewById(R.id.user_comment_pic);
            commentBar = (LinearLayout) itemView.findViewById(R.id.comment_bar);
            allPostContainer = (LinearLayout) itemView.findViewById(R.id.allPostContainer);
            likeList = (LinearLayout) itemView.findViewById(R.id.likeList);
            loadmore = (ProgressBar) itemView.findViewById(R.id.loadmore);
            user_image = (CircleImageView) itemView.findViewById(R.id.user_picture);
            imageFrame = (FrameLayout) itemView.findViewById(R.id.imageFrame);
            like0 = (CircleImageView) itemView.findViewById(R.id.likePerson0);
            play = (ImageView) itemView.findViewById(R.id.play);
            like1 = (CircleImageView) itemView.findViewById(R.id.likePerson1);
            like2 = (CircleImageView) itemView.findViewById(R.id.likePerson2);
            like3 = (CircleImageView) itemView.findViewById(R.id.likePerson3);
            post_image = (ImageView) itemView.findViewById(R.id.post_image);
            allLike = (Button) itemView.findViewById(R.id.allLikes);
            mLike = (ImageView) itemView.findViewById(R.id.like);
            mLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    like();
                }

            });
            mDislike = (ImageView) itemView.findViewById(R.id.dislike);
            mDislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dislike();
                }

            });
            mComment = (ImageView) itemView.findViewById(R.id.comment);
            mComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comment();
                }

            });
            recyclerView = (RecyclerView) itemView.findViewById(R.id.suggestion);

            allLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allLikes();
                }


            });

            user_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProfileDialog();
                }
            });

            control.setOnClickListener(this);

        }

        void showProfileDialog() {
            ProfileDialogMaster alert = new ProfileDialogMaster(helpview.getContext());
            alert.showDialog(helpview.getContext(), allPosts.get(getAdapterPosition()).getUser_picture().toString(),
                    String.valueOf(allPosts.get(getAdapterPosition()).getUser_id()),
                    String.valueOf(allPosts.get(getAdapterPosition()).getUser_name()));
        }

        void reset() {

            user_image.setImageResource(R.drawable.face);
            post_image.setImageResource(R.drawable.face);
            allPostContainer.setVisibility(View.VISIBLE);
            loadmore.setVisibility(View.GONE);
            imageFrame.setVisibility(View.GONE);
            resetLike();
            play.setVisibility(View.INVISIBLE);
            post_image.setOnClickListener(null);
            post_image.setOnTouchListener(null);
            post_image.setVisibility(View.GONE);
            like0.setVisibility(View.GONE);
            like1.setVisibility(View.GONE);
            like2.setVisibility(View.GONE);
            like3.setVisibility(View.GONE);
            likeList.setVisibility(View.GONE);
            post_text.setVisibility(View.VISIBLE);
            like0.setImageResource(R.drawable.face);
            like1.setImageResource(R.drawable.face);
            like2.setImageResource(R.drawable.face);
            like3.setImageResource(R.drawable.face);
            commentBar.setVisibility(View.GONE);
            comment_pic.setImageResource(R.drawable.face);
            user_name.setText("");
            post_time.setText("");
            post_text.setText("");
            likeCount.setText("");
            post_data.setText("");
            dislikeCount.setText("");
            commentCount.setText("");
            comment.setText("");
            comment_time.setText("");
        }

        private void OfflineUserList(String response) throws JSONException, MalformedURLException, URISyntaxException {
            mUsersList.clear();
            JSONArray arr = new JSONArray(response);
            for (int i = 0; i < arr.length(); i++) {
                mUsersList.add(Utils.UserListHelper(arr, i));
            }

            if (arr.length() > 0) {
                if (recyclerView.getAdapter() == null) {
                    mSuggestionAdapter = new SuggestionAdapter(mUsersList);
                    recyclerView.setAdapter(mSuggestionAdapter);
                }
            } else {
                mSuggestionAdapter.setUserList(mUsersList);
                mSuggestionAdapter.notifyDataSetChanged();
            }
        }

        SuggestionAdapter mSuggestionAdapter;
        SnapHelper helper;

        void bind(int listIndex) {

            if (listIndex == allPosts.size()) {
                allPostContainer.setVisibility(View.GONE);
                if (!isDone) {
                    loadmore.setVisibility(View.VISIBLE);
                }


            } else {

                recyclerView.setVisibility(View.GONE);
                if (listIndex == 3 && mAddress.equals(Utils.POST_ADDRESS)) {
                    recyclerView.setVisibility(View.VISIBLE);
                    if (helper == null) {
                        helper = new LinearSnapHelper();
                        helper.attachToRecyclerView(recyclerView);
                    }
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(helpview.getContext(), LinearLayoutManager.HORIZONTAL, true);
                    layoutManager.setReverseLayout(false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    String response = Preferences.getSavedSuggestions(mContext);
                    try {
                        if (response != null && !response.equals("[]"))
                            OfflineUserList(response);

                    } catch (JSONException | MalformedURLException | URISyntaxException e) {
                        e.printStackTrace();
                    }

                }

                reset();
                final Posts p = allPosts.get(listIndex);
                imageLoader.displayImage(String.valueOf(p.getUser_picture()), user_image);


                if (p.getPost_type() == 4) {
                    play.setVisibility(View.VISIBLE);

                    post_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String youtube_image = p.getPost_image();
                            String youtube = "http://img.youtube.com/vi/";
                            int x = youtube_image.indexOf(youtube);
                            youtube_image = youtube_image.substring(x + youtube.length(), youtube_image.length());
                            int x1 = youtube_image.indexOf("/");
                            youtube_image = youtube_image.substring(0, x1);

                            final String finalYoutube_image = youtube_image;

                            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + finalYoutube_image));
                            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + finalYoutube_image));
                            try {
                                itemView.getContext().startActivity(appIntent);
                            } catch (ActivityNotFoundException ex) {
                                itemView.getContext().startActivity(webIntent);
                            }

                        }
                    });

                } else {
                    play.setVisibility(View.GONE);
                    post_image.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            return gd.onTouchEvent(event);
                        }
                    });
                }

                if (p.getPost_image() != null) {
                    imageLoader.displayImage(String.valueOf(p.getPost_image()), post_image);
                    imageFrame.setVisibility(View.VISIBLE);
                    post_image.setVisibility(View.VISIBLE);
                }

                if (p.getPost_text().length() == 0)
                    post_text.setVisibility(View.GONE);

                post_data.setText(p.getData());
                like0.setVisibility(View.GONE);
                like1.setVisibility(View.GONE);
                like2.setVisibility(View.GONE);
                like3.setVisibility(View.GONE);

                for (int i = 0; i < p.getLike().size(); i++) {
                    CircleImageView img;
                    if (i == 0) {
                        img = like1;
                    } else if (i == 1) {
                        img = like2;
                    } else {
                        img = like3;
                    }
                    img.setVisibility(View.VISIBLE);
                    imageLoader.displayImage(String.valueOf(p.getLike().get(i).getUser_picture()), img);
                    likeList.setVisibility(View.VISIBLE);

                }

                for (int i = 0; i < p.getComment().size(); i++) {
                    commentBar.setVisibility(View.VISIBLE);

                    commentBar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           comment();
                        }
                    });

                    imageLoader.displayImage(String.valueOf(p.getComment().get(i).getUser_picture()), comment_pic);
                    user_comment_name.setText(p.getComment().get(i).getUser_name());
                    comment.setText(Html.fromHtml(Utils.EmojiDecoder(p.getComment().get(i).getComment()), new Html.ImageGetter() {
                        @Override
                        public Drawable getDrawable(String source) {
                            int resourceId = mContext.getResources().getIdentifier(source, "drawable",
                                    mContext.getPackageName());
                            Drawable drawable = mContext.getResources().getDrawable(resourceId);
                            drawable.setBounds(0, 0, 45, 45);
                            return drawable;
                        }
                    }, null));

                    comment_time.setText(p.getComment().get(i).getComment_time());
                }

                user_name.setText(p.getUser_name());
                post_time.setText(p.getPost_time());

                post_text.setText(Html.fromHtml(Utils.EmojiDecoder(p.getPost_text()), new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {
                        int resourceId = mContext.getResources().getIdentifier(source, "drawable",
                                mContext.getPackageName());
                        Drawable drawable = mContext.getResources().getDrawable(resourceId);
                        drawable.setBounds(0, 0, 45, 45);
                        return drawable;
                    }
                }, null));

                likeCount.setText(String.valueOf(p.getLikes()));
                dislikeCount.setText(String.valueOf(p.getDislikes()));
                commentCount.setText(String.valueOf(p.getComments()));

                if (p.getIsliked() == 1) {
                    mLike.setColorFilter(ContextCompat.getColor(helpview.getContext(), R.color.loginBackground));
                    likeCount.setTextColor(ContextCompat.getColor(helpview.getContext(), R.color.loginBackground));
                }

                if (p.getIsdisliked() == 1) {
                    mDislike.setColorFilter(ContextCompat.getColor(helpview.getContext(), R.color.danger));
                    dislikeCount.setTextColor(ContextCompat.getColor(helpview.getContext(), R.color.danger));
                }


                if (p.getIscommented() > 0) {
                    mComment.setColorFilter(ContextCompat.getColor(helpview.getContext(), R.color.success));
                    commentCount.setTextColor(ContextCompat.getColor(helpview.getContext(), R.color.success));
                }
            }

        }

        final GestureDetector gd = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {


            //here is the method for double tap


            @Override
            public boolean onDoubleTap(MotionEvent e) {
                LikeDT.setVisibility(View.VISIBLE);
                like();

                YoYo.with(Techniques.BounceIn)
                        .duration(700)
                        .repeat(0)
                        .playOn(LikeDT);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LikeDT.setVisibility(View.INVISIBLE);
                    }
                }, 1000);


                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                final CharSequence[] items = {"View Image"};
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            int pos = getAdapterPosition();
                            Posts p = allPosts.get(pos);
                            String url = p.getPost_image();
                            Intent intent = new Intent(helpview.getContext(), ImageZoomActivity.class);
                            intent.putExtra(IntentKeys.IMAGE_URL, url);
                            intent.putExtra(IntentKeys.IMAGE_TITLE, p.getUser_name() + "'s Image");
                            helpview.getContext().startActivity(intent);
                        }
                    }
                });
                builder.show();
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }


        });


        void like() {

            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(0)
                    .playOn(mLike);


            int pos = getAdapterPosition();
            Posts p = allPosts.get(pos);

            List<Like> likes = p.getLike();

            for (int i = 0; i < likes.size(); i++) {

                String myId = ApiUtil.getUserId();
                if (myId.equals(String.valueOf(likes.get(i).getUser_id()))) {

                    switch (i) {
                        case 0:
                            like1.setVisibility(View.GONE);
                            break;
                        case 1:
                            like2.setVisibility(View.GONE);
                            break;
                        case 2:
                            like3.setVisibility(View.GONE);
                            break;
                    }
                }
            }

            if (p.getIsliked() == 1) {
                resetLike();
                likeCount.setText(String.valueOf(p.getLikes() - 1));
                p.setLikes(p.getLikes() - 1);
                p.setIsliked(0);
                like0.setVisibility(View.GONE);
            } else if (p.getIsdisliked() == 1) {
                likeList.setVisibility(View.VISIBLE);
                resetLike();
                likeActive();
                likeCount.setText(String.valueOf(p.getLikes() + 1));
                p.setLikes(p.getLikes() + 1);
                p.setIsliked(1);
                dislikeCount.setText(String.valueOf(p.getDislikes() - 1));
                p.setIsdisliked(0);
                p.setDislikes(p.getDislikes() - 1);
                showLikeImage();

            } else {
                likeList.setVisibility(View.VISIBLE);
                resetLike();
                likeActive();
                likeCount.setText(String.valueOf(p.getLikes() + 1));
                p.setLikes(p.getLikes() + 1);
                p.setIsliked(1);
                showLikeImage();
            }
            likeApi(p.getId(), 1);
            updateHomePosts(p, pos, p.getIsliked(), p.getLikes(), p.getIsdisliked(), p.getDislikes());

        }


        void updateHomePosts(Posts p, int pos, int isLiked, int likes, int isDisliked, int dislikes) {
            String posts = Preferences.getSavedHomePosts(mContext);
            try {
                JSONArray arr1 = new JSONArray(posts);
                JSONObject obj1 = new JSONObject(String.valueOf(arr1.get(pos)));
                JSONArray obj2 = new JSONArray(String.valueOf(obj1.get("like_all")));
                JSONArray obj3 = new JSONArray();
                JSONObject newLike = new JSONObject();
                newLike.put("post_id", p.getId());
                newLike.put("user_id", ApiUtil.getUserId());
                newLike.put("user_name", ApiUtil.getName());
                newLike.put("username", ApiUtil.getUserName());
                newLike.put("user_picture", ApiUtil.getProfileSmall());

                String like1Json = obj2.getString(0);
                String like2Json = obj2.getString(1);
                String like3Json = obj2.getString(2);

                if (isLiked == 1) {
                    obj3.put(0, newLike);

                    if (like1Json != null) {
                        JSONObject like1 = new JSONObject(like1Json);
                        obj3.put(1, like1);
                    }

                    if (like2Json != null) {
                        JSONObject like2 = new JSONObject(like2Json);
                        obj3.put(2, like2);
                    }


                } else {
                    int counter = 0;
                    if (like1Json != null) {
                        JSONObject like1 = new JSONObject(like1Json);
                        int user_id = like1.getInt("user_id");
                        if (!ApiUtil.getUserId().equals(String.valueOf(user_id)))
                            obj3.put(counter++, like1);
                    }

                    if (like2Json != null) {
                        JSONObject like2 = new JSONObject(like2Json);
                        int user_id = like2.getInt("user_id");
                        if (!ApiUtil.getUserId().equals(String.valueOf(user_id)))
                            obj3.put(counter++, like2);
                    }

                    if (like3Json != null) {
                        JSONObject like3 = new JSONObject(like3Json);
                        int user_id = like3.getInt("user_id");
                        if (!ApiUtil.getUserId().equals(String.valueOf(user_id)))
                            obj3.put(counter++, like3);
                    }


                }
                obj1.put("isliked", isLiked);
                obj1.put("likes", likes);
                obj1.put("isdisliked", isDisliked);
                obj1.put("dislikes", dislikes);
                obj1.put("like_all", obj3);
                arr1.put(pos, obj1);
                Preferences.saveHomePosts(arr1, mContext);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        void showLikeImage() {
            like0.setVisibility(View.VISIBLE);
            imageLoader.displayImage(ApiUtil.getProfileSmall(), like0);
        }

        void hideLikeImage() {
            like0.setVisibility(View.GONE);
            imageLoader.displayImage("drawable://" + R.drawable.face, like0);
        }

        void resetLike() {
            mLike.setColorFilter(ContextCompat.getColor(helpview.getContext(), R.color.light));
            likeCount.setTextColor(ContextCompat.getColor(helpview.getContext(), R.color.light));
            mDislike.setColorFilter(ContextCompat.getColor(helpview.getContext(), R.color.light));
            dislikeCount.setTextColor(ContextCompat.getColor(helpview.getContext(), R.color.light));
            mComment.setColorFilter(ContextCompat.getColor(helpview.getContext(), R.color.light));
            commentCount.setTextColor(ContextCompat.getColor(helpview.getContext(), R.color.light));
        }

        void likeActive() {
            mLike.setColorFilter(ContextCompat.getColor(helpview.getContext(), R.color.loginBackground));
            likeCount.setTextColor(ContextCompat.getColor(helpview.getContext(), R.color.loginBackground));
        }

        void dislikeActive() {
            mDislike.setColorFilter(ContextCompat.getColor(helpview.getContext(), R.color.danger));
            dislikeCount.setTextColor(ContextCompat.getColor(helpview.getContext(), R.color.danger));
        }


        void dislike() {

            int pos = getAdapterPosition();
            Posts p = allPosts.get(pos);


            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(0)
                    .playOn(mDislike);

            List<Like> likes = p.getLike();

            for (int i = 0; i < likes.size(); i++) {

                String myId = ApiUtil.getUserId();
                if (myId.equals(String.valueOf(likes.get(i).getUser_id()))) {

                    switch (i) {
                        case 0:
                            like1.setVisibility(View.GONE);
                            break;
                        case 1:
                            like2.setVisibility(View.GONE);
                            break;
                        case 2:
                            like3.setVisibility(View.GONE);
                            break;
                    }
                }

            }


            if (p.getIsdisliked() == 1) {

                resetLike();
                dislikeCount.setText(String.valueOf(p.getDislikes() - 1));
                p.setDislikes(p.getDislikes() - 1);
                p.setIsdisliked(0);
            } else if (p.getIsliked() == 1) {
                resetLike();
                dislikeActive();
                dislikeCount.setText(String.valueOf(p.getDislikes() + 1));
                p.setDislikes(p.getDislikes() + 1);
                p.setIsdisliked(1);
                likeCount.setText(String.valueOf(p.getLikes() - 1));
                p.setIsliked(0);
                p.setLikes(p.getLikes() - 1);
                hideLikeImage();

            } else {
                resetLike();
                dislikeActive();
                dislikeCount.setText(String.valueOf(p.getDislikes() + 1));
                p.setDislikes(p.getDislikes() + 1);
                p.setIsdisliked(1);
            }
            likeApi(p.getId(), 0);
            updateHomePosts(p, pos, p.getIsliked(), p.getLikes(), p.getIsdisliked(), p.getDislikes());

        }

        void comment() {
            Intent intent = new Intent(helpview.getContext(), CommentList.class);
            int pos = getAdapterPosition();
            Posts p = allPosts.get(pos);
            intent.putExtra(IntentKeys.COMMENTS, p.getId());
            intent.putExtra(IntentKeys.COMMENT_COUNTS, p.getComments());
            intent.putExtra(IntentKeys.ADDRESS, mAddress);
            helpview.getContext().startActivity(intent);
        }

        void allLikes() {
            Intent intent = new Intent(helpview.getContext(), PeopleList.class);
            int pos = getAdapterPosition();
            Posts p = allPosts.get(pos);
            intent.putExtra(IntentKeys.SOURCEID, p.getId());
            intent.putExtra(IntentKeys.SUBTITLE, p.getLikes() + " likes");
            intent.putExtra(IntentKeys.TITLE, "Likes");
            intent.putExtra(IntentKeys.TYPE, -1);
            helpview.getContext().startActivity(intent);
        }

        void likeApi(final int postID, final int likeType) {

            String url = ApiUtil.getLikeUrl();

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
                    params.put("post_id", String.valueOf(postID));
                    params.put("like_type", String.valueOf(likeType));
                    return params;
                }

            };
            RequestManager.getInstance(helpview.getContext()).addToRequestQueue(stringRequest);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            mOnClickListener.onListItemClickPost(pos, mAddress);
        }


    }
}
