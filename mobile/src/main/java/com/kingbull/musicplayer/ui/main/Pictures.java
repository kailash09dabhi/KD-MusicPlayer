package com.kingbull.musicplayer.ui.main;

import com.kingbull.musicplayer.R;
import java.util.Random;

/**
 * @author Kailash Dabhi
 * @date 25 April, 2017 3:11 PM
 */
public final class Pictures {
  private final int pictures[] = {
      R.drawable.k1, R.drawable.k2, R.drawable.k3, R.drawable.k4, R.drawable.k5, R.drawable.k6,
      R.drawable.k7, R.drawable.k8, R.drawable.k9, R.drawable.k10, R.drawable.k11, R.drawable.k12,
      R.drawable.k13, R.drawable.k14, R.drawable.k15, R.drawable.k16, R.drawable.k17,
      R.drawable.k18, R.drawable.k19,
  };
  private final Random random = new Random();

  public int[] toDrawablesId() {
    return pictures.clone();
  }

  public int random() {
    return pictures[random.nextInt(pictures.length - 1)];
  }
}
