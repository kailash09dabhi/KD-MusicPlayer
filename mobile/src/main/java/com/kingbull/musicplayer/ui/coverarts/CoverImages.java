package com.kingbull.musicplayer.ui.coverarts;

import io.reactivex.Observable;
import java.util.List;

/**
 * @author Kailash Dabhi
 * @date 12/25/2016.
 */

public interface CoverImages {
  Observable<List<String>> albums(String albumName);

  Observable<List<String>> artists(String artistName);
}
