package sanguine.gui.controller;

import sanguine.model.Card;

/**
 * Sends method calls + parameters to the textual controller.
 */
public class SanguineCommunicator implements Communicator {

  private final SanguineProxyController target;

  /**
   * Constructor for the communicator communicates between the Proxy Model and the Proxy Controller.
   *
   * @param target the proxy controller
   */
  public SanguineCommunicator(SanguineProxyController target) {
    this.target = target;
  }

  @Override
  public boolean checkLegalPlacement(int player, Card card, int row, int col) {
    return target.checkLegalPlacement(player, card, row, col);
  }

  @Override
  public void send(String methodName, Object... args) {
    target.execute(methodName, args);
  }
}