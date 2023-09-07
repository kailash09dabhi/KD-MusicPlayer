package com.kingbull.musicplayer.ui.base.ads;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.kingbull.musicplayer.R;

/**
 * class expects that the Activity should have adView.
 *
 * @author Kailash Dabhi
 * @date 10/3/2016.
 */
public final class AdmobBannerLoaded {
  private final AdView adView;

  public AdmobBannerLoaded(ViewGroup rootView) {
    adView = (AdView) rootView.findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder()
        .build();
    adView.loadAd(adRequest);
    adView.setAdListener(new AdListener() {
      @Override public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
        adView.setVisibility(View.GONE);
      }
    });
  }
}
