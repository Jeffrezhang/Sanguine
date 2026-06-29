package sanguine.gui.controller;

import sanguine.model.Card;
import sanguine.model.ReadOnlySanguineModel;

/**
 * Basic interface for us to implement AI behavior.
 */
public interface AiStrategy {

  /**
   * This is the basic blueprint for the AI strategy.
   *
   * @param model takes in the model to see game behavior.
   * @param proxy takes in the proxy to avoid changing the original model.
   * @param player takes in what the player/turn.
   * @return a valid ai move.
   */
  AiMove chooseMove(ReadOnlySanguineModel<Card> model,
                    ProxyModel<Card> proxy,
                    int player);
}
