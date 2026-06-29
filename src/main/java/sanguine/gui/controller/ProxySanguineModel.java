package sanguine.gui.controller;

import sanguine.model.Card;
import sanguine.model.ReadOnlySanguineModel;

/**
 * Proxy model that sends all commands to a communicator.
 * It has the same public API as the real model,
 * but does not directly modify any game state.
 */
public class ProxySanguineModel implements ProxyModel<Card> {

  private final ReadOnlySanguineModel readOnlyModel;
  private final SanguineCommunicator communicator;

  /**
   * Constructor for the proxy model takes in the read only model and the communicator.
   *
   * @param model the read only model
   * @param comm the communicator
   */
  public ProxySanguineModel(ReadOnlySanguineModel model, SanguineCommunicator comm) {
    this.readOnlyModel = model;
    this.communicator = comm;
  }

  @Override
  public boolean isLegalPlacement(int player, int cardIndex, int row, int col) {
    Card card = (Card) readOnlyModel.getHand(player).get(cardIndex);
    return communicator.checkLegalPlacement(player, card, row, col);
  }

  @Override
  public void placeCard(int player, int cardIndex, int row, int col) {
    Card card = (Card) readOnlyModel.getHand(player).get(cardIndex);
    communicator.send("placeCard", player, card.getName(), row, col);
  }

  @Override
  public void drawCard(int player) {
    communicator.send("drawCard", player);
  }

  @Override
  public void pass(int player) {
    communicator.send("pass", player);
  }
}