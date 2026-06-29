package sanguine.gui.controller;

import sanguine.model.Card;
import sanguine.model.ReadOnlySanguineModel;

/**
 * The class for the maximum row scoring AI.
 */
public class RowScoreAi implements AiStrategy {

  @Override
  public AiMove chooseMove(ReadOnlySanguineModel<Card> model,
                           ProxyModel<Card> proxy,
                           int player) {

    int rows = model.getNumRows();
    int cols = model.getNumCols();
    int handSize = model.getHand(player).size();
    int opp = (player == 1) ? 2 : 1;

    for (int r = 0; r < rows; r++) {
      int myScore = model.getRowScore(r, player);
      int oppScore = model.getRowScore(r, opp);

      if (myScore > oppScore) {
        continue;
      }

      for (int cardIndex = 0; cardIndex < handSize; cardIndex++) {
        Card card = model.getHand(player).get(cardIndex);

        if (myScore + card.getValue() <= oppScore) {
          continue;
        }

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
