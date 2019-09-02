package in.fabits.fabits.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

import in.fabits.fabits.R;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.Utils;


public class PrivacyFragment extends PreferenceFragmentCompat
        implements
        SharedPreferences.OnSharedPreferenceChangeListener{

    SharedPreferences sharedPreferences;
    ListPreference phone;
    ListPreference follower;
    ListPreference following;
    ListPreference facematch;



    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_privacy);
        sharedPreferences = getContext().getSharedPreferences(getDefaultSharedPreferencesName(getContext()), 0);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        phone = (ListPreference) findPreference("S_P_PHONE");
        follower = (ListPreference) findPreference("S_P_FOLLOWERS");
        following = (ListPreference) findPreference("S_P_FOLLOWING");
        facematch = (ListPreference) findPreference("S_P_FACEMATCH");
        init();
    }
    private static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }

    void init() {

        String data = Preferences.getSavedSetting(Preferences.S_P_PHONE, getContext());
        phone.setSummary(data);

        String emailAdd = Preferences.getSavedSetting(Preferences.S_P_FOLLOWERS, getContext());
        follower.setSummary(emailAdd);

        String face = Preferences.getSavedSetting(Preferences.S_P_FOLLOWING, getContext());
        following.setSummary(face);

        String what = Preferences.getSavedSetting(Preferences.S_P_FACEMATCH, getContext());
        facematch.setSummary(what);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(Preferences.S_P_PHONE)) {
            String prev = phone.getSummary().toString();

            String data = Preferences.getSavedSetting(key, getContext());
            if (!Utils.seetingItsME) {
                Utils.updateSetting(data,
                        key,
                        getContext(), prev);
            }
            Utils.seetingItsME = false;
            data = Preferences.getSavedSetting(key, getContext());
            phone.setSummary(data);


        } else if (key.equals(Preferences.S_P_FOLLOWERS)) {

            String prev = follower.getSummary().toString();

            String data = Preferences.getSavedSetting(key, getContext());
            if (!Utils.seetingItsME) {
                Utils.updateSetting(data,
                        key,
                        getContext(), prev);
            }
            Utils.seetingItsME = false;
            data = Preferences.getSavedSetting(key, getContext());
            follower.setSummary(data);


        } else if (key.equals(Preferences.S_P_FOLLOWING)) {

            String prev = following.getSummary().toString();

            String data = Preferences.getSavedSetting(key, getContext());
            if (!Utils.seetingItsME) {
                Utils.updateSetting(data,
                        key,
                        getContext(), prev);
            }
            Utils.seetingItsME = false;
            data = Preferences.getSavedSetting(key, getContext());
            following.setSummary(data);



        } else if (key.equals(Preferences.S_P_FACEMATCH)) {

            String prev = facematch.getSummary().toString();

            String data = Preferences.getSavedSetting(key, getContext());
            if (!Utils.seetingItsME) {
                Utils.updateSetting(data,
                        key,
                        getContext(), prev);
            }
            Utils.seetingItsME = false;          data = Preferences.getSavedSetting(key, getContext());

            facematch.setSummary(data);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //unregister the preferenceChange listener

        sharedPreferences .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregister the preference change listener

        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

}
