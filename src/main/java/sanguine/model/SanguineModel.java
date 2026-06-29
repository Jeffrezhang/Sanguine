package sanguine.model;

import java.util.List;

/**
 * interface to represent a model representing the game of Sanguine.
 *
 * @param <C> ensures that all parameters set as C extend the Card interface
 */
public interface SanguineModel<C extends Card> extends ReadOnlySanguineModel<C> {

  /**
   * Creates a new deck that is then returned.
   *
   * @return a deck that is represented by a list
   */
  List<C> createNewDeck();

  /**
   * Starts the game of Sanguine.
   *
   * @param p1deck the deck that the player 1 is using
   * @param p2deck the deck that the player 2 is using
   * @param p1Type the player type of p1
   * @param p2Type the player type of p2
   * @param startingSize the starting size of both hands of the players
   */
  void startGame(List<Card> p1deck, List<Card> p2deck,
                 PlayerType p1Type, PlayerType p2Type, int startingSize);

  /**
   * Places the given card down on the board.
   *
   * @param row the desired row to place the card
   * @param col the desired column to place the card
   * @param playerTurn the current player that is placing the card
   * @param card the card that is to be placed
   */
  void placeCard(int row, int col, int playerTurn, Card card);

  /**
   * Draws a card for the player specified.
   *
   * @param playerTurn the current player to draw the card for
   */
  void drawCard(int playerTurn);

  /**
   * represents a pass in turn (player turn doesn't matter since the controller handles that).
   */
  void pass();
}
