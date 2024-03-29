package com.kingbull.musicplayer.ui.main.categories.all;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.R;
import com.kingbull.musicplayer.RxBus;
import com.kingbull.musicplayer.domain.Music;
import com.kingbull.musicplayer.event.DurationFilterEvent;
import com.kingbull.musicplayer.event.PaletteEvent;
import com.kingbull.musicplayer.event.SortEvent;
import com.kingbull.musicplayer.event.ThemeEvent;
import com.kingbull.musicplayer.event.TransparencyChangedEvent;
import com.kingbull.musicplayer.ui.addtoplaylist.AddToPlayListDialogFragment;
import com.kingbull.musicplayer.ui.base.BaseFragment;
import com.kingbull.musicplayer.ui.base.Keyboard;
import com.kingbull.musicplayer.ui.base.PresenterFactory;
import com.kingbull.musicplayer.ui.base.animators.Alpha;
import com.kingbull.musicplayer.ui.base.animators.SlideHorizontal;
import com.kingbull.musicplayer.ui.base.drawable.IconDrawable;
import com.kingbull.musicplayer.ui.base.musiclist.MusicRecyclerViewAdapter;
import com.kingbull.musicplayer.ui.base.musiclist.OnSelectionListener;
import com.kingbull.musicplayer.ui.base.view.SelectionOptionsLayout;
import com.kingbull.musicplayer.ui.base.view.Snackbar;
import com.kingbull.musicplayer.ui.music.MusicPlayerActivity;
import com.kingbull.musicplayer.ui.sorted.SortDialogFragment;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents All Songs screen.
 *
 * @author Kailash Dabhi
 * @date 8th Nov, 2016
 */
public final class AllSongsFragment extends BaseFragment<AllSongs.Presenter>
    implements LoaderManager.LoaderCallbacks<Cursor>, AllSongs.View {
  private final List<Music> musicList = new ArrayList<>();
  private final SlideHorizontal.Animation slideHorizontalAnimation =
      new SlideHorizontal.Animation(400);
  private final Alpha.Animation alphaAnimation = new Alpha.Animation(400);
  private final Keyboard keyboard = new Keyboard();
  @BindView(R.id.allSongsLayout) LinearLayout allSongsLayout;
  @BindView(R.id.progressLayout) LinearLayout progressLayout;
  @BindView(R.id.deletedOutOfText) TextView deletedOutOfTextView;
  @BindView(R.id.donutProgress) DonutProgress donutProgress;
  @BindView(R.id.totalSongCountView) TextView totalSongCountView;
  @BindView(R.id.recyclerView) FastScrollRecyclerView recyclerView;
  @BindView(R.id.totalSongLayout) LinearLayout totalSongLayout;
  @BindView(R.id.sortButton) ImageView sortButton;
  @BindView(R.id.searchButton) ImageView searchButton;
  @BindView(R.id.exitSearchButton) ImageView exitSearchButton;
  @BindView(R.id.searchLayout) LinearLayout searchLayout;
  @BindView(R.id.selectionContextOptionsLayout) SelectionOptionsLayout selectionOptionsLayout;
  @BindView(R.id.searchView) EditText searchView;
  private MusicRecyclerViewAdapter musicRecyclerViewAdapter;

  @OnTextChanged(R.id.searchView) void onSearchTextChanged(CharSequence text) {
    presenter.onSearchTextChanged(text.toString());
  }

  @OnClick(R.id.exitSearchButton) void onExitSearchClick() {
    presenter.onExitSearchClick();
  }

  @OnClick(R.id.searchButton) void onSearchClick() {
    presenter.onSearchClick();
  }

  @OnClick(R.id.sortButton) void onSortClick() {
    presenter.onSortMenuClick();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MusicPlayerApp.instance().component().inject(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_all_songs, container, false);
  }

  @Override protected Disposable subscribeEvents() {
    return RxBus.getInstance()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(o -> {
          if (presenter != null && presenter.hasView()) {
            if (o instanceof SortEvent) {
              presenter.onSortEvent((SortEvent) o);
            } else if (o instanceof DurationFilterEvent) {
              getLoaderManager().restartLoader(0, null, AllSongsFragment.this);
            } else if (o instanceof PaletteEvent || o instanceof ThemeEvent || o instanceof
                TransparencyChangedEvent) {
              updateDrawableOfButtons(
                  smartTheme.header().dark(0.01f).transparent(0.16f).intValue());
              applyUiColors();
            }
          } else {
            FirebaseCrashlytics.getInstance().recordException(
                new NullPointerException(
                    String.format(
                        "class: %s presenter- %s hasView- %b",
                        AllSongsFragment.class.getSimpleName(),
                        presenter, presenter != null && presenter.hasView()
                    )
                )
            );
          }

        });
  }

  @Override protected PresenterFactory<AllSongs.Presenter> presenterFactory() {
    return new PresenterFactory.AllSongs();
  }

  @Override protected void onPresenterPrepared(AllSongs.Presenter presenter) {
    ButterKnife.bind(this, getView());
    setupView();
    presenter.takeView(this);
  }

  private void setupView() {
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    applyUiColors();
    getLoaderManager().initLoader(0, null, this);
    musicRecyclerViewAdapter =
        new MusicRecyclerViewAdapter(musicList, (AppCompatActivity) getActivity());
    recyclerView.setAdapter(musicRecyclerViewAdapter);
    musicRecyclerViewAdapter.addOnSelectionListener(
        new OnSelectionListener() {
          @Override public void onClearSelection() {
            hideSelectionContextOptions();
          }

          @Override public void onMultiSelection(int selectionCount) {
            if (selectionCount == 1) {
              alphaAnimation.fadeOut(searchLayout);
              alphaAnimation.fadeOut(totalSongLayout);
              alphaAnimation.fadeIn(selectionOptionsLayout);
            }
          }
        });
    selectionOptionsLayout.addOnContextOptionClickListener(
        new SelectionOptionsLayout.OnContextOptionClickListener() {
          @Override public void onAddToPlaylistClick() {
            presenter.onAddToPlayListMenuClick();
          }

          @Override public void onDeleteSelectedClick() {
            presenter.onDeleteSelectedMusic();
          }

          @Override public void onClearSelectionClick() {
            musicRecyclerViewAdapter.clearSelection();
            hideSelectionContextOptions();
          }
        });
    updateDrawableOfButtons(Color.BLACK);
    searchLayout.post(() -> {
      //searchLayout.setX(searchLayout.getWidth());
      searchLayout.setTranslationX(totalSongLayout.getWidth());
    });
  }

  private void updateDrawableOfButtons(int fillColor) {
    sortButton.setImageDrawable(new IconDrawable(R.drawable.ic_sort_48dp, fillColor));
    searchButton.setImageDrawable(new IconDrawable(R.drawable.ic_search_48dp, fillColor));
    exitSearchButton.setImageDrawable(new IconDrawable(R.drawable.ic_back_48dp, fillColor));
    selectionOptionsLayout.updateIconsColor(fillColor);
  }

  private void applyUiColors() {
    int headerColor = smartTheme.header().intValue();
    recyclerView.setPopupBgColor(Color.WHITE);
    recyclerView.setThumbColor(Color.WHITE);
    recyclerView.setTrackColor(headerColor);
    recyclerView.setPopupTextColor(headerColor);
    ((View) totalSongLayout.getParent()).setBackgroundColor(headerColor);
    int screenColor = smartTheme.screen().intValue();
    recyclerView.setBackgroundColor(screenColor);
    searchView.setHintTextColor(smartTheme.bodyText().intValue());
    donutProgress.setFinishedStrokeColor(Color.WHITE);
    donutProgress.setUnfinishedStrokeColor(flatTheme.header().intValue());
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new AllSongsCursorLoader(getContext(), settingPreferences);
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    presenter.onAllSongsCursorLoadFinished(cursor);
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {
  }

  @Override public void showAllSongs(List<Music> songs) {
    totalSongCountView.setText(String.valueOf(songs.size()));
    musicList.clear();
    musicList.addAll(songs);
    musicRecyclerViewAdapter.notifyDataSetChanged();
  }

  @Override public void showMusicScreen() {
    startActivity(new Intent(getActivity(), MusicPlayerActivity.class));
  }

  @Override public void showAddToPlayListDialog() {
    AddToPlayListDialogFragment.newInstance(musicRecyclerViewAdapter.getSelectedMusics())
        .show(getActivity().getSupportFragmentManager(),
            AddToPlayListDialogFragment.class.getName());
    musicRecyclerViewAdapter.clearSelection();
  }

  @Override public void showSortMusicScreen() {
    SortDialogFragment.newInstance()
        .show(getActivity().getSupportFragmentManager(), SortDialogFragment.class.getName());
  }

  @Override public List<Music> selectedMusicList() {
    return musicRecyclerViewAdapter.getSelectedMusics();
  }

  @Override public void notifyItemRemoved(int position) {
    musicRecyclerViewAdapter.notifyItemRemoved(position);
    musicList.remove(position);
  }

  @Override public void clearSelection() {
    musicRecyclerViewAdapter.clearSelection();
  }

  @Override public void hideSelectionContextOptions() {
    alphaAnimation.fadeOut(selectionOptionsLayout);
    alphaAnimation.fadeIn(searchLayout);
    alphaAnimation.fadeIn(totalSongLayout);
  }

  @Override public void showMessage(String message) {
    new Snackbar(recyclerView).show(message);
  }

  @Override public void refreshSongCount(int size) {
    totalSongCountView.setText(String.valueOf(size));
  }

  @Override public void showProgressLayout() {
    alphaAnimation.fadeOut(allSongsLayout);
    alphaAnimation.fadeIn(progressLayout);
  }

  @Override public void showAllSongsLayout() {
    alphaAnimation.fadeOut(progressLayout);
    alphaAnimation.fadeIn(allSongsLayout);
  }

  @Override public void percentage(final int percentage) {
    System.out.println(percentage + "%" + Thread.currentThread().getName());
    donutProgress.setDonut_progress(String.valueOf(percentage));
  }

  @Override public void deletedOutOfText(String deleteOutOfText) {
    deletedOutOfTextView.setText(deleteOutOfText);
  }

  @Override public void exitSearch() {
    searchView.setText("");
    alphaAnimation.fadeOut(searchLayout, () -> keyboard.hide(getActivity()));
    alphaAnimation.fadeIn(totalSongLayout);
    slideHorizontalAnimation.slide(searchLayout, searchLayout.getWidth());
    slideHorizontalAnimation.slide(totalSongLayout, 0);
  }

  @Override public void enterSearch() {
    alphaAnimation.fadeOut(totalSongLayout);
    alphaAnimation.fadeIn(searchLayout, () -> keyboard.show(searchView));
    slideHorizontalAnimation.slide(totalSongLayout, -totalSongLayout.getWidth());
    slideHorizontalAnimation.slide(searchLayout, 0);
  }
}
