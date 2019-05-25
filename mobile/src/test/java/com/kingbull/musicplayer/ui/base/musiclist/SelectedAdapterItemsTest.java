package com.kingbull.musicplayer.ui.base.musiclist;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import androidx.recyclerview.widget.RecyclerView;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Created by KD on 7/26/2017.
 */
public class SelectedAdapterItemsTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  private SelectedAdapterItems selectedAdapterItems = new SelectedAdapterItems(
      Mockito.mock(RecyclerView.Adapter.class));

  @Before public void setUp() throws Exception {
    selectedAdapterItems.addOnSelectionListener(OnSelectionListener.NONE);
  }

  @Test
  public void toggleSelection() throws Exception {
    selectedAdapterItems.toggleSelection(7);
    assertEquals(true, selectedAdapterItems.has(7));
    selectedAdapterItems.toggleSelection(7);
    assertEquals(false, selectedAdapterItems.has(7));
  }

  @Test
  public void getSelectedItemCount() throws Exception {
    selectedAdapterItems.toggleSelection(6);
    selectedAdapterItems.toggleSelection(9);
    assertEquals(2, selectedAdapterItems.count());
  }

  @Test
  public void removeAll() throws Exception {
    selectedAdapterItems.toggleSelection(1);
    selectedAdapterItems.toggleSelection(1);
    selectedAdapterItems.toggleSelection(2);
    selectedAdapterItems.toggleSelection(10);
    selectedAdapterItems.toggleSelection(100);
    selectedAdapterItems.removeAll();
    assertEquals(0, selectedAdapterItems.count());
  }

  @Test
  public void getSelectedItems() throws Exception {
    selectedAdapterItems.toggleSelection(9);
    selectedAdapterItems.toggleSelection(16);
    selectedAdapterItems.toggleSelection(7);
    selectedAdapterItems.toggleSelection(97);
    assertArrayEquals(new int[]{7, 9, 16, 97}, selectedAdapterItems.selectedIndexes());

  }

  @Test
  public void getSelectedMusics() throws Exception {
  }

}