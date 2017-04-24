/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kingbull.musicplayer.di;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.storage.StorageDirectory;
import com.kingbull.musicplayer.domain.storage.preferences.SettingPreferences;
import com.kingbull.musicplayer.domain.storage.sqlite.MusicSqliteOpenHelper;
import com.kingbull.musicplayer.domain.storage.sqlite.table.AlbumTable;
import com.kingbull.musicplayer.domain.storage.sqlite.table.MediaStatTable;
import com.kingbull.musicplayer.domain.storage.sqlite.table.MediaTable;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

@Module public final class StorageModule {
  public static final String COVER_ART_DIR = "Cover Art";

  @Provides @Singleton SQLiteOpenHelper provideOpenHelper(MusicPlayerApp app) {
    return new MusicSqliteOpenHelper(app);
  }

  @Provides @Singleton SQLiteDatabase provideSqliteDatabase(SQLiteOpenHelper openHelper) {
    return openHelper.getWritableDatabase();
  }

  @Provides @Singleton MediaStatTable provideMediaStaTable(SQLiteDatabase sqLiteDatabase) {
    return new MediaStatTable(sqLiteDatabase);
  }

  @Provides @Singleton MediaTable provideMediaTable() {
    return new MediaTable();
  }

  @Provides @Singleton AlbumTable provideAlbumTable() {
    return new AlbumTable();
  }

  @Named(COVER_ART_DIR) @Provides @Singleton StorageDirectory provideStorageDirectory() {
    return new StorageDirectory(COVER_ART_DIR);
  }

  @Singleton @Provides SettingPreferences providesSettingPreferences(MusicPlayerApp application) {
    return new SettingPreferences(PreferenceManager.getDefaultSharedPreferences(application));
  }
}
