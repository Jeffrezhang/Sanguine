package sanguine.gui.view;

import java.awt.Component;

/**
 * Interface representing a single card widget in the GUI hand.
 * It is responsible for drawing the cards info
 */
public interface CardWidget {

  /**
   * Gets and returns the index of the card within the players hand.
   *
   * @return the index of the card within the players hand
   */
  int getIndex();

  /**
   * Returns this card widget as a Swing component so the hand panel can display it.
   *
   * @return this card widget as a Swing component.
   */
  Component asComponent();
}