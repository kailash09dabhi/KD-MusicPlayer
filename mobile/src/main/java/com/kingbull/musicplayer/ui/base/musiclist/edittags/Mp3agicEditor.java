package com.kingbull.musicplayer.ui.base.musiclist.edittags;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import java.io.IOException;

/**
 * @author Kailash Dabhi
 * @date 12/10/2016.
 */

final class Mp3agicEditor implements MusicTags.Editor {
  private final String title;
  private final String artist;
  private final String album;
  private final String genre;
  private final String year;
  private final Mp3File mp3File;

  Mp3agicEditor(Builder mp3agicBuilder) {
    this.title = mp3agicBuilder.title;
    this.artist = mp3agicBuilder.artist;
    this.album = mp3agicBuilder.album;
    this.genre = mp3agicBuilder.genre;
    this.year = mp3agicBuilder.year;
    mp3File = mp3agicBuilder.mp3File;
  }

  @Override public void save() throws IOException, NotSupportedException {
    ID3v1 id3v1Tag;
    if (mp3File.hasId3v1Tag()) {
      id3v1Tag = mp3File.getId3v1Tag();
    } else {
      id3v1Tag = new ID3v1Tag();
      mp3File.setId3v1Tag(id3v1Tag);
    }
    id3v1Tag.setTitle(title);
    id3v1Tag.setArtist(artist);
    id3v1Tag.setAlbum(album);
    id3v1Tag.setGenre(12);
    id3v1Tag.setYear(year);
    ID3v2 id3v2Tag;
    if (mp3File.hasId3v2Tag()) {
      id3v2Tag = mp3File.getId3v2Tag();
    } else {
      id3v2Tag = new ID3v24Tag();
      mp3File.setId3v2Tag(id3v2Tag);
    }
    id3v2Tag.setTitle(title);
    id3v2Tag.setArtist(artist);
    id3v2Tag.setAlbum(album);
    id3v2Tag.setGenre(12);
    id3v2Tag.setYear(year);
    mp3File.save(title + ".mp3");
  }

  public static class Builder implements MusicTags.Editor.Builder {

    private final Mp3File mp3File;
    private String title; // optional
    private String artist; // required
    private String album; // optional
    private String genre; // required
    private String year; // optional

    public Builder(Mp3File mp3File) {
      this.mp3File = mp3File;
    }

    @Override public MusicTags.Editor.Builder title(String title) {
      this.title = title;
      return this;
    }

    @Override public MusicTags.Editor.Builder artist(String artist) {
      this.artist = artist;
      return this;
    }

    @Override public MusicTags.Editor.Builder album(String album) {
      this.album = album;
      return this;
    }

    @Override public MusicTags.Editor.Builder genre(String genre) {
      this.genre = genre;
      return this;
    }

    @Override public MusicTags.Editor.Builder year(String year) {
      this.year = year;
      return this;
    }

    @Override public MusicTags.Editor build() {
      return new Mp3agicEditor(this);
    }
  }
}
