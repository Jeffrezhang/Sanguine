package sanguine.gui.player;

import sanguine.gui.controller.AiMove;
import sanguine.gui.controller.AiStrategy;
import sanguine.gui.controller.ProxyModel;
import sanguine.model.Card;
import sanguine.model.ReadOnlySanguineModel;

/**
 * Implementation of the Game Player interface to represent an AI player.
 */
public class AiGamePlayer implements GamePlayer {

  private final int id;
  private final AiStrategy strategy;
  private final ReadOnlySanguineModel<Card> model;
  private final ProxyModel<Card> proxy;
  private PlayerActionListener listener;

  /**
   * Constructs the AI game player.
   *
   * @param id the id of the player.
   * @param strategy the AI strategy
   * @param model the read only model.
   * @param proxy the proxy model.
   */
  public AiGamePlayer(int id,
                      AiStrategy strategy,
                      ReadOnlySanguineModel<Card> model,
                      ProxyModel<Card> proxy) {
    this.id = id;
    this.strategy = strategy;
    this.model = model;
    this.proxy = proxy;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setActionListener(PlayerActionListener listener) {
    this.listener = listener;
  }

  @Override
  public void onYourTurn() {
    AiMove move = strategy.chooseMove(model, proxy, id);
    if (listener == null) {
      return;
    }
    if (move.isPass()) {
      listener.onPass();
    } else {
      listener.onPlayCard(move.getCardIndex(), move.getRow(), move.getCol());
    }
  }
}
