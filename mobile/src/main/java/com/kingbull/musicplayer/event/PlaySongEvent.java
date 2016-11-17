package com.kingbull.musicplayer.event;

import com.kingbull.musicplayer.domain.Music;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 9/5/16
 * Time: 6:32 PM
 * Desc: PlaySongEvent
 */
public final class PlaySongEvent {

    public Music song;

    public PlaySongEvent(Music song) {
        this.song = song;
    }
}
