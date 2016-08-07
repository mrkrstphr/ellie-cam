package com.grcodingcompany.elliecam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class ViewFeed extends AppCompatActivity {

    SharedPreferences settings;
    Timer refreshTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settings = PreferenceManager.getDefaultSharedPreferences(ViewFeed.this);

        fetchNewSnapshot();
    }

    @Override
    protected void onStart() {
        super.onStart();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                fetchNewSnapshot();
            }
        };


        refreshTimer = new Timer();
        refreshTimer.scheduleAtFixedRate(task, 250, 250);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (refreshTimer != null) {
            refreshTimer.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        refreshTimer.cancel();
        refreshTimer = null;
    }

    private void fetchNewSnapshot() {
        if (areSettingsReady()) {
            String url = settings.getString("camera_url", "") +
                    "/snapshot.cgi?user=" + settings.getString("camera_username", "") +
                    "&pwd=" + settings.getString("camera_password", "");
            new ImageFetcher((ImageView) findViewById(R.id.imageView)).execute(url);
        }
    }

    private boolean areSettingsReady() {
        return (settings.getString("camera_url", "") +
                settings.getString("camera_username", "") +
                settings.getString("camera_password", "")).length() > 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
