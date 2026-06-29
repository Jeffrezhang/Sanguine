package sanguine.gui.player;

/**
 * Interface to represent a player in the game of Sanguine.
 */
public interface GamePlayer {
  /**
   * Gets and returns the id of the Player.
   *
   * @return the id of the player.
   */
  int getId();

  /**
   * controller registers to be notified when this player decides on a move.
   */
  void setActionListener(PlayerActionListener listener);

  /**
   * called when it's this player's turn.
   */
  void onYourTurn();
}
