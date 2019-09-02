package in.fabits.fabits.adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import in.fabits.fabits.R;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.RequestManager;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.dialogs.ProfileDialogMaster;
import in.fabits.fabits.model.Following;
import in.fabits.fabits.model.UsersList;


public class FacematchListAdapter extends RecyclerView.Adapter<FacematchListAdapter.ViewHolder> {

    private List<UsersList> mUserList = new ArrayList<>();
    private ImageLoader imageLoader = null;

    public FacematchListAdapter(List<UsersList> mUserList) {
        this.mUserList = mUserList;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.users_list;
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
        return mUserList.size() + 1;
    }

    public void setUserList(List<UsersList> posts) {
        mUserList = posts;
    }

    boolean isDone = false;

    public void Done() {
        isDone = true;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView image;
        TextView name;
        TextView time;
        Button follow;
        LinearLayout info;
        LinearLayout control;
        LinearLayout root;
        ProgressBar loading;

        Context mContext;

        ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            image = (CircleImageView) itemView.findViewById(R.id.chat_image);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            follow = (Button) itemView.findViewById(R.id.follow);

            info = (LinearLayout) itemView.findViewById(R.id.info);
            control = (LinearLayout) itemView.findViewById(R.id.control);
            root = (LinearLayout) itemView.findViewById(R.id.root_layout);
            loading = (ProgressBar) itemView.findViewById(R.id.loading);
            itemView.setOnClickListener(this);


            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProfileDialog();
                }
            });



        }

        void showProfileDialog() {
            ProfileDialogMaster alert = new ProfileDialogMaster(mContext);
            alert.showDialog(mContext, mUserList.get(getAdapterPosition()).getUserPicture().toString(),
                    String.valueOf(mUserList.get(getAdapterPosition()).getUserID()),
                    String.valueOf(mUserList.get(getAdapterPosition()).getName())
                    );
        }

        void bind(int listIndex) {

            if (listIndex == mUserList.size()) {
                image.setVisibility(View.GONE);
                info.setVisibility(View.GONE);
                control.setVisibility(View.GONE);
                if (!isDone)
                    root.setVisibility(View.VISIBLE);
                else
                    root.setVisibility(View.GONE);
            } else {
                root.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
                info.setVisibility(View.VISIBLE);
                control.setVisibility(View.GONE);
                final UsersList users = mUserList.get(listIndex);
                follow.setVisibility(View.GONE);
                name.setText(users.getName());
                time.setText(users.getTime());
                imageLoader.displayImage(String.valueOf(users.getUserPicture()), image);


            }

        }


        @Override
        public void onClick(View v) {


        }

    }
}
