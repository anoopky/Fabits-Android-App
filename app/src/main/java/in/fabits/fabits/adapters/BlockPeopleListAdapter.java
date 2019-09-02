package in.fabits.fabits.adapters;

import android.content.Context;
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
import in.fabits.fabits.R;
import in.fabits.fabits.model.UsersList;


public class BlockPeopleListAdapter extends RecyclerView.Adapter<BlockPeopleListAdapter.ViewHolder> {

    private List<UsersList> mUserList = new ArrayList<>();
    private ImageLoader imageLoader = null;

    public BlockPeopleListAdapter(List<UsersList> mUserList, ListItemClickListener OnClickListener) {
        this.mUserList = mUserList;
        this.mOnClickListener = OnClickListener;
    }

    public interface ListItemClickListener {

        void onListItemClick(int id);
    }

    private ListItemClickListener mOnClickListener;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.block_users_list;
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
        return mUserList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView image;
        TextView name;
        ImageView cross;

        ViewHolder(View itemView) {
            super(itemView);
            image = (CircleImageView) itemView.findViewById(R.id.chat_image);
            name = (TextView) itemView.findViewById(R.id.name);
            cross = (ImageView) itemView.findViewById(R.id.cross);
            cross.setOnClickListener(this);
        }

        void bind(int listIndex) {

                UsersList users = mUserList.get(listIndex);
                name.setText(users.getName());
                imageLoader.displayImage(String.valueOf(users.getUserPicture()), image);

        }


        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            mOnClickListener.onListItemClick(pos);

        }

    }
}
