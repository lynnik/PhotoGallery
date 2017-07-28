package com.example.lynnik.photogallery;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PhotoPageFragment extends VisibleFragment {

  private static final String ARG_URI = "photo_page_url";

  private Uri mUri;
  private WebView mWebView;

  public static PhotoPageFragment newInstance(Uri uri) {
    Bundle args = new Bundle();
    args.putParcelable(ARG_URI, uri);

    PhotoPageFragment fragment = new PhotoPageFragment();
    fragment.setArguments(args);

    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mUri = getArguments().getParcelable(ARG_URI);
  }

  @SuppressLint("SetJavaScriptEnabled")
  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_photo_page, container, false);

    mWebView = (WebView) v.findViewById(R.id.fragment_photo_page_web_view);
    mWebView.getSettings().setJavaScriptEnabled(true);
    mWebView.setWebViewClient(new WebViewClient() {

      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
      }
    });
    mWebView.loadUrl(mUri.toString());

    return v;
  }
}