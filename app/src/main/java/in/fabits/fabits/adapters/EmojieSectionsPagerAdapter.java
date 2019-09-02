package in.fabits.fabits.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import in.fabits.fabits.placeholder.EmojiesPlaceholderFragment;
import in.fabits.fabits.placeholder.PlaceholderFragment;


public class EmojieSectionsPagerAdapter extends FragmentPagerAdapter {

    private EmojiesPlaceholderFragment.ListItemClickListener mOnClickListener;

    public EmojieSectionsPagerAdapter(FragmentManager fm, EmojiesPlaceholderFragment.ListItemClickListener mOnClickListener ) {
        super(fm);
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public Fragment getItem(int position) {
        return EmojiesPlaceholderFragment.newInstance(position + 1, mOnClickListener);
    }

    @Override
    public int getCount() {
        return 9;
    }





}