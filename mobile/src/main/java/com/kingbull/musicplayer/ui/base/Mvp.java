package com.kingbull.musicplayer.ui.base;

/**
 * @author Kailash Dabhi
 * @date 9th July, 2016 12:37 AM
 * @From DirectConnect
 */
public interface Mvp {
  interface View {
  }

  interface Model {
  }

  interface Presenter<V> {
    Presenter NONE = new Presenter() {
      @Override public void takeView(Object view) {

      }

      @Override public boolean hasView() {
        return false;
      }

      @Override public Object view() {
        return null;
      }
    };

    void takeView(V view);

    boolean hasView();

    V view();
  }
}
