package in.fabits.fabits.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

import in.fabits.fabits.R;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.Utils;


public class AlertFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    ListPreference login;
    ListPreference message;
    ListPreference notification;
    ListPreference AMessage;

    SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_alert);
        sharedPreferences = getContext().getSharedPreferences(getDefaultSharedPreferencesName(getContext()), 0);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        login = (ListPreference) findPreference("S_A_LOGIN");
        message = (ListPreference) findPreference("S_A_MESSAGE");
        notification = (ListPreference) findPreference("S_A_NOTIFICATION");
        AMessage = (ListPreference) findPreference("S_A_ANONY_MESSAGE");
        init();

    }


    void init() {

        String data = Preferences.getSavedSetting(Preferences.S_A_LOGIN, getContext());
        login.setSummary(data);

        String emailAdd = Preferences.getSavedSetting(Preferences.S_A_MESSAGE, getContext());
        message.setSummary(emailAdd);

        String face = Preferences.getSavedSetting(Preferences.S_A_NOTIFICATION, getContext());
        notification.setSummary(face);

        String what = Preferences.getSavedSetting(Preferences.S_A_ANONY_MESSAGE, getContext());
        AMessage.setSummary(what);
    }

    private static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Preferences.S_A_LOGIN)) {


            String prev = login.getSummary().toString();

            String data = Preferences.getSavedSetting(key, getContext());
            if (!Utils.seetingItsME) {
                Utils.updateSetting(data,
                        key,
                        getContext(), prev);
            }
            Utils.seetingItsME = false;
            data = Preferences.getSavedSetting(key, getContext());
            login.setSummary(data);


        } else if (key.equals(Preferences.S_A_MESSAGE)) {
            String prev = message.getSummary().toString();

            String data = Preferences.getSavedSetting(key, getContext());
            if (!Utils.seetingItsME) {
                Utils.updateSetting(data,
                        key,
                        getContext(), prev);
            }
            Utils.seetingItsME = false;
            data = Preferences.getSavedSetting(key, getContext());
            message.setSummary(data);



        } else if (key.equals(Preferences.S_A_NOTIFICATION)) {

            String prev = notification.getSummary().toString();

            String data = Preferences.getSavedSetting(key, getContext());
            if (!Utils.seetingItsME) {
                Utils.updateSetting(data,
                        key,
                        getContext(), prev);
            }
            Utils.seetingItsME = false;
            data = Preferences.getSavedSetting(key, getContext());

            notification.setSummary(data);


        } else if (key.equals(Preferences.S_A_ANONY_MESSAGE)) {

            String prev = AMessage.getSummary().toString();

            String data = Preferences.getSavedSetting(key, getContext());
            if (!Utils.seetingItsME) {
                Utils.updateSetting(data,
                        key,
                        getContext(), prev);
            }
            Utils.seetingItsME = false;
          data = Preferences.getSavedSetting(key, getContext());

            AMessage.setSummary(data);





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


