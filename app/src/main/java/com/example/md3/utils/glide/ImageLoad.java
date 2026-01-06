package com.example.md3.utils.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.request.RequestListener;



public interface ImageLoad {
    void loadImage(Drawable drawable, ImageView imageView);
    void loadGif(int resID, ImageView imageView, GlideImageLoader.GifFinishCallBack gifFinishCallBack);
    void loadImage(String url, ImageView imageView);
    void loadImage(Uri uri, ImageView imageView);
    void loadImage(String url, RequestListener<Drawable> listener);
    void loadImage(String url, ImageView imageView, ProgressBar progressBar);
    void loadRoundedCornerImage(String url, ImageView imageView, int roundRadius);
    void loadRoundedCornerImage(Bitmap uri, ImageView imageView, int roundRadius);
    void loadCircleImage(String url, ImageView imageView);

    void loanImageWIthPreLoad(String url, ImageView imageView, ProgressBar progressBar);

  void   loadImageWithProgress(Uri uri, ImageView imageView,ProgressBar progressBar);

//     void loadZoomImage(String url, ZoomableImageView imageView, final ProgressBar progressBar);
//   void loadRoundedCornerShipmentImage(String url, ImageView imageView, int roundRadius);
}
