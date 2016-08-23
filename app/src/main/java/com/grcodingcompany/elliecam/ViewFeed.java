package com.grcodingcompany.elliecam;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.save_photo) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 99);
                return false;
            }

            saveSnapshot();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 99: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveSnapshot();
                }
            }
        }
    }

    private void saveSnapshot() {
        findViewById(R.id.imageView).buildDrawingCache();
        Bitmap bm = findViewById(R.id.imageView).getDrawingCache();

        try {
            String photoFile = MediaStore.Images.Media.insertImage(getContentResolver(), bm, "EllieCam", "EllieCam Snapshot");
            showSnapshotNotification(photoFile, bm);
        } catch (Exception e) {
            Toast.makeText(this, "Please try again later. " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showSnapshotNotification(String photoFile, Bitmap bm)
    {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(photoFile), "image/*");

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);


        Notification.Builder mBuilder =
                new Notification.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(bm)
                        .setContentTitle("EllieCam")
                        .setContentText("Snapshot saved! Click to view")
                        .setContentIntent(pIntent)
                        .setStyle(new Notification.BigPictureStyle()
                                .bigPicture(bm));

        Notification notify = mBuilder.build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(55, notify);
    }
}
