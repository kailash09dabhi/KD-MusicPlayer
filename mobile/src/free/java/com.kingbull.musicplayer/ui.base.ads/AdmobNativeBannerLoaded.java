package com.kingbull.musicplayer.ui.base.ads;

import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.kingbull.musicplayer.R;

/**
 * class expects that the Activity should have adView.
 *
 * @author Kailash Dabhi
 * @date 10/3/2016.
 */
public final class AdmobNativeBannerLoaded {
  private final NativeExpressAdView adView;

  public AdmobNativeBannerLoaded(ViewGroup rootView) {
    adView = (NativeExpressAdView) rootView.findViewById(R.id.adView);
    //if (BuildConfig.DEBUG) return;
    adView.setVisibility(View.VISIBLE);
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
