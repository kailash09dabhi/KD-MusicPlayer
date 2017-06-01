package com.kingbull.musicplayer.ui.base.ads;

import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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
    AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        .addTestDevice("7CCFDE0BCA5D3295A5406D318F0BBCC1") //LS-5005
        .build();
    adView.loadAd(adRequest);
    adView.setAdListener(new AdListener() {
      @Override public void onAdFailedToLoad(int i) {
        adView.setVisibility(View.GONE);
      }
    });
  }
}
