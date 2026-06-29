package sanguine.gui.controller;

import sanguine.model.Card;
import sanguine.model.Pawn;
import sanguine.model.SanguineModel;

/**
 * Thin controller that receives commands from the communicator and forwards
 * them directly to the real model.
 */
public class SanguineProxyController implements ProxyController {

  private final SanguineModel<Card> realModel;

  /**
   * Constructor for the Proxy Model.
   *
   * @param realModel takes in the real model so that we can use the proxy instead.
   */
  public SanguineProxyController(SanguineModel<Card> realModel) {
    this.realModel = realModel;
  }

  @Override
  public void execute(String method, Object... args) {

    switch (method) {
      case "placeCard":
        {
        int player   = (int) args[0];
        String name  = (String) args[1];
        int row      = (int) args[2];
        int col      = (int) args[3];

        Card card = findCardInHand(player, name);
        realModel.placeCard(row, col, player, card);
        return;
        }
      case "pass":
        {
        realModel.pass();
        return;
        }
      case "drawCard":
        {
        int player = (int) args[0];
        realModel.drawCard(player);
        return;
        }
      default:
        throw new IllegalArgumentException("Unknown method: " + method);
    }
  }

  @Override
  public boolean checkLegalPlacement(int player, Card card, int row, int col) {

    try {

      dryRunPlaceCard(row, col, player, card);
      return true;

    } catch (Exception e) {
      return false;
    }
  }

  /**
   * A dry run of placeCard that checks legality without sending it to the model.
   */
  private void dryRunPlaceCard(int row, int col, int player, Card card) {
    if (row < 0 || row >= realModel.getNumRows()
        || col < 0 || col >= realModel.getNumCols()) {
      throw new IllegalArgumentException("Out of bounds");
    }

    char cellChar = realModel.getCell(row, col);
    if (cellChar == 'R' || cellChar == 'B') {
      throw new IllegalStateException("Cell already has a card");
    }

    int cost = card.getCost();
    Pawn pawn = realModel.getPawnAt(row, col);

    if (pawn == null || pawn.getOwner() != player || pawn.getCount() < cost) {
      throw new IllegalStateException("Not enough pawns or enemy pawns present");
    }
  }

  /**
   * Finds a card by name in the player's hand.
   */
  private Card findCardInHand(int player, String name) {
    for (Card c : realModel.getHand(player)) {
      if (c.getName().equalsIgnoreCase(name)) {
        return c;
      }
    }
    throw new IllegalArgumentException("Card not found in hand: " + name);
  }
}
