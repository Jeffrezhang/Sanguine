package sanguine.gui.player;

/**
 * Implementation of the Game Player interface to represent a human player.
 */
public final class HumanGamePlayer implements GamePlayer {
  private final int id;
  private PlayerActionListener listener;

  /**
   * Constructs a human player.
   *
   * @param id the id of the player.
   */
  public HumanGamePlayer(int id) {
    this.id = id;
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

  }
}
