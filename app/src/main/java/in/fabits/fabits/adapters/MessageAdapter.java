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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.fabits.fabits.ImageZoomActivity;
import in.fabits.fabits.R;
import in.fabits.fabits.api.IntentKeys;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.model.Message;
import in.fabits.fabits.model.Seen;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    //    private static final String TAG = ChatListAdapter.class.getSimpleName();
    private List<Message> mMessageLists = new ArrayList<>();
    private ImageLoader imageLoader = null;
    private Seen mSeen;

    public interface ListItemLongClickListener {

        void onListItemClick(int id);
    }

    private ListItemLongClickListener mOnClickListener;
    String name;
    public MessageAdapter(List<Message> MessageLists, Seen seen, ListItemLongClickListener listen, String name) {
        this.mMessageLists = MessageLists;
        this.mSeen = seen;
        this.name = name;
        mOnClickListener = listen;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem;
        if (viewType == 1)
            layoutIdForListItem = R.layout.chatright_item;
        else
            layoutIdForListItem = R.layout.chatleft_item;
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
        return mMessageLists.size();
    }

    public void setMessage(List<Message> message, Seen seen) {
        mMessageLists = message;
        this.mSeen = seen;
    }

    @Override
    public int getItemViewType(int position) {
        Message p = mMessageLists.get(position);
        if (p.IsMe())
            return 1;
        else
            return 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        CircleImageView image;
        TextView msg;
        TextView time;
        FrameLayout seenFrame;
        CircleImageView seenUser;
        View blur;
        Context helperContext;
        ImageView imageMessage;

        ViewHolder(final View itemView) {
            super(itemView);
            helperContext = itemView.getContext();
            msg = (TextView) itemView.findViewById(R.id.msg);
            time = (TextView) itemView.findViewById(R.id.time);
            seenFrame = (FrameLayout) itemView.findViewById(R.id.seenframe);
            seenUser = (CircleImageView) itemView.findViewById(R.id.seenuserImage);
            blur = itemView.findViewById(R.id.blur);
            imageMessage = (ImageView) itemView.findViewById(R.id.imageMessage);
            itemView.setOnLongClickListener(this);


        }

        void bind(int listIndex) {
            Message p = mMessageLists.get(listIndex);
            seenFrame.setVisibility(View.GONE);
            blur.setVisibility(View.VISIBLE);

            if (mSeen != null) {
                String seenImage = mSeen.getImage();
                int readId = mSeen.getUserLastRead();

                int deliveredId = mSeen.getUserLastDelivered();
                if (p.getId() == deliveredId) {
                    seenFrame.setVisibility(View.VISIBLE);
                    imageLoader.displayImage(seenImage, seenUser);
                }

                if (p.getId() == readId) {
                    blur.setVisibility(View.GONE);
                    seenFrame.setVisibility(View.VISIBLE);
                    imageLoader.displayImage(seenImage, seenUser);
                }

            }

            if (!p.IsMe()) {
                image = (CircleImageView) itemView.findViewById(R.id.userImage);
                imageLoader.displayImage(String.valueOf(p.getImage()), image);
            }
            time.setVisibility(View.VISIBLE);

            if (listIndex != 0) {
                Message p1 = mMessageLists.get(listIndex - 1);

                if (p.IsMe() != p1.IsMe())
                    time.setText(p.getTime());
                else {
                    if (p.getTime().equals(p1.getTime()))
                        time.setVisibility(View.GONE);
                }
            } else {
                time.setText(p.getTime());

            }
            imageMessage.setVisibility(View.GONE);
            if (p.getMessage().contains("[IMAGE]")) {
                final Message p1 = p;
                imageMessage.setVisibility(View.VISIBLE);

                String Message = p.getMessage();
                String helper = Message.substring(
                        Message.indexOf("[IMAGE][[") + "[IMAGE][[".length(),
                        Message.length()
                );
                final String imageUrl = helper.substring(
                        0,
                        helper.indexOf("][")
                );

                String message = helper.substring(
                        helper.indexOf("][") + "][".length(),
                        helper.indexOf("]]")
                );
                imageLoader.displayImage(imageUrl, imageMessage);

                imageMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(helperContext, ImageZoomActivity.class);
                        intent.putExtra(IntentKeys.IMAGE_URL, imageUrl);
                        intent.putExtra(IntentKeys.IMAGE_TITLE, name+"'s Image");
                        helperContext.startActivity(intent);
                    }
                });

                msg.setText(Html.fromHtml(Utils.EmojiDecoder(message), new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {
                        int resourceId = helperContext.getResources().getIdentifier(source, "drawable",
                                helperContext.getPackageName());
                        Drawable drawable = helperContext.getResources().getDrawable(resourceId);
                        drawable.setBounds(0, 0, 45, 45);
                        return drawable;
                    }
                }, null));


            } else
                msg.setText(Html.fromHtml(Utils.EmojiDecoder(p.getMessage()), new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {
                        int resourceId = helperContext.getResources().getIdentifier(source, "drawable",
                                helperContext.getPackageName());
                        Drawable drawable = helperContext.getResources().getDrawable(resourceId);
                        drawable.setBounds(0, 0, 45, 45);
                        return drawable;
                    }
                }, null));

        }


        @Override
        public boolean onLongClick(View v) {

            int pos = getAdapterPosition();
            mOnClickListener.onListItemClick(pos);

            return true;
        }
    }
}
