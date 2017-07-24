package com.example.lynnik.photogallery;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class PhotoGalleryFragment extends Fragment {

  private static final String TAG = "PhotoGalleryFragment";

  private RecyclerView mRecyclerView;
  private List<GalleryItem> mItems = new ArrayList<>();
  private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;

  public static PhotoGalleryFragment newInstance() {
    return new PhotoGalleryFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    new FetchItemTask().execute();

    Handler responseHandler = new Handler();
    mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
    mThumbnailDownloader.setThumbnailDownloadListener(
        new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>() {
          @Override
          public void onThumbnailDownloaded(
              PhotoHolder target, Bitmap thumbnail) {
            Drawable drawable = new BitmapDrawable(getResources(), thumbnail);
            target.bindGalleryItem(drawable);
          }
        });

    mThumbnailDownloader.start();
    mThumbnailDownloader.getLooper();
    Log.i(TAG, "Background thread started.");
  }

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater
        .inflate(R.layout.fragment_photo_gallery, container, false);

    mRecyclerView = (RecyclerView)
        v.findViewById(R.id.fragment_photo_gallery_recycler_view);
    mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

    setupAdapter();

    return v;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mThumbnailDownloader.clearQueue();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mThumbnailDownloader.quit();
    Log.i(TAG, "Background thread destroyed.");
  }

  private void setupAdapter() {
    if (isAdded())
      mRecyclerView.setAdapter(new PhotoAdapter(mItems));
  }

  private class PhotoHolder extends RecyclerView.ViewHolder {

    private ImageView mTitleImageView;

    public PhotoHolder(View itemView) {
      super(itemView);

      mTitleImageView = (ImageView) itemView
          .findViewById(R.id.fragment_photo_gallery_image_view);
    }

    public void bindGalleryItem(Drawable drawable) {
      mTitleImageView.setImageDrawable(drawable);
    }
  }

  private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

    private List<GalleryItem> mGalleryItems;

    public PhotoAdapter(List<GalleryItem> galleryItems) {
      mGalleryItems = galleryItems;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(getActivity());
      View view = inflater.inflate(R.layout.gallery_item, parent, false);

      return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
      GalleryItem galleryItem = mGalleryItems.get(position);

      Drawable placeholder = getResources().getDrawable(R.drawable.ic_placeholder);
      holder.bindGalleryItem(placeholder);
      mThumbnailDownloader.queueThumbnail(holder, galleryItem.getUrl());
    }

    @Override
    public int getItemCount() {
      return mGalleryItems.size();
    }
  }

  private class FetchItemTask extends AsyncTask<Void, Void, List<GalleryItem>> {

    @Override
    protected List<GalleryItem> doInBackground(Void... voids) {
      return new FlickrFetchr().fetchItems();
    }

    @Override
    protected void onPostExecute(List<GalleryItem> galleryItems) {
      mItems = galleryItems;
      setupAdapter();
    }
  }
}