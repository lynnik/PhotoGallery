package com.example.lynnik.photogallery;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.util.Log;

public class PollService extends IntentService {

  private static final String TAG = "PollService";

  public static Intent newIntent(Context context) {
    return new Intent(context, PollService.class);
  }

  public PollService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    if (!isNetworkAvailableAndConnected()) {
      return;
    }

    Log.i(TAG, "Received an intent: " + intent);
  }

  private boolean isNetworkAvailableAndConnected() {
    ConnectivityManager cm = (ConnectivityManager)
        getSystemService(CONNECTIVITY_SERVICE);

    boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
    boolean isNetworkConnected = isNetworkAvailable &&
        cm.getActiveNetworkInfo().isConnected();

    return isNetworkConnected;
  }
}