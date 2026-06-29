package sanguine.gui.controller;

import sanguine.model.Card;

/**
 * Interface for the proxy controller that receives high-level method calls
 * from the communicator and forwards them to the real Sanguine model.
 */
public interface ProxyController {

  /**
   * Executes a command sent from the communicator.
   *
   * @param method the name of the method to dispatch
   * @param args the method arguments
   * @throws IllegalArgumentException if the method name is unknown
   */
  void execute(String method, Object... args);

  /**
   * Performs a legality check for card placement without modifying
   * the true model state.
   *
   * @param player the player attempting to place a card
   * @param card the card to check
   * @param row the targeted row
   * @param col the targeted column
   * @return true if the move is legal, or false otherwise
   */
  boolean checkLegalPlacement(int player, Card card, int row, int col);
}
