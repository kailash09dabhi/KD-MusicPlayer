package com.kingbull.musicplayer.ui.main.categories.folder;

import android.support.v4.util.LruCache;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 12/14/2016.
 */

public interface Folder {
  /** includes all subfolders recursively to give all music list */
  List<File> allMusics();

  /** only the music file which are in same folder */
  List<File> musics();

  List<Folder> musicFolders();

  List<File> musicFoldersAsFiles();

  class Smart implements Folder {
    private static final LruCache<File, Folder> cache = new LruCache<File, Folder>(45) {
      @Override protected Folder create(File key) {
        return new Folder.Smart(key);
      }
    };

    private static final AudioFileFilter audioFileFilter = new AudioFileFilter();
    private final File file;

    private Smart(File file) {
      this.file = file;
    }

    public static Folder from(File file) {
      return cache.get(file);
    }

    @Override public List<File> allMusics() {
      List<File> allMusics = new ArrayList<>();
      allMusics.addAll(musics());
      List<Folder> directories = musicFolders();
      for (Folder folder : directories) {
        allMusics.addAll(folder.allMusics());
      }
      return allMusics;
    }

    @Override public List<File> musics() {
      List<File> onlyMusics = new ArrayList<>();
      List<File> files = Arrays.asList(file.listFiles(audioFileFilter));
      for (File file : files) {
        if (!file.isDirectory()) {
          onlyMusics.add(file);
        }
      }
      return onlyMusics;
    }

    @Override public List<Folder> musicFolders() {
      List<Folder> onlyDirectories = new ArrayList<>();
      List<File> files = Arrays.asList(file.listFiles(audioFileFilter));
      for (File file : files) {
        if (file.isDirectory()) onlyDirectories.add(Folder.Smart.from(file));
      }
      return onlyDirectories;
    }

    @Override public List<File> musicFoldersAsFiles() {
      List<File> onlyDirectories = new ArrayList<>();
      List<File> files = Arrays.asList(file.listFiles(audioFileFilter));
      for (File file : files) {
        if (file.isDirectory()) onlyDirectories.add(file);
      }
      return onlyDirectories;
    }
  }

  class Cached implements Folder {
    private static final LruCache<Folder, List<File>> allMusicsCache =
        new LruCache<Folder, List<File>>(45) {
          @Override protected List<File> create(Folder key) {
            return Collections.unmodifiableList(key.allMusics());
          }
        };
    private static final LruCache<Folder, List<File>> musicsCache =
        new LruCache<Folder, List<File>>(45) {
          @Override protected List<File> create(Folder key) {
            return Collections.unmodifiableList(key.musics());
          }
        };
    private static final LruCache<Folder, List<Folder>> musicFoldersCache =
        new LruCache<Folder, List<Folder>>(45) {
          @Override protected List<Folder> create(Folder key) {
            return Collections.unmodifiableList(key.musicFolders());
          }
        };
    private static final LruCache<Folder, List<File>> musicFoldersAsFilesCache =
        new LruCache<Folder, List<File>>(45) {
          @Override protected List<File> create(Folder key) {
            return Collections.unmodifiableList(key.musicFoldersAsFiles());
          }
        };

    private final Folder folder;

    public Cached(Folder folder) {
      this.folder = folder;
    }

    @Override public List<File> allMusics() {
      return allMusicsCache.get(folder);
    }

    @Override public List<File> musics() {
      return musicsCache.get(folder);
    }

    @Override public List<Folder> musicFolders() {
      return musicFoldersCache.get(folder);
    }

    @Override public List<File> musicFoldersAsFiles() {
      return musicFoldersAsFilesCache.get(folder);
    }
  }
}
