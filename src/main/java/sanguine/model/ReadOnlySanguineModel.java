package sanguine.model;

import java.util.List;

/**
 * Interface for the immutable readonly model.
 *
 * @param <C> A generic for the card.
 */
public interface ReadOnlySanguineModel<C extends Card> {

  /**
   * Checks if the game is over (when both players have passed).
   *
   * @return if the game is over
   */
  boolean isGameOver();

  /**
   * Gets the score of the specified row.
   *
   * @param row the row that is specified
   * @param player the player to know which score to send
   * @return the score of the players specified row
   */
  int getRowScore(int row, int player);

  /**
   * Gets the score of the player and returns it.
   *
   * @param player the player whose score we are getting
   * @return the score of the player
   */
  int getPlayerScore(int player);

  /**
   * Checks and confirms whether the player is AI or not.
   *
   * @param player the player we are checking
   * @return true if the player is AI, false if not
   */
  boolean isPlayerAi(int player);

  /**
   * Gets the PlayerType (Human, AI, RowScoreAI) for the given player.
   *
   * @param player the player we are checking
   * @return the player type
   */
  PlayerType getPlayerType(int player);

  /**
   * Gets the number of rows of the board in play.
   *
   * @return the number of rows
   */
  int getNumRows();

  /**
   * Gets the number of columns of the board in play.
   *
   * @return the number of columns
   */
  int getNumCols();

  /**
   * Gets the char value of the wanted cell on the board whether it's a pawn a card or nothing.
   *
   * @param row the row of the cell
   * @param col the column of the cell
   * @return the value of the cell represented through a char
   */
  char getCell(int row, int col);

  /**
   * Returns pawn information at the given cell, or null if no pawns are there.
   *
   * @param row the row of the cell
   * @param col the column of the cell
   * @return a Pawn describing owner + count, or null if empty
   */
  Pawn getPawnAt(int row, int col);

  /**
   * Gets and returns the hand of the given player.
   *
   * @param player the player that is given
   * @return the hand of the player that is given
   */
  List<Card> getHand(int player);


}
