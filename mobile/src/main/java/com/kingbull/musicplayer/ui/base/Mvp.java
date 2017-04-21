package com.kingbull.musicplayer.ui.base;

/**
 * @author Kailash Dabhi
 * @date 9th July, 2016 12:37 AM
 * @From DirectConnect
 */
public interface Mvp {
  interface View {
    //void close();
  }

  interface Model {
  }

  interface Presenter<V> {
    void takeView(V view);

    boolean hasView();

    V view();
  }
}
