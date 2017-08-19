package com.grcodingcompany.elliecam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class ViewCameraActivity extends AppCompatActivity
        implements ConfirmRemoveCameraDialogFragment.ConfirmRemoveCameraDialogListener {

    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_camera);

        camera = Camera.find(getIntent().getLongExtra("camera", 0));

        setTitle(camera.name);

        ImageView cameraPreview = (ImageView) findViewById(R.id.cameraPreview);

        if (cameraPreview != null) {

            if (camera.thumbnailSnapshot == null) {
                cameraPreview.setImageResource(R.drawable.no_preview);
            } else {
                Bitmap cover = BitmapFactory.decodeFile(camera.thumbnailSnapshot);
                cameraPreview.setImageBitmap(cover);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.remove_camera) {
            DialogFragment dialog = new ConfirmRemoveCameraDialogFragment();
            dialog.show(getSupportFragmentManager(), "ConfirmRemoveCameraDialog");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        camera.delete();
        setResult(RESULT_OK);
        finishFromChild(this);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // do nothing, really
    }
}
