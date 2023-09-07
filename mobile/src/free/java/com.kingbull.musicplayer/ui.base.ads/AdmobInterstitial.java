package com.kingbull.musicplayer.ui.base.ads;

import android.app.Activity;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.kingbull.musicplayer.ui.base.AdListener;

/**
 * @author Kailash Dabhi
 * @date 12/9/2016.
 */
public final class AdmobInterstitial {
  private InterstitialAd interstitialAd;
  private final Activity activity;

  public AdmobInterstitial(Activity activity, String interstitialUnitId,
      AdListener adListener) {
    this.activity = activity;
    InterstitialAd.load(activity, interstitialUnitId, adRequest(),
        new InterstitialAdLoadCallback() {
          @Override public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
            AdmobInterstitial.this.interstitialAd = interstitialAd;
            AdmobInterstitial.this.interstitialAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                  @Override public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    adListener.onAdClosed();
                  }

                  @Override
                  public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    adListener.onAdClosed();
                  }
                });
          }
        });
  }

  @NonNull private AdRequest adRequest() {
    return new AdRequest.Builder().build();
  }

  public boolean isLoaded() {
    return interstitialAd != null;
  }

  public void show() {
    if (AdmobInterstitial.this.interstitialAd != null) {
      AdmobInterstitial.this.interstitialAd.setFullScreenContentCallback(
          new FullScreenContentCallback() {
          });
      AdmobInterstitial.this.interstitialAd.show(activity);
    }
  }
}