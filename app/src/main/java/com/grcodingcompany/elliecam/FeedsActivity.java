package com.grcodingcompany.elliecam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class FeedsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final int ADD_CAMERA = 1;
    static final int EDIT_CAMERA = 2;

    ListView cameraList;
    CamerasAdapter camerasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        cameraList = (ListView) findViewById(R.id.gridview);
        camerasAdapter = new CamerasAdapter(this);
        cameraList.setAdapter(camerasAdapter);

        refreshCameras();

        cameraList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(FeedsActivity.this, ViewCameraActivity.class);
                Camera selected = (Camera) cameraList.getAdapter().getItem(position);
                intent.putExtra("camera", selected.getId());
                startActivityForResult(intent, EDIT_CAMERA);
            }
        });

        TextView noCamerasNotice = (TextView) findViewById(R.id.no_cameras);

        cameraList.setVisibility(camerasAdapter.getCount() > 0 ? View.VISIBLE : View.GONE);
        noCamerasNotice.setVisibility(camerasAdapter.getCount() > 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feeds, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_camera) {
            Intent intent = new Intent(FeedsActivity.this, AddCameraActivity.class);
            startActivityForResult(intent, ADD_CAMERA);
        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD_CAMERA:
            case EDIT_CAMERA:
                refreshCameras();
                break;
        }
    }

    private void refreshCameras() {
        camerasAdapter.setItems(Camera.getAll());
        camerasAdapter.notifyDataSetChanged();
    }
}
