package sanguine.gui.view;

import java.awt.Component;
import sanguine.gui.controller.GuiClickListener;

/**
 * A GUI view for the game of Sanguine.
 */
public interface SanguineGuiView {

  /**
   * Assign the click listener.
   *
   * @param listener the click listener
   */
  void setClickListener(GuiClickListener listener);

  /**
   * Refreshes the entire GUI to the current state of the model.
   */
  void render();

  /**
   * Returns the row on the board of the mouse click.
   *
   * @param x mouse click x position
   * @param y mouse click y position
   * @return the row on the board
   */
  int boardPixelToRow(int x, int y);

  /**
   * Returns the column on the board of the mouse click.
   *
   * @param x mouse click x position
   * @param y mouse click y position
   * @return the column on the board
   */
  int boardPixelToCol(int x, int y);

  /**
   * Displays the message to the user.
   *
   * @param msg the message to be shown to the user
   */
  void showMessage(String msg);

  /**
   * Gets and returns the board panel.
   *
   * @return the board panel
   */
  BoardPanel getBoardPanel();

  /**
   * Gets and returns the score panel.
   *
   * @return the score panel
   */
  ScorePanel getScorePanel();

  /**
   * Gets and returns the hand panel.
   *
   * @return the hand panel
   */
  HandPanel getHandPanel();

  /**
   * Sets the active player to the given player.
   *
   * @param player the player who is taking their turn
   */
  void setActivePlayer(int player);

  /**
   * Updates which hand card should be shown as selected.
   *
   * @param index card index, or -1 for none
   */
  void setSelectedCardIndex(int index);


  /**
   * Returns this view as a Swing component to be displayed in a windowed environment.
   *
   * @return this view as a Swing component
   */
  Component asComponent();
}
