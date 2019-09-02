package in.fabits.fabits.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import in.fabits.fabits.R;

public class CustomGrid extends BaseAdapter {

    private Context mContext;
    private final String[] Imageid;

    public CustomGrid(Context c,String[] Imageid ) {
        mContext = c;
        this.Imageid = Imageid;

    }

    @Override
    public int getCount() {
        return Imageid.length;
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
            grid = inflater.inflate(R.layout.emojie_single, null);
        } else {
            grid = convertView;
        }

        ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
        String abc = Imageid[position];
        int imageResource = mContext.getResources().getIdentifier(abc, "drawable", mContext.getPackageName());
        try {
            imageView.setImageResource(imageResource);
        }
        catch (Exception e){
        }
        return grid;
    }
}
