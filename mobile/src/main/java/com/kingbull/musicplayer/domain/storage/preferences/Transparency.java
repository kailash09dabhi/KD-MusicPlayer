package com.kingbull.musicplayer.domain.storage.preferences;

import android.content.SharedPreferences;

/**
 * Represents Transparency.
 *
 * @author Kailash Dabhi
 * @date 7/28/2017 6:45 PM
 */
public interface Transparency {
  void apply(float transparency);

  float value();

//  abstract class AbstractTransparency implements Transparency {
//    private final String key;
//    private final float defaultValue;
//    private final SharedPreferences sharedPreferences;
//
//    AbstractTransparency(SharedPreferences sharedPreferences, String key, float defaultValue) {
//      this.sharedPreferences = sharedPreferences;
//      this.key = key;
//      this.defaultValue = defaultValue;
//    }
//
//    @Override public void apply(float transparency) {
//      sharedPreferences.edit().putFloat(key, transparency).apply();
//    }
//
//    @Override public float value() {
//      return sharedPreferences.getFloat(key, defaultValue);
//    }
//  }

//  class Header extends AbstractTransparency {
//    public Header(SharedPreferences sharedPreferences) {
//      super(sharedPreferences, "header", 0.52f);
//    }
//  }
//
//  class Body extends AbstractTransparency {
//    public Body(SharedPreferences sharedPreferences) {
//      super(sharedPreferences, "body", 0.7f);
//    }
//  }

  class Wrap implements Transparency {
    private final Transparency transparency;

    public Wrap(Transparency transparency) {
      this.transparency = transparency;
    }

    @Override public void apply(float transparency) {
      if (transparency >= 1.0) {
        transparency = 0.99f;
      }
      this.transparency.apply(transparency);
    }

    @Override public float value() {
      return this.transparency.value();
    }
  }

  class WithSharedPrefs extends Wrap {
    WithSharedPrefs(final SharedPreferences sharedPreferences,
        final String key, final float defaultValue) {
      super(new Transparency() {
        @Override public void apply(float transparency) {
          sharedPreferences.edit().putFloat(key, transparency).apply();
        }

        @Override public float value() {
          return sharedPreferences.getFloat(key, defaultValue);
        }
      });
    }
  }

  class Header extends Wrap {
    public Header(SharedPreferences sharedPreferences) {
      super(
          new WithSharedPrefs(
              sharedPreferences, "header", 0.52f
          )
      );
    }
  }

  class Body extends Wrap {
    public Body(SharedPreferences sharedPreferences) {
      super(
          new WithSharedPrefs(
              sharedPreferences, "body", 0.7f
          )
      );
    }
  }
}
