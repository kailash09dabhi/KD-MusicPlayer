package com.kingbull.musicplayer.ui.equalizer.preset;

import com.kingbull.musicplayer.ui.base.Presenter;

/**
 * @author Kailash Dabhi
 * @date 11/27/2016.
 */

public final class PresetPresenter extends Presenter<Preset.View>
    implements Preset.Presenter {

  @Override public void onCreateNewClick() {
    view().showCreatePresetScreen();

  }
}
