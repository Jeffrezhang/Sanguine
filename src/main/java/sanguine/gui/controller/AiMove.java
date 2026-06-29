package sanguine.gui.controller;

/**
 * This class is for the AI player movement.
 */
public class AiMove {
  private final boolean pass;
  private final int cardIndex;
  private final int row;
  private final int col;

  /**
   * THis is the constructor for the AI to make a move.
   *
   * @param pass if the AI passes their turn.
   * @param cardIndex the Card that the AI wants to place.
   * @param row what row the AI wants to place the card.
   * @param col what column the AI wants to place the card.
   */
  private AiMove(boolean pass, int cardIndex, int row, int col) {
    this.pass = pass;
    this.cardIndex = cardIndex;
    this.row = row;
    this.col = col;
  }

  /**
   * the method is used to implement passing/giving up their turn.
   *
   * @return updates the move as a pass.
   */
  public static AiMove pass() {
    return new AiMove(true, -1, -1, -1);
  }

  /**
   * Goes through columns and rows to place their selected card.
   *
   * @param cardIndex the card that the AI wants to use.
   * @param row the row where the AI wants to place the card.
   * @param col the column where the AI wants to place.
   * @return updates the game so that it shows the AI made a move that places a card.
   */
  public static AiMove place(int cardIndex, int row, int col) {
    return new AiMove(false, cardIndex, row, col);
  }

  public boolean isPass() {
    return pass;
  }

  public int getCardIndex() {
    return cardIndex;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }
}
