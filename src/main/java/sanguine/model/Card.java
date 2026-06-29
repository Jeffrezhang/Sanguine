package sanguine.model;

/**
 * an interface representing a card.
 */
public interface Card {

  /**
   * creates and returns text representation of the card.
   *
   * @return card as text
   */
  String toString();

  /**
   * Gets and returns the matrix representing the AOE of the card.
   *
   * @return the AOE of the card
   */
  int[][] getAoe();

  /**
   * Gets and returns the value of the card.
   *
   * @return the value of the card
   */
  int getValue();

  /**
   * Gets and returns the cost of the card.
   *
   * @return the cost of the card
   */
  int getCost();

  /**
   * Gets and returns the name of the card.
   *
   * @return the name of the card
   */
  String getName();
}
