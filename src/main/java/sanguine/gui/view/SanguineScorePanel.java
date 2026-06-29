package sanguine.gui.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import sanguine.model.Card;
import sanguine.model.ReadOnlySanguineModel;

/**
 * This class uses JPanel to show scoring.
 */
public class SanguineScorePanel extends JPanel implements ScorePanel {

  private final ReadOnlySanguineModel<Card> model;
  private final int player;
  private final Color playerColor;
  private final String playerName;

  /**
   * Creates a score panel bound to the given read-only model for a single player.
   *
   * @param model  the model used to obtain scores
   * @param player which player this panel represents (1 = Red, 2 = Blue)
   */
  public SanguineScorePanel(ReadOnlySanguineModel<Card> model, int player) {
    if (player != 1 && player != 2) {
      throw new IllegalArgumentException("player must be 1 or 2");
    }
    this.model = model;
    this.player = player;
    this.playerColor = (player == 1) ? Color.RED : Color.BLUE;
    this.playerName = (player == 1) ? "Red" : "Blue";

    setPreferredSize(new Dimension(120, 240));
  }

  @Override
  public void refresh() {
    repaint();
  }

  @Override
  public Component asComponent() {
    return this;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    drawRowScores(g);
    drawTotalScores(g);
  }

  /**
   * Draws the individual row scores for each player next to the board.
   */
  private void drawRowScores(Graphics g) {
    int rows = model.getNumRows();
    int ystep = getHeight() / (rows + 2);

    g.setColor(Color.BLACK);
    for (int r = 0; r < rows; r++) {
      int rowScore = model.getRowScore(r, player);
      int y = (r + 1) * ystep;
      g.drawString(playerName + " R" + r + ": " + rowScore, 10, y);
    }
  }

  /**
   * Draws the total scores for both players at the bottom of the panel.
   * Assumes the model has a getPlayerScore method.
   */
  private void drawTotalScores(Graphics g) {
    int rows = model.getNumRows();
    int ybase = (rows + 1) * (getHeight() / (rows + 2));
    int total = model.getPlayerScore(player);

    g.setColor(playerColor);
    g.drawString(playerName + " total: " + total, 10, ybase);
  }
}
