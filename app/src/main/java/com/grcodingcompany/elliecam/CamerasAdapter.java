package com.grcodingcompany.elliecam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class CamerasAdapter extends BaseAdapter {
    private static LayoutInflater inflater=null;

    private List<Camera> mCameras;

    public CamerasAdapter(Context c) {
        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mCameras == null ? 0 : mCameras.size();
    }

    public Object getItem(int position) {
        return mCameras.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public void setItems(List<Camera> items) {
        this.mCameras = items;
    }

    private static class Holder
    {
        TextView cameraName;
        CameraFeedImageView cameraPreview;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        Camera camera = mCameras.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.camera_grid_item, null);

            holder = new Holder();

            holder.cameraName = (TextView) convertView.findViewById(R.id.cameraName);
            holder.cameraPreview = (CameraFeedImageView) convertView.findViewById(R.id.cameraPreview);
            holder.cameraPreview.setCamera(camera);

            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }

        holder.cameraName.setText(camera.name);

        TextView status = (TextView) convertView.findViewById(R.id.cameraStatus);
        status.setTextColor(ContextCompat.getColor(convertView.getContext(), camera.status ? R.color.online : R.color.offline));
        status.setText(camera.status ? R.string.online : R.string.offline);

        if (camera.thumbnailSnapshot == null) {
            holder.cameraPreview.setImageResource(R.drawable.no_preview);
        } else {
//            if (camera.thumbnailSnapshot.substring(0, 9).equals("@drawable")) {
                Context context = holder.cameraPreview.getContext();
                int id = context.getResources().getIdentifier(camera.thumbnailSnapshot, "drawable", context.getPackageName());
                holder.cameraPreview.setImageResource(id);
//            }
//            Bitmap cover = BitmapFactory.decodeFile(camera.thumbnailSnapshot);
//            holder.cameraPreview.setImageBitmap(cover);
        }

        return convertView;
    }
}
