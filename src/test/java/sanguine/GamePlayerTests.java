package sanguine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import org.junit.Test;
import sanguine.gui.controller.AiMove;
import sanguine.gui.controller.AiStrategy;
import sanguine.gui.controller.ProxyModel;
import sanguine.gui.player.AiGamePlayer;
import sanguine.gui.player.GamePlayer;
import sanguine.gui.player.HumanGamePlayer;
import sanguine.gui.player.PlayerActionListener;
import sanguine.model.Card;
import sanguine.model.Pawn;
import sanguine.model.PlayerType;
import sanguine.model.ReadOnlySanguineModel;

/**
 * Tests for AiGamePlayer and HumanGamePlayer.
 */
public class GamePlayerTests {

  /**
   * readonly model only used to satisfy interfaces.
   */
  private static final class ReadOnlyModel implements ReadOnlySanguineModel<Card> {

    @Override
    public boolean isGameOver() {
      return false;
    }

    @Override
    public int getRowScore(int row, int player) {
      return 0;
    }

    @Override
    public int getPlayerScore(int player) {
      return 0;
    }

    @Override
    public boolean isPlayerAi(int player) {
      return false;
    }

    @Override
    public int getNumRows() {
      return 3;
    }

    @Override
    public int getNumCols() {
      return 5;
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
      return Collections.emptyList();
    }

    @Override
    public PlayerType getPlayerType(int player) {
      return PlayerType.Human;
    }
  }

  /**
   * dummy proxy model that is never actually called by ai game player.
   */
  private static final class DummyProxyModel implements ProxyModel<Card> {

    @Override
    public boolean isLegalPlacement(int player, int cardIndex, int row, int col) {
      return true;
    }

    @Override
    public void placeCard(int player, int cardIndex, int row, int col) {
      //do nothing
    }

    @Override
    public void drawCard(int player) {
      //nothing
    }

    @Override
    public void pass(int player) {
      //nothing
    }
  }

  /**
   * strategy that records its arguments and returns a move.
   */
  private static final class RecordingStrategy implements AiStrategy {
    ReadOnlySanguineModel<Card> seenModel;
    ProxyModel<Card> seenProxy;
    int seenPlayer;
    AiMove moveToReturn;

    RecordingStrategy(AiMove moveToReturn) {
      this.moveToReturn = moveToReturn;
    }

    @Override
    public AiMove chooseMove(ReadOnlySanguineModel<Card> model,
                             ProxyModel<Card> proxy,
                             int player) {
      this.seenModel = model;
      this.seenProxy = proxy;
      this.seenPlayer = player;
      return moveToReturn;
    }
  }

  /**
   * Listener that records whether it was called.
   */
  private static final class RecordingListener implements PlayerActionListener {
    boolean passCalled = false;
    boolean playCalled = false;
    int lastCardIndex = -1;
    int lastRow = -1;
    int lastCol = -1;

    @Override
    public void onPlayCard(int cardIndex, int row, int col) {
      playCalled = true;
      lastCardIndex = cardIndex;
      lastRow = row;
      lastCol = col;
    }

    @Override
    public void onPass() {
      passCalled = true;
    }
  }

  @Test
  public void aiGamePlayerGetIdReturnsConstructorId() {
    GamePlayer p = new AiGamePlayer(7,
        new RecordingStrategy(AiMove.pass()),
        new ReadOnlyModel(),
        new DummyProxyModel());
    assertEquals(7, p.getId());
  }

  @Test
  public void aiGamePlayerPassMoveNotifiesListenerOnPass() {
    RecordingStrategy strat = new RecordingStrategy(AiMove.pass());
    ReadOnlyModel model = new ReadOnlyModel();
    DummyProxyModel proxy = new DummyProxyModel();
    AiGamePlayer p = new AiGamePlayer(1, strat, model, proxy);
    RecordingListener listener = new RecordingListener();
    p.setActionListener(listener);

    p.onYourTurn();

    assertEquals(model, strat.seenModel);
    assertEquals(proxy, strat.seenProxy);
    assertEquals(1, strat.seenPlayer);

    // Listener was notified about a pass
    assertTrue(listener.passCalled);
    assertFalse(listener.playCalled);
  }

  @Test
  public void aiGamePlayerPlaceMoveNotifiesListenerOnPlayCard() {
    AiMove move = AiMove.place(3, 2, 4);
    RecordingStrategy strat = new RecordingStrategy(move);
    ReadOnlyModel model = new ReadOnlyModel();
    DummyProxyModel proxy = new DummyProxyModel();
    AiGamePlayer p = new AiGamePlayer(2, strat, model, proxy);
    RecordingListener listener = new RecordingListener();
    p.setActionListener(listener);

    p.onYourTurn();

    assertFalse(listener.passCalled);
    assertTrue(listener.playCalled);
    assertEquals(3, listener.lastCardIndex);
    assertEquals(2, listener.lastRow);
    assertEquals(4, listener.lastCol);
  }

  @Test
  public void aiGamePlayerOnYourTurnWithNoListenerDoesNotThrow() {
    AiMove move = AiMove.place(0, 0, 0);
    RecordingStrategy strat = new RecordingStrategy(move);
    AiGamePlayer p =
        new AiGamePlayer(1, strat, new ReadOnlyModel(), new DummyProxyModel());
    p.onYourTurn();

    assertEquals(1, strat.seenPlayer);
  }

  @Test
  public void humanGamePlayerGetIdReturnsConstructorId() {
    GamePlayer p = new HumanGamePlayer(5);
    assertEquals(5, p.getId());
  }

  @Test
  public void humanGamePlayerOnYourTurnDoesNotCallListener() {
    HumanGamePlayer p = new HumanGamePlayer(3);
    RecordingListener listener = new RecordingListener();
    p.setActionListener(listener);

    p.onYourTurn();

    assertFalse(listener.passCalled);
    assertFalse(listener.playCalled);
  }
}
