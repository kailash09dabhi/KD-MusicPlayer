package com.kingbull.musicplayer.domain.storage;

/**
 * @author Kailash Dabhi
 * @date 11/17/2016.
 */

public interface SqlTableRow {
  /**
   * either update or insert the  {@code SqlTableRow} in {@code SqlTable}
   *
   * @return affected row id
   */
  long save();

  /**
   * delete the {@code Persistable} from {@code DataSource}
   */
  boolean delete();
}
