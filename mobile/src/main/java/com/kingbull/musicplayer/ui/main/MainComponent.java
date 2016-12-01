package com.kingbull.musicplayer.ui.main;

import com.kingbull.musicplayer.di.ActivityModule;
import com.kingbull.musicplayer.di.AppComponent;
import com.kingbull.musicplayer.di.PerActivity;
import com.kingbull.musicplayer.ui.main.categories.folder.MyFilesFragment;
import dagger.Component;

/**
 * @author Kailash Dabhi
 * @date 12/1/2016.
 */

@PerActivity @Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface MainComponent {

  void inject(MyFilesFragment fragment);
}
