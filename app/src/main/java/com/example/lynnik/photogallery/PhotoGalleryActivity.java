package com.example.lynnik.photogallery;

import android.support.v4.app.Fragment;

public class PhotoGalleryActivity extends SingleFragmentActivity {

  @Override
  protected Fragment createFragment() {
    return PhotoGalleryFragment.newInstance();
  }
}