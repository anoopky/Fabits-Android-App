package in.fabits.fabits.adapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import in.fabits.fabits.PeopleList;
import in.fabits.fabits.PostSingleActivity;
import in.fabits.fabits.R;
import in.fabits.fabits.api.IntentKeys;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.model.Pool;
import in.fabits.fabits.model.Posts;

public class PoolCustomGrid extends BaseAdapter {

    private Context mContext;
    private List<Pool> mPools;
    private ImageLoader imageLoader;

    public PoolCustomGrid(Context c, List<Pool> pools) {
        mContext = c;
        this.mPools = pools;
        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions opts = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(c)
                .defaultDisplayImageOptions(opts).build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public int getCount() {
        return mPools.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.pool_item, null);
        } else {
            grid = convertView;
        }


        TextView name = (TextView) grid.findViewById(R.id.PoolName);
        ImageView imageView = (ImageView) grid.findViewById(R.id.poolImage);


        final Pool p = mPools.get(position);
        name.setText(p.getName());
        imageLoader.displayImage(p.getImage(), imageView);

        final View finalGrid = grid;


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (p.getSearch_id().equals("287")) {
                    Toast.makeText(mContext, "Comming Soon.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(finalGrid.getContext(), PostSingleActivity.class);
                    intent.putExtra(IntentKeys.SEARCH, p.getSearch_id());
                    intent.putExtra(IntentKeys.TYPE, Utils.PoolSearch);
                    intent.putExtra(IntentKeys.SUBTITLE, "");
                    intent.putExtra(IntentKeys.TITLE, p.getName());


                    finalGrid.getContext().startActivity(intent);
                }
            }
        });


        return grid;
    }
}
