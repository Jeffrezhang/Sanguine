package sanguine.gui.controller;

/**
 * Callback used by a per-player controller to signal that this player's turn
 * has finished, so some coordinator can start the next player's turn.
 */
public interface TurnListener {
  /**
   * Called when the given player has finished their turn.
   *
   * @param playerId the player who just finished (1 or 2)
   */
  void onTurnFinished(int playerId);
}
