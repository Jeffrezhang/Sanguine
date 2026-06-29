package sanguine.gui.player;

/**
 * Interface to represent the listener for all inputs from player.
 */
public interface PlayerActionListener {

  /**
   * When the player decides to place a card.
   *
   * @param cardIndex the card index within the players hand
   * @param row the row on the board that the player wants to place the card
   * @param col the column on the board that the player wants to place the card
   */
  void onPlayCard(int cardIndex, int row, int col);

  /**
   * When the player decides to pass their turn.
   */
  void onPass();
}
