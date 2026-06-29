package sanguine.model;

import java.util.List;

/**
 * This class allows us to have a readonly file that can not be mutated.
 */
public class ReadOnlyBasicSanguine implements ReadOnlySanguineModel<Card> {

  private SanguineModel<Card> model;

  /**
   * Constructor that bases the readonly on the real model.
   *
   * @param model the real model that it takes in.
   */
  public ReadOnlyBasicSanguine(SanguineModel<Card> model) {
    this.model = model;
  }

  @Override
  public boolean isGameOver() {
    return model.isGameOver();
  }

  @Override
  public int getRowScore(int row, int player) {
    return model.getRowScore(row, player);
  }

  @Override
  public int getPlayerScore(int player) {
    return model.getPlayerScore(player);
  }

  @Override
  public boolean isPlayerAi(int player) {
    return model.isPlayerAi(player);
  }

  @Override
  public PlayerType getPlayerType(int player) {
    return model.getPlayerType(player);
  }

  @Override
  public int getNumRows() {
    return model.getNumRows();
  }

  @Override
  public int getNumCols() {
    return model.getNumCols();
  }

  @Override
  public char getCell(int row, int col) {
    return model.getCell(row, col);
  }

  @Override
  public Pawn getPawnAt(int row, int col) {
    return model.getPawnAt(row, col);
  }

  @Override
  public List<Card> getHand(int player) {
    return model.getHand(player);
  }
}
