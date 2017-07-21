package com.example.lynnik.photogallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

public class PhotoGalleryFragment extends Fragment {

  private static final String TAG = "PhotoGalleryFragment";

  private RecyclerView mRecyclerView;

  public static PhotoGalleryFragment newInstance() {
    return new PhotoGalleryFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    new FetchItemTask().execute();
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

    return v;
  }

  private class FetchItemTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
      try {
        String result = new FlickrFetchr()
            .getUrlString("https://www.bignerdranch.com");
        Log.i(TAG, "Fetched contents of URL: " + result);
      } catch (IOException ioe) {
        Log.e(TAG, "Failed to fetch URL: ", ioe);
      }

      return null;
    }
  }
}