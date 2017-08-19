package com.grcodingcompany.elliecam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

class ImageFetcher extends AsyncTask<String, Void, Bitmap> {
    ImageView imageDest;

    public ImageFetcher(ImageView imageDest) {
        this.imageDest = imageDest;
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap image = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            image = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage() + " - " + url);
        }
        return image;
    }

    protected void onPostExecute(Bitmap result) {
        imageDest.setImageBitmap(result);
        imageDest.invalidate();
    }
}