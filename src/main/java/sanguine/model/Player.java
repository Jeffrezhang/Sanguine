package sanguine.model;

import java.util.List;
import java.util.Queue;

/**
 * Interface to represent a player.
 */
public interface Player {

  /**
   * Gets and returns the players id.
   *
   * @return the player id.
   */
  int getId();

  /**
   * Gets and returns the players deck.
   *
   * @return the player deck
   */
  Queue<Card> getDeck();

  /**
   * Gets and returns the players hand.
   *
   * @return the player hand
   */
  List<Card> getHand();

  /**
   * Draws card from deck to hand.
   */
  void drawCard();

  /**
   * Removes the card from hand. Simulates placing a card.
   *
   * @param c the card to be removed
   */
  void removeCard(Card c);

  /**
   * Gets and returns if the player is AI or Human.
   *
   * @return if the player is AI
   */
  boolean isAi();

  /**
   * Returns this player's type (Human, AI, RowScoreAI, etc.).
   *
   * @return the player type
   */
  PlayerType getType();

}
