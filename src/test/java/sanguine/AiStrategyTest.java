package sanguine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import sanguine.gui.controller.AiMove;
import sanguine.gui.controller.AiStrategy;
import sanguine.gui.controller.FillFirstAi;
import sanguine.gui.controller.ProxyModel;
import sanguine.gui.controller.RowScoreAi;
import sanguine.model.Card;
import sanguine.model.Pawn;
import sanguine.model.PlayerType;
import sanguine.model.ReadOnlySanguineModel;


/**
 * Tests for AI strategies.
 */
public class AiStrategyTest {

  private static class TestCard implements Card {
    private final String name;
    private final int cost;
    private final int value;

    TestCard(String name, int cost, int value) {
      this.name = name;
      this.cost = cost;
      this.value = value;
    }

    @Override
    public String toString() {
      return this.name;
    }

    @Override
    public int[][] getAoe() {
      return new int[5][5];
    }

    @Override
    public int getValue() {
      return this.value;
    }

    @Override
    public int getCost() {
      return this.cost;
    }

    @Override
    public String getName() {
      return this.name;
    }
  }

  private static class MockReadOnlyModel implements ReadOnlySanguineModel<Card> {
    private final int numRows = 2;
    private final int numCols = 3;
    private final int[][] rowScores = new int[this.numRows][2];
    private final Map<Integer, List<Card>> hands = new HashMap<>();
    private final boolean[] ai = new boolean[3];
    private final PlayerType[] types = new PlayerType[3];

    MockReadOnlyModel() {
      this.hands.put(1, new ArrayList<Card>());
      this.hands.put(2, new ArrayList<Card>());
      this.ai[1] = false;
      this.ai[2] = false;
      this.types[1] = PlayerType.Human;
      this.types[2] = PlayerType.Human;
    }

    void setHand(int player, List<Card> cards) {
      this.hands.put(player, new ArrayList<>(cards));
    }

    void setRowScore(int row, int player, int score) {
      this.rowScores[row][player - 1] = score;
    }

    @Override
    public boolean isGameOver() {
      return false;
    }

    @Override
    public int getRowScore(int row, int player) {
      return this.rowScores[row][player - 1];
    }

    @Override
    public int getPlayerScore(int player) {
      int sum = 0;
      for (int r = 0; r < this.numRows; r++) {
        sum += this.rowScores[r][player - 1];
      }
      return sum;
    }

    @Override
    public boolean isPlayerAi(int player) {
      return this.ai[player];
    }

    @Override
    public int getNumRows() {
      return this.numRows;
    }

    @Override
    public int getNumCols() {
      return this.numCols;
    }

    @Override
    public char getCell(int row, int col) {
      return '_';
    }

    @Override
    public Pawn getPawnAt(int row, int col) {
      return null;
    }

    @Override
    public List<Card> getHand(int player) {
      return Collections.unmodifiableList(this.hands.get(player));
    }

    @Override
    public PlayerType getPlayerType(int player) {
      return this.types[player];
    }
  }

  private static class MockProxyModel implements ProxyModel<Card> {

    private static class Key {
      private final int player;
      private final int cardIndex;
      private final int row;
      private final int col;

      Key(int player, int cardIndex, int row, int col) {
        this.player = player;
        this.cardIndex = cardIndex;
        this.row = row;
        this.col = col;
      }

      @Override
      public boolean equals(Object other) {
        if (!(other instanceof Key)) {
          return false;
        }
        Key that = (Key) other;
        return this.player == that.player
            && this.cardIndex == that.cardIndex
            && this.row == that.row
            && this.col == that.col;
      }

      @Override
      public int hashCode() {
        return java.util.Objects.hash(
            this.player, this.cardIndex, this.row, this.col);
      }
    }

    final Map<Key, Boolean> legalMap = new HashMap<>();
    final List<String> legalCalls = new ArrayList<>();
    final List<String> placeCalls = new ArrayList<>();
    final List<Integer> passCalls = new ArrayList<>();

    void setLegal(int player, int cardIndex, int row, int col, boolean legal) {
      this.legalMap.put(new Key(player, cardIndex, row, col), legal);
    }

    @Override
    public boolean isLegalPlacement(int player, int cardIndex, int row, int col) {
      this.legalCalls.add(
          "p=" + player + ",c=" + cardIndex + ",r=" + row + ",col=" + col);
      Boolean result = this.legalMap.get(new Key(player, cardIndex, row, col));
      return result != null && result;
    }

    @Override
    public void placeCard(int player, int cardIndex, int row, int col) {
      this.placeCalls.add(
          "p=" + player + ",c=" + cardIndex + ",r=" + row + ",col=" + col);
    }

    @Override
    public void drawCard(int player) {
      //not needed
    }

    @Override
    public void pass(int player) {
      this.passCalls.add(player);
    }
  }

  @Test
  public void fillFirstAiChoosesFirstLegalMove() {
    MockReadOnlyModel model = new MockReadOnlyModel();
    final MockProxyModel proxy = new MockProxyModel();
    final AiStrategy strat = new FillFirstAi();

    model.setHand(
        1,
        java.util.Arrays.asList(
            new TestCard("C0", 1, 1),
            new TestCard("C1", 1, 1)));

    proxy.setLegal(1, 1, 1, 0, true);

    AiMove move = strat.chooseMove(model, proxy, 1);

    assertFalse(move.isPass());
    assertEquals(1, move.getCardIndex());
    assertEquals(1, move.getRow());
    assertEquals(0, move.getCol());
    assertTrue(!proxy.legalCalls.isEmpty());
  }

  @Test
  public void rowScoreAiChoosesMoveThatWinsRow() {
    MockReadOnlyModel model = new MockReadOnlyModel();
    final MockProxyModel proxy = new MockProxyModel();
    final AiStrategy strat = new RowScoreAi();

    model.setRowScore(0, 1, 0);
    model.setRowScore(0, 2, 10);
    model.setRowScore(1, 1, 3);
    model.setRowScore(1, 2, 6);

    model.setHand(
        1,
        java.util.Arrays.asList(
            new TestCard("Low", 1, 3),
            new TestCard("High", 1, 4)));

    proxy.setLegal(1, 1, 1, 2, true);

    AiMove move = strat.chooseMove(model, proxy, 1);

    assertFalse(move.isPass());
    assertEquals(1, move.getCardIndex());
    assertEquals(1, move.getRow());
    assertEquals(2, move.getCol());
  }

  @Test
  public void rowScoreAiPassesWhenNoWinningMove() {
    MockReadOnlyModel model = new MockReadOnlyModel();
    final MockProxyModel proxy = new MockProxyModel();
    final AiStrategy strat = new RowScoreAi();

    model.setRowScore(0, 1, 5);
    model.setRowScore(0, 2, 10);
    model.setRowScore(1, 1, 5);
    model.setRowScore(1, 2, 10);

    model.setHand(
        1,
        java.util.Arrays.asList(
            new TestCard("Small", 1, 1),
            new TestCard("Medium", 1, 2)));

    AiMove move = strat.chooseMove(model, proxy, 1);

    assertTrue(move.isPass());
  }
}
