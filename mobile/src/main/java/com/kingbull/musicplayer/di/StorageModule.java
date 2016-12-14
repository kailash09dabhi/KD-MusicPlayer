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
import com.kingbull.musicplayer.domain.storage.sqlite.table.MediaStatTable;
import com.kingbull.musicplayer.domain.storage.sqlite.table.MediaTable;
import com.kingbull.musicplayer.domain.storage.sqlite.MusicSqliteOpenHelper;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public final class StorageModule {

  @Provides @Singleton SQLiteOpenHelper provideOpenHelper(MusicPlayerApp app) {
    return new MusicSqliteOpenHelper(app);
  }

  @Provides @Singleton SQLiteDatabase provideSqliteDatabase(SQLiteOpenHelper openHelper) {
    return openHelper.getWritableDatabase();
  }

  @Provides @Singleton MediaStatTable provideMediaStaTable(SQLiteDatabase sqLiteDatabase) {
    return new MediaStatTable(sqLiteDatabase);
  }

  @Provides @Singleton MediaTable provideMediaTable(SQLiteDatabase sqLiteDatabase) {
    return new MediaTable();
  }
}
