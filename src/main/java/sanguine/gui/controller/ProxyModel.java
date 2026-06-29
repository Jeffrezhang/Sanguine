package sanguine.gui.controller;

import sanguine.model.Card;

/**
 * A proxy-facing model interface used by the GUI controller.
 * This model does not mutate game state directly.
 */
public interface ProxyModel<C extends Card> {

  /**
   * Checks whether placing the card at the given index from the player's hand
   * onto the specified board cell is a legal move.
   *
   * @param player the active player (1 or 2)
   * @param cardIndex the index of the card in the player's hand
   * @param row the target row
   * @param col the target column
   * @return true if placement is legal; false otherwise
   */
  boolean isLegalPlacement(int player, int cardIndex, int row, int col);

  /**
   * Issues a remote request to place the specified card onto the board.
   *
   * @param player     the active player (1 or 2)
   * @param cardIndex  the index of the card to place
   * @param row        the target board row
   * @param col        the target board column
   */
  void placeCard(int player, int cardIndex, int row, int col);

  /**
   * Draw a card for the respective player.
   *
   * @param player the player who is drawing a card
   */
  void drawCard(int player);

  /**
   * passes turn for the respective player.
   *
   * @param player the player who is passing their turn
   */
  void pass(int player);
}
