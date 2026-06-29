package sanguine.gui.view;

import java.awt.Component;

/**
 * Interface representing the score panel of the Sanguine GUI view.
 * It draws row scores for each player of each row.
 */
public interface ScorePanel {

  /**
   * Refreshes the score display to the current state of the model for the current player.
   */
  void refresh();

  /**
   * Returns this score panel as a Swing component.
   *
   * @return this score panel as a Swing component
   */
  Component asComponent();
}