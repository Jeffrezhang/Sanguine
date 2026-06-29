package sanguine.gui.controller;

/**
 * Class to help coordinate inputs from the controllers from both players.
 */
public final class GameCoordinator implements TurnListener {

  private final SanguineGuiController controller1;
  private final SanguineGuiController controller2;
  private int activePlayer = 1;

  /**
   * Constructor for the Game Coordinator of the two player sanguine game.
   *
   * @param c1 the controller for player 1
   * @param c2 the controller for player 2
   */
  public GameCoordinator(SanguineGuiController c1, SanguineGuiController c2) {
    this.controller1 = c1;
    this.controller2 = c2;
  }

  /**
   * Starts the game
   */
  public void startGame() {
    activePlayer = 1;
    controller1.beginTurn();
  }

  @Override
  public void onTurnFinished(int playerId) {
    if (playerId != activePlayer) {
      return;
    }

    if (activePlayer == 1) {
      controller1.endTurn();
      activePlayer = 2;
      controller2.beginTurn();
    } else {
      controller2.endTurn();
      activePlayer = 1;
      controller1.beginTurn();
    }
  }
}
