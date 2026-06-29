package sanguine.gui.controller;

import sanguine.model.Card;
import sanguine.model.ReadOnlySanguineModel;

/**
 * This is the class that takes the greedy strategy of fill first.
 */
public class FillFirstAi implements AiStrategy {

  @Override
  public AiMove chooseMove(ReadOnlySanguineModel<Card> model,
                           ProxyModel<Card> proxy,
                           int player) {

    int handSize = model.getHand(player).size();
    int rows = model.getNumRows();
    int cols = model.getNumCols();

    for (int cardIndex = 0; cardIndex < handSize; cardIndex++) {
      for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
          if (proxy.isLegalPlacement(player, cardIndex, r, c)) {
            return AiMove.place(cardIndex, r, c);
          }
        }
      }
    }

    return AiMove.pass();
  }
}
