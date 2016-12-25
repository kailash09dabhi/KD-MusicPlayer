package com.kingbull.musicplayer.ui.coverarts;

import com.kingbull.musicplayer.ui.base.Presenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 12/25/2016.
 */

public final class CoverArtsPresenter extends Presenter<CoverArts.View>
    implements CoverArts.Presenter {
  CoverImages coverImages = new LastFmCovers();

  @Override public void onAlbumSearch(String album) {
    view().showProgress();
    coverImages.albums(album)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<List<String>>() {
          @Override public void onNext(List<String> albumImages) {
            if (albumImages.isEmpty()) {
              view().showNoResultFoundMessage();
            } else {
              view().showCoverArts(albumImages);
            }
          }

          @Override public void onError(Throwable e) {
          }

          @Override public void onComplete() {
            view().dismissProgress();
          }
        });
  }

  @Override public void onArtistSearch(final String artist) {
    view().showProgress();
    coverImages.artists(artist)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<List<String>>() {
          @Override public void onNext(List<String> artistImages) {
            if (artistImages.isEmpty()) {
              view().showNoResultFoundMessage();
            } else {
              view().showCoverArts(artistImages);
            }
          }

          @Override public void onError(Throwable e) {
          }

          @Override public void onComplete() {
            view().dismissProgress();
          }
        });
  }

  @Override public void onCoverSelection() {
    // TODO: 12/25/2016 save the albumart in Mediastore and refresh
  }
}
