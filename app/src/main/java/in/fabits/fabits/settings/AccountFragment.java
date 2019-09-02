package in.fabits.fabits.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

import in.fabits.fabits.R;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.Utils;


public class AccountFragment extends PreferenceFragmentCompat
        implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    EditTextPreference username;
    EditTextPreference email;
    EditTextPreference facebook;
    EditTextPreference whatsapp;
    EditTextPreference location;
    ListPreference relationship;

    SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_account);
        sharedPreferences = getContext().getSharedPreferences(getDefaultSharedPreferencesName(getContext()), 0);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        username = (EditTextPreference) findPreference("S_USERNAME");
        email = (EditTextPreference) findPreference("S_EMAIL");
        facebook = (EditTextPreference) findPreference("S_FACEBOOK");
        whatsapp = (EditTextPreference) findPreference("S_WHATSAPP");
        location = (EditTextPreference) findPreference("S_LOCATION");
        relationship = (ListPreference) findPreference("S_RELATIONSHIP");
        init();
    }


    private static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }

    void init() {

        String data = Preferences.getSavedSetting(Preferences.S_USERNAME, getContext());
        username.setSummary(data);

        String emailAdd = Preferences.getSavedSetting(Preferences.S_EMAIL, getContext());
        email.setSummary(emailAdd);

        String face = Preferences.getSavedSetting(Preferences.S_FACEBOOK, getContext());
        facebook.setSummary(face);

        String what = Preferences.getSavedSetting(Preferences.S_WHATSAPP, getContext());
        whatsapp.setSummary(what);

        String loc = Preferences.getSavedSetting(Preferences.S_LOCATION, getContext());
        location.setSummary(loc);

        String relations = Preferences.getSavedSetting(Preferences.S_RELATIONSHIP, getContext());
        relationship.setSummary(relations);



    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


        if (key.equals(Preferences.S_USERNAME)) {
            String prev = username.getSummary().toString();
            String data = Preferences.getSavedSetting(key, getContext());
            if (!Utils.seetingItsME) {

                Utils.updateSetting(data,
                        key,
                        getContext(),
                        prev);
            }
            Utils.seetingItsME = false;
            data = Preferences.getSavedSetting(key, getContext());

            username.setSummary(data);

        } else if (key.equals(Preferences.S_EMAIL)) {
            String prev = email.getSummary().toString();

            String data = Preferences.getSavedSetting(key, getContext());
            if (!Utils.seetingItsME) {

                Utils.updateSetting(data,
                        key,
                        getContext(),
                        prev);
            }
            Utils.seetingItsME = false;
            data = Preferences.getSavedSetting(key, getContext());
            email.setSummary(data);

        } else if (key.equals(Preferences.S_FACEBOOK)) {
            String prev = facebook.getSummary().toString();

            String data = Preferences.getSavedSetting(key, getContext());
            if (!Utils.seetingItsME) {

                Utils.updateSetting(data,
                        key,
                        getContext(),
                        prev);
            }
            Utils.seetingItsME = false;
            data = Preferences.getSavedSetting(key, getContext());
            facebook.setSummary(data);


        } else if (key.equals(Preferences.S_WHATSAPP)) {
            String prev = whatsapp.getSummary().toString();

            String data = Preferences.getSavedSetting(key, getContext());
            if (!Utils.seetingItsME) {

                Utils.updateSetting(data,
                        key,
                        getContext(),
                        prev);
            }
            Utils.seetingItsME = false;
            data = Preferences.getSavedSetting(key, getContext());
            whatsapp.setSummary(data);


        } else if (key.equals(Preferences.S_LOCATION)) {
            String prev = location.getSummary().toString();

            String data = Preferences.getSavedSetting(key, getContext());
            if (!Utils.seetingItsME) {

                Utils.updateSetting(data,
                        key,
                        getContext(),
                        prev);
            }
            Utils.seetingItsME = false;
            data = Preferences.getSavedSetting(key, getContext());
            location.setSummary(data);


        } else if (key.equals(Preferences.S_RELATIONSHIP)) {

            String prev = relationship.getSummary().toString();

            String data = Preferences.getSavedSetting(key, getContext());
            if (!Utils.seetingItsME) {
            Utils.updateSetting(data,
                    key,
                    getContext(), prev);
            }
            Utils.seetingItsME = false;
            data = Preferences.getSavedSetting(key, getContext());
            relationship.setSummary(data);


        }

    }

    @Override
    public void onResume() {
        super.onResume();
        //unregister the preferenceChange listener
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregister the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}


