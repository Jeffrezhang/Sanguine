package sanguine.gui.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import sanguine.model.Pawn;
import sanguine.model.ReadOnlySanguineModel;

/**
 * Implements the Board panel to represent a Swing component that draws the sanguine board.
 */
public class SanguineBoardPanel extends JPanel implements BoardPanel {

  private final ReadOnlySanguineModel model;
  private int highlightRow = -1;
  private int highlightCol = -1;

  /**
   * Creates a board panel that uses the given model for rendering.
   *
   * @param model the read-only model containing board state
   */
  public SanguineBoardPanel(ReadOnlySanguineModel model) {
    this.model = model;
    setPreferredSize(new Dimension(model.getNumCols() * 100,
        model.getNumRows() * 100));
  }

  @Override
  public Component asComponent() {
    return this;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    drawBoardBackground(g);
    drawCells(g);
    drawHighlight(g);
  }

  private void drawHighlight(Graphics g) {
    if (highlightRow < 0 || highlightCol < 0) {
      return;
    }

    int rows = model.getNumRows();
    int cols = model.getNumCols();
    int cellWidth = getWidth() / cols;
    int cellHeight = getHeight() / rows;

    int x = highlightCol * cellWidth;
    int y = highlightRow * cellHeight;

    g.setColor(Color.YELLOW);
    g.drawRect(x, y, cellWidth - 1, cellHeight - 1);
    g.drawRect(x + 1, y + 1, cellWidth - 3, cellHeight - 3);
  }

  @Override
  public void setHighlight(int row, int col) {
    this.highlightRow = row;
    this.highlightCol = col;
    repaint();
  }

  @Override
  public void clearHighlight() {
    this.highlightRow = -1;
    this.highlightCol = -1;
    repaint();
  }

  /**
   * Draws the background of the board.
   */
  private void drawBoardBackground(Graphics g) {
    int rows = model.getNumRows();
    int cols = model.getNumCols();
    int cellWidth = getWidth() / cols;
    int cellHeight = getHeight() / rows;

    g.setColor(Color.LIGHT_GRAY);
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        int x = c * cellWidth;
        int y = r * cellHeight;
        g.fillRect(x, y, cellWidth, cellHeight);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(x, y, cellWidth, cellHeight);
        g.setColor(Color.LIGHT_GRAY);
      }
    }
  }

  /**
   * Draws each cell of the board based on model state.
   */
  private void drawCells(Graphics g) {
    int rows = model.getNumRows();
    int cols = model.getNumCols();

    int cellWidth = getWidth() / cols;
    int cellHeight = getHeight() / rows;

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {

        char cell = model.getCell(r, c);
        int x = c * cellWidth;
        int y = r * cellHeight;

        // draw background for pawns based on owner
        Pawn pawn = model.getPawnAt(r, c);
        if (pawn != null && cell != 'R' && cell != 'B') {
          if (pawn.getOwner() == 1) {
            g.setColor(new Color(255, 200, 200)); // light red
          } else {
            g.setColor(new Color(200, 200, 255)); // light blue
          }
          g.fillRect(x, y, cellWidth, cellHeight);
          g.setColor(Color.BLACK);
          g.drawRect(x, y, cellWidth, cellHeight);
        }

        drawCell(g, cell, x, y, cellWidth, cellHeight);
      }
    }
  }

  /**
   * Draws a single cell depending on content.
   */
  private void drawCell(Graphics g, char cell, int x, int y,
                        int cellWidth, int cellHeight) {

    if (cell == 'R' || cell == 'B') {
      g.setColor(cell == 'R' ? Color.RED : Color.BLUE);
      g.fillRect(x + 5, y + 5, cellWidth - 10, cellHeight - 10);
    } else if (Character.isDigit(cell)) {
      g.setColor(Color.BLACK);
      g.drawString(String.valueOf(cell),
          x + cellWidth / 2, y + cellHeight / 2);
    }
  }
}