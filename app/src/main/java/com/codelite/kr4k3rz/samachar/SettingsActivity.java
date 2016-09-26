package com.codelite.kr4k3rz.samachar;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");
        getFragmentManager().beginTransaction().replace(R.id.fContent, new MyPrefFragment()).commit();

    }

    public static class MyPrefFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);

            Preference preference_editCategory = findPreference("editCategories");
            preference_editCategory.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), EditCategory.class);
                    startActivity(intent);
                    return true;
                }
            });

            Preference preference_about = findPreference("aboutPref");
            preference_about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), AboutActivity.class);
                    startActivity(intent);
                    return true;
                }
            });

            SwitchPreference disableImage = (SwitchPreference) findPreference("enableImage");
            disableImage.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    getActivity().finish();
                    Intent refresh = new Intent(getActivity(), SplashActivity.class);
                    refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(refresh);
                    return true;
                }
            });

            SwitchPreference pushNotification = (SwitchPreference) findPreference("pushNotification");
            pushNotification.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    getActivity().finish();
                    Intent refresh = new Intent(getActivity(), SplashActivity.class);
                    refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(refresh);
                    return true;
                }
            });

        }
    }


}
