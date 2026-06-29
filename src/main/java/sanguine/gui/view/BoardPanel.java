package sanguine.gui.view;

import java.awt.Component;

/**
 * Interface representing the board panel in the Sanguine GUI.
 * Responsible for drawing the board as a Swing component.
 *
 */
public interface BoardPanel {

  /**
   * Clears the highlight of the highlighted cell.
   */
  void clearHighlight();

  /**
   * Highlights the cell at the given row and column.
   *
   * @param row row of the cell
   * @param col column of the cell
   */
  void setHighlight(int row, int col);
  /**
   * Returns this board panel as a Swing component.
   *
   * @return this board as a Swing component
   */

  Component asComponent();
}