package com.example.md3.utils.glide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;


public class GlideImageLoader implements ImageLoad {

    private RequestManager request_manager;
    private RequestOptions diskCaching;

    public GlideImageLoader(Context context) {
        request_manager = Glide.with(context).applyDefaultRequestOptions(RequestOptions.timeoutOf(5 * 60 * 1000));
        diskCaching = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL);
    }


    @Override
    public void loadImage(Drawable drawable, ImageView imageView) {
        request_manager.load(drawable)
                .apply(diskCaching)
                .into(imageView);
    }

    @Override
    public void loadGif(int resID, ImageView imageView,GifFinishCallBack gifFinishCallBack) {
//        request_manager.load(resID).into(new GifHelper(imageView, 1));
        request_manager.load(resID).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (resource instanceof GifDrawable) {
                    ((GifDrawable)resource).setLoopCount(1);

                    ((GifDrawable)resource).registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                        @Override
                        public void onAnimationStart(Drawable drawable) {
                            super.onAnimationStart(drawable);
                        }

                        @Override
                        public void onAnimationEnd(Drawable drawable) {
                            gifFinishCallBack.funOnAnimationFinished();
                            super.onAnimationEnd(drawable);
                        }
                    });

                }
                return false;
            }
        }).into(imageView);
    }

    @Override
    public void loadImage(String url, ImageView imageView) {
        request_manager.load(url)
//                .placeholder(R.drawable.placeholder)
                .apply(diskCaching)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    public void loadImage(Uri uri, ImageView imageView) {
        request_manager.load(uri.getPath())
//                .placeholder(R.drawable.placeholder)
                .apply(diskCaching)
                .into(imageView);
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadImage(String url, RequestListener<Drawable> listener) {
        request_manager.load(url)
                .apply(diskCaching)
                .listener(listener).preload();
    }

    @Override
    public void loadImage(String url, ImageView imageView, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);

        request_manager.load(url)
                .apply(diskCaching)
                .listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(imageView);
    }


    public void loadImageWithImagePlaceHolder(String url, ImageView imageView, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);

        request_manager.load(url)
                .apply(diskCaching)
                .thumbnail(0.1f)
//                .placeholder(R.drawable.image_place_holder)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(imageView);
    }

    @Override
    public void loadRoundedCornerImage(String url, ImageView imageView, int roundRadius) {
        request_manager.load(url)
//                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(roundRadius)))
                .apply(diskCaching)
                .into(imageView);
    }



    public void loadImageWithImagePlaceHolderProfile(String url, ImageView imageView, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);

        request_manager.load(url)
                .apply(diskCaching)
                .thumbnail(0.1f)
//                .placeholder(R.drawable.upload_logo)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(imageView);
    }

    @Override
    public void loadRoundedCornerImage(Bitmap bitmap, ImageView imageView, int roundRadius) {
        request_manager.load(bitmap)
//                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(roundRadius)))
                .apply(diskCaching)
                .into(imageView);
    }

    @Override
    public void loadCircleImage(String url, ImageView imageView){
        request_manager.load(url)
//                .placeholder(R.drawable.placeholder)
                .apply(RequestOptions.circleCropTransform())
                .apply(diskCaching)
                .into(imageView);
    }

    @Override
    public void loanImageWIthPreLoad(String url, ImageView imageView, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        request_manager.load(url)
                .apply(diskCaching)
                .thumbnail(0.1f)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        progressBar.setVisibility(View.GONE);
                        imageView.setImageDrawable(resource);

                        return true;
                    }
                }).preload();
    }

//    @Override
//    public void loadZoomImage(String url, ZoomableImageView imageView, ProgressBar progressBar) {
//        progressBar.setVisibility(View.VISIBLE);
//        request_manager.load(url)
//                .apply(diskCaching)
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        progressBar.setVisibility(View.GONE);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        progressBar.setVisibility(View.GONE);
//                        return false;
//                    }
//                }).into(imageView);
//    }


//    public void loadProfileImage(String url, ImageView imageView){
//        request_manager.load(url).placeholder(R.drawable.default_profile)
//                .apply(RequestOptions.circleCropTransform())
//                .apply(diskCaching)
//                .into(imageView);
//    }



    @Override
    public void loadImageWithProgress(Uri uri, ImageView imageView,ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        request_manager.load(uri.getPath())
//                .placeholder(R.drawable.placeholder)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        progressBar.setVisibility(View.GONE);
                        imageView.setImageDrawable(resource);
                        return false;
                    }
                })
                .apply(diskCaching)
                .into(imageView);
    }
   public interface GifFinishCallBack{

        void funOnAnimationFinished();
    }



}
