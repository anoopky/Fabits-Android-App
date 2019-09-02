package in.fabits.fabits;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import in.fabits.fabits.adapters.NotificationAdapter;
import in.fabits.fabits.adapters.SettingsAdapter;
import in.fabits.fabits.model.Notification;

public class SettingsActivity extends AppCompatActivity {

    SettingsAdapter notificationAdapter;
    RecyclerView recyclerView;
    List<String> mSettings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar mToolbar;

//For Toolbar (Action bar) start
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Settings");

        recyclerView = (RecyclerView) findViewById(R.id.settingsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mSettings.add("-");
        mSettings.add("Personal Info.");
        mSettings.add("Phone");
//        mSettings.add("Chat");
        mSettings.add("Password");
        mSettings.add("Privacy");
        mSettings.add("Alerts");
        mSettings.add("Blocks");
        mSettings.add("Contact Us");
        mSettings.add("Logout");

        notificationAdapter = new SettingsAdapter(mSettings);
        recyclerView.setAdapter(notificationAdapter);

    }
}
