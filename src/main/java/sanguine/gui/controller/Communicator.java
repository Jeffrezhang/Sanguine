package sanguine.gui.controller;

import sanguine.model.Card;

/**
 * The communication layer between the GUI-side proxy model and the
 * proxy controller connected to the real model.
 */
public interface Communicator {

  /**
   * Asks the proxy controller whether a card placement would be legal.
   *
   * @param player the active player (1 or 2)
   * @param card   the card to hypothetically place
   * @param row    the row to test
   * @param col    the column to test
   * @return {@code true} if the move is legal; {@code false} otherwise
   */
  boolean checkLegalPlacement(int player, Card card, int row, int col);

  /**
   * Sends a command to the proxy controller for execution.
   *
   * @param methodName the name of the method to invoke remotely
   * @param args       the parameters for that invocation
   */
  void send(String methodName, Object... args);
}
