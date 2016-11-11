package com.kingbull.musicplayer.ui.base;

/**
 * Created by Kailash Dabhi on 09-07-2016.
 * You can contact us at kailash09dabhi@gmail.com OR on skype(kailash.09)
 * Copyright (c) 2016 Kingbull Technology. All rights reserved.
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
