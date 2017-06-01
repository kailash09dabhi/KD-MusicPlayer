/*
 * Copyright (c) 2016. Kailash Dabhi (Kingbull Technology)
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.kingbull.musicplayer.domain;

import java.util.concurrent.TimeUnit;

public final class Milliseconds {
  private final long milliSeconds;

  public Milliseconds(long milliSeconds) {
    this.milliSeconds = milliSeconds;
  }

  public String toString() {
    if (milliSeconds >= TimeUnit.HOURS.toMillis(1)) {
      return toHhMmSs();
    } else {
      return toMmSs();
    }
  }

  public String toHhMmSs() {
    long hours = TimeUnit.MILLISECONDS.toHours(milliSeconds);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(hours);
    long seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds)
        - TimeUnit.HOURS.toSeconds(hours)
        - TimeUnit.MINUTES.toSeconds(minutes);
    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
  }

  public String toMmSs() {
    long hours = TimeUnit.MILLISECONDS.toHours(milliSeconds);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(hours);
    long seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds)
        - TimeUnit.HOURS.toSeconds(hours)
        - TimeUnit.MINUTES.toSeconds(minutes);
    return String.format("%02d:%02d", minutes, seconds);
  }

  public long asLong() {
    return milliSeconds;
  }
}
