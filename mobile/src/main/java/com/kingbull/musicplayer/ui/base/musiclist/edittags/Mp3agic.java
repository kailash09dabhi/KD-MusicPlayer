package com.kingbull.musicplayer.ui.base.musiclist.edittags;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;

/**
 * @author Kailash Dabhi
 * @date 12/10/2016.
 */

public final class Mp3agic implements MusicTags {
  private final Mp3File mp3File;

  public Mp3agic(File file) throws InvalidDataException, IOException, UnsupportedTagException {
    this.mp3File = new Mp3File(file);
  }

  @Override public String artist() {
    String artist = "";
    if (mp3File.hasId3v1Tag()) {
      ID3v1 id3v1Tag = mp3File.getId3v1Tag();
      artist = id3v1Tag.getArtist();
    } else if (mp3File.hasId3v2Tag()) {
      ID3v2 id3v2Tag;
      id3v2Tag = mp3File.getId3v2Tag();
      artist = id3v2Tag.getArtist();
    }
    return artist;
  }

  @Override public String genre() {
    String genre = "";
    if (mp3File.hasId3v1Tag()) {
      ID3v1 id3v1Tag = mp3File.getId3v1Tag();
      genre = id3v1Tag.getGenreDescription();
    } else if (mp3File.hasId3v2Tag()) {
      ID3v2 id3v2Tag;
      id3v2Tag = mp3File.getId3v2Tag();
      genre = id3v2Tag.getGenreDescription();
    }
    return genre;
  }

  @Override public String year() {
    String year = "";
    if (mp3File.hasId3v1Tag()) {
      ID3v1 id3v1Tag = mp3File.getId3v1Tag();
      year = id3v1Tag.getYear();
    } else if (mp3File.hasId3v2Tag()) {
      ID3v2 id3v2Tag;
      id3v2Tag = mp3File.getId3v2Tag();
      year = id3v2Tag.getYear();
    }
    return year;
  }

  @Override public String title() {
    String title = "";
    if (mp3File.hasId3v1Tag()) {
      ID3v1 id3v1Tag = mp3File.getId3v1Tag();
      title = id3v1Tag.getTitle();
    } else if (mp3File.hasId3v2Tag()) {
      ID3v2 id3v2Tag;
      id3v2Tag = mp3File.getId3v2Tag();
      title = id3v2Tag.getTitle();
    }
    return title;
  }

  @Override public String album() {
    String album = "";
    if (mp3File.hasId3v1Tag()) {
      ID3v1 id3v1Tag = mp3File.getId3v1Tag();
      album = id3v1Tag.getAlbum();
    } else if (mp3File.hasId3v2Tag()) {
      ID3v2 id3v2Tag;
      id3v2Tag = mp3File.getId3v2Tag();
      album = id3v2Tag.getAlbum();
    }
    return album;
  }

  @Override public Editor.Builder edit() {
    return new Mp3agicEditor.Builder(mp3File);
  }
}
