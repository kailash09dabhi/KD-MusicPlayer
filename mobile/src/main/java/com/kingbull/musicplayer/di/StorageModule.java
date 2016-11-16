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
import com.kingbull.musicplayer.MusicPlayerApp;
import com.kingbull.musicplayer.domain.storage.MusicSqliteOpenHelper;
import com.kingbull.musicplayer.domain.storage.SongTable;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module  public final class StorageModule {
  //@Singleton @Provides SharedPreferences provideDefaultSharedPreferences(TimestampApplication app) {
  //  return PreferenceManager.getDefaultSharedPreferences(app);
  //}
  //
  //@Singleton @Provides SettingPreferences provideSettingsPreference(TimestampApplication app) {
  //  return new SettingPreferences(app);
  //}

  @Provides @Singleton SQLiteOpenHelper provideOpenHelper(MusicPlayerApp app) {
    return new MusicSqliteOpenHelper(app);
  }

  @Provides @Singleton SQLiteDatabase provideSqliteDatabase(SQLiteOpenHelper openHelper) {
    return openHelper.getWritableDatabase();
  }

  @Provides @Singleton SongTable provideTimestampTable(SQLiteDatabase sqLiteDatabase) {
    return new SongTable(sqLiteDatabase);
  }
  //@Provides @Singleton File provideStorageDirectory(TimestampApplication app) {
  //  File storageDir =
  //      new File(Environment.getExternalStorageDirectory(), app.getString(R.string.app_name));
  //  storageDir.mkdirs();
  //  return storageDir;
  //}
}
