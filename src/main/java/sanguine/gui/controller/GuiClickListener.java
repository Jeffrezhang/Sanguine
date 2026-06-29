package sanguine.gui.controller;

/**
 * Listener interface used by the GUI view to notify the controller of all user mouse actions.
 */
public interface GuiClickListener {

  /**
   * Triggered when a card in the hand is clicked.
   *
   * @param cardIndex index of the clicked card within the current player's hand
   */
  void onCardClicked(int cardIndex);

  /**
   * Triggered when the board is clicked.
   *
   * @param row the row on the game board the user clicked
   * @param col the column on the game board the user clicked
   */
  void onBoardClicked(int row, int col);

  /**
   * Triggered when pass button is clicked.
   */
  void onPassClicked();
}
