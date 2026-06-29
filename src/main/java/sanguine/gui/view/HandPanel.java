package sanguine.gui.view;

import java.awt.Component;
import sanguine.gui.controller.GuiClickListener;

/**
 * Interface representing the hand panel in the Sanguine GUI.
 * It is responsible for displaying the current player's hand.
 **/
public interface HandPanel {

  /**
   * Refreshes the hand display to the current state of the model for the current player.
   */
  void refresh();

  /**
   * Sets the click listener used to notify when a card is clicked.
   *
   * @param listener the GUI click listener
   */
  void setCardClickListener(GuiClickListener listener);

  /**
   * Updates which card index should be drawn as selected.
   *
   * @param index the selected card index, or -1 if none
   */
  void setSelectedCardIndex(int index);

  /**
   * Returns this hand panel as a Swing component.
   *
   * @return this hand panel as a Swing component
   */
  Component asComponent();
}