//package com.kingbull.musicplayer.ui.base.musiclist.quickaction;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.widget.RecyclerView;
//import com.kingbull.musicplayer.R;
//import com.kingbull.musicplayer.domain.Music;
//import com.kingbull.musicplayer.domain.PlayList;
//import com.kingbull.musicplayer.domain.storage.sqlite.FavouritesPlayList;
//import com.kingbull.musicplayer.player.Player;
//import com.kingbull.musicplayer.ui.base.drawable.IconDrawable;
//import com.kingbull.musicplayer.ui.base.musiclist.edittags.EditTagsDialogFragment;
//import com.kingbull.musicplayer.ui.main.categories.playlists.members.MoveToDialogFragment;
//import com.kingbull.musicplayer.ui.music.MusicPlayerActivity;
//import java.io.File;
//import java.util.List;
//
///**
// * @author Kailash Dabhi
// * @date 24 April, 2017 9:01 PM
// */
//public interface QuickAction {
//  void execute();
//
//  Drawable icon(int fillColor);
//
//  class Play implements QuickAction {
//    private final Activity activity;
//    private final Player player;
//    private final List<Music> songs;
//    private final Music music;
//
//    public Play(Activity activity, Player player, List<Music> songs, Music music) {
//      this.activity = activity;
//      this.player = player;
//      this.songs = songs;
//      this.music = music;
//    }
//
//    @Override public void execute() {
//      player.addToNowPlaylist(songs);
//      player.play(music);
//      activity.startActivity(new Intent(activity, MusicPlayerActivity.class));
//    }
//
//    @Override public Drawable icon(int fillColor) {
//      return new IconDrawable(R.drawable.ic_play_48dp, fillColor);
//    }
//  }
//
//  class MovePlaylist implements QuickAction {
//    private final FragmentManager fragmentManager;
//    private final PlayList playList;
//    private final Music music;
//    private final int position;
//
//    public MovePlaylist(FragmentManager fragmentManager, PlayList playList, Music music,
//        int position) {
//      this.fragmentManager = fragmentManager;
//      this.playList = playList;
//      this.music = music;
//      this.position = position;
//    }
//
//    @Override public void execute() {
//      if (playList instanceof PlayList.Smart) {
//        MoveToDialogFragment.newInstance(playList, music, position)
//            .show(fragmentManager, MoveToDialogFragment.class.getName());
//      }
//    }
//
//    @Override public Drawable icon(int fillColor) {
//      return new IconDrawable(R.drawable.ic_playlist_add_48dp, fillColor);
//    }
//  }
//
//  class EditTags implements QuickAction {
//    private final FragmentManager fragmentManager;
//    private final Music music;
//
//    public EditTags(FragmentManager fragmentManager, Music music) {
//      this.fragmentManager = fragmentManager;
//      this.music = music;
//    }
//
//    @Override public void execute() {
//      EditTagsDialogFragment.newInstance(music)
//          .show(fragmentManager, EditTagsDialogFragment.class.getName());
//    }
//
//    @Override public Drawable icon(int fillColor) {
//      return new IconDrawable(R.drawable.ic_edit_48dp, fillColor);
//    }
//  }
//
//  class Ringtone implements QuickAction {
//    private final Activity activity;
//    private final Music music;
//
//    public Ringtone(Activity activity, Music music) {
//      this.activity = activity;
//      this.music = music;
//    }
//
//    @Override public void execute() {
//      new com.kingbull.musicplayer.ui.base.musiclist.ringtone.Ringtone(activity, music).set();
//    }
//
//    @Override public Drawable icon(int fillColor) {
//      return new IconDrawable(R.drawable.ic_ringtone_48dp, fillColor);
//    }
//  }
//
//  class Delete implements QuickAction {
//    private final PlayList playList;
//    private final List<Music> songs;
//    private final RecyclerView.Adapter adapter;
//    private final int adapterPosition;
//
//    public Delete(PlayList playList, List<Music> songs, RecyclerView.Adapter adapter,
//        int adapterPosition) {
//      this.playList = playList;
//      this.songs = songs;
//      this.adapter = adapter;
//      this.adapterPosition = adapterPosition;
//    }
//
//    @Override public void execute() {
//      if (playList instanceof PlayList.Smart) {
//        ((PlayList.Smart) playList).remove(songs.get(adapterPosition));
//        songs.remove(adapterPosition);
//        adapter.notifyItemRemoved(adapterPosition);
//      } else if (playList instanceof FavouritesPlayList) {
//        songs.get(adapterPosition).mediaStat().toggleFavourite();
//        songs.remove(adapterPosition);
//        adapter.notifyItemRemoved(adapterPosition);
//      }
//    }
//
//    @Override public Drawable icon(int fillColor) {
//      return new IconDrawable(R.drawable.ic_delete_48dp, fillColor);
//    }
//  }
//
//  class Send implements QuickAction {
//    private final Activity activity;
//    private final File file;
//
//    public Send(Activity activity, File file) {
//      this.activity = activity;
//      this.file = file;
//    }
//
//    @Override public void execute() {
//      Intent share = new Intent(Intent.ACTION_SEND);
//      share.setType("audio/*");
//      share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//      activity.startActivity(Intent.createChooser(share, "Share Sound File"));
//    }
//
//    @Override public Drawable icon(int fillColor) {
//      return new IconDrawable(R.drawable.ic_send_48dp, fillColor);
//    }
//  }
//}
