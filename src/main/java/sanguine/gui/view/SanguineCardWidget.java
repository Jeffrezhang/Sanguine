package sanguine.gui.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import sanguine.model.Card;

/**
 * Swing component that renders a single card's information.
 */
public class SanguineCardWidget extends JPanel implements CardWidget {

  private final Card card;
  private final int index;
  private final Color ownerColor;
  private final boolean isSelected;

  /**
   * Constructs a card widget for the specified card and index.
   *
   * @param card the card whose info and AOE will be drawn
   * @param index the index of the card in the player's hand
   * @param ownerColor the base background color (red or blue) for this card
   * @param isSelected whether this card is currently selected
   */
  public SanguineCardWidget(Card card, int index, Color ownerColor, boolean isSelected) {
    this.card = card;
    this.index = index;
    this.ownerColor = ownerColor;
    this.isSelected = isSelected;
    setPreferredSize(new Dimension(140, 180));
  }

  @Override
  public int getIndex() {
    return index;
  }

  @Override
  public Component asComponent() {
    return this;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    drawCardBackground(g);
    drawCardFrame(g);
    drawTextInfo(g);
    drawAoeGrid(g);
  }

  /**
   * Owner-based colored background.
   */
  private void drawCardBackground(Graphics g) {
    g.setColor(ownerColor); // red or blue
    g.fillRect(0, 0, getWidth(), getHeight());
  }

  /**
   * Draws the outer card rectangle and background. Yellow if selected,
   * Otherwise owner's color (red or blue), Border is always black.
   */
  private void drawCardFrame(Graphics g) {
    if (isSelected) {
      g.setColor(Color.YELLOW);
    } else {
      g.setColor(ownerColor);
    }
    g.fillRect(0, 0, getWidth(), getHeight());

    g.setColor(Color.BLACK);
    g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
  }


  /**
   * Draws the card's name, cost, and value as text near the top.
   */
  private void drawTextInfo(Graphics g) {
    g.setColor(Color.BLACK);
    g.drawString(card.getName(), 10, 20);
    g.drawString("Cost: " + card.getCost(), 10, 35);
    g.drawString("Value: " + card.getValue(), 10, 50);
  }

  /**
   * Draws a 5x5 grid representing the card's AOE. '1' cells are
   * drawn cyan, '2' (the center) is drawn yellow, and others are dark.
   */
  private void drawAoeGrid(Graphics g) {
    int[][] aoe = card.getAoe();
    int cellSize = 14;
    int offsetX = 10;
    int offsetY = 70;

    for (int r = 0; r < 5; r++) {
      for (int c = 0; c < 5; c++) {
        int x = offsetX + c * cellSize;
        int y = offsetY + r * cellSize;
        int val = aoe[r][c];

        if (val == 1) {
          g.setColor(Color.CYAN);
        } else if (val == 2) {
          g.setColor(Color.YELLOW);
        } else {
          g.setColor(Color.DARK_GRAY);
        }
        g.fillRect(x, y, cellSize, cellSize);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, cellSize, cellSize);
      }
    }
  }
}
