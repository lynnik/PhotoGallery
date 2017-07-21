package com.example.lynnik.photogallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PhotoGalleryFragment extends Fragment {

  private static final String TAG = "PhotoGalleryFragment";

  private RecyclerView mRecyclerView;
  private List<GalleryItem> mItems = new ArrayList<>();

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

    setupAdapter();

    return v;
  }

  private void setupAdapter() {
    if (isAdded())
      mRecyclerView.setAdapter(new PhotoAdapter(mItems));
  }

  private class PhotoHolder extends RecyclerView.ViewHolder {

    private TextView mTitleTextView;

    public PhotoHolder(View itemView) {
      super(itemView);

      mTitleTextView = (TextView) itemView;
    }

    public void bindGalleryItem(GalleryItem item) {
      mTitleTextView.setText(item.toString());
    }
  }

  private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

    private List<GalleryItem> mGalleryItems;

    public PhotoAdapter(List<GalleryItem> galleryItems) {
      mGalleryItems = galleryItems;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      TextView textView = new TextView(getActivity());
      return new PhotoHolder(textView);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
      GalleryItem galleryItem = mGalleryItems.get(position);
      holder.bindGalleryItem(galleryItem);
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