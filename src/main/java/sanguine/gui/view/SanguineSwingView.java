package sanguine.gui.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import sanguine.gui.controller.GuiClickListener;
import sanguine.model.Card;
import sanguine.model.ReadOnlySanguineModel;

/**
 * A Swing based implementation of the interface SanguineGuiView.
 * This class combines the board, score and hand panel to create a complete and coherent
 * representation of the game of Sanguine.
 */
public class SanguineSwingView extends JFrame implements SanguineGuiView {

  private final ReadOnlySanguineModel<Card> model;
  private final BoardPanel boardPanel;
  private HandPanel handPanel;
  private final ScorePanel redScorePanel;
  private final ScorePanel blueScorePanel;

  private GuiClickListener listener;
  private int activePlayer = 1;

  /**
   * Constructor for the GUI view of Sanguine that takes in the Read Only Model.
   *
   * @param model the read only model
   */
  public SanguineSwingView(ReadOnlySanguineModel<Card> model) {
    super("Sanguine");
    this.model = model;

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    this.boardPanel = new SanguineBoardPanel(model);
    this.handPanel = new SanguineHandPanel(model, 1);
    this.redScorePanel = new SanguineScorePanel(model, 1);
    this.blueScorePanel = new SanguineScorePanel(model, 2);
    JButton passButton = new JButton("Pass Turn");

    passButton.addActionListener(e -> {
      if (listener != null) {
        listener.onPassClicked();
      }
    });

    add(passButton, BorderLayout.NORTH);

    add(boardPanel.asComponent(), BorderLayout.CENTER);
    add(handPanel.asComponent(), BorderLayout.SOUTH);
    add(redScorePanel.asComponent(), BorderLayout.WEST);
    add(blueScorePanel.asComponent(), BorderLayout.EAST);

    installBoardMouseListener();

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private void installBoardMouseListener() {
    boardPanel.asComponent().addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (listener == null) {
          return;
        }

        int row = boardPixelToRow(e.getX(), e.getY());
        int col = boardPixelToCol(e.getX(), e.getY());
        System.out.println("Board clicked at row=" + row + " col=" + col);

        listener.onBoardClicked(row, col);
      }
    });
  }

  @Override
  public void setClickListener(GuiClickListener listener) {
    this.listener = listener;
    handPanel.setCardClickListener(listener);
  }

  @Override
  public void render() {
    boardPanel.asComponent().repaint();
    handPanel.refresh();
    redScorePanel.refresh();
    blueScorePanel.refresh();
  }

  @Override
  public int boardPixelToRow(int x, int y) {
    int height = boardPanel.asComponent().getHeight();
    int rows = model.getNumRows();
    return (int) (y / ((double) height / rows));
  }

  @Override
  public int boardPixelToCol(int x, int y) {
    int width = boardPanel.asComponent().getWidth();
    int cols = model.getNumCols();
    return (int) (x / ((double) width / cols));
  }

  @Override
  public void showMessage(String msg) {
    JOptionPane.showMessageDialog(this, msg);
  }

  @Override
  public BoardPanel getBoardPanel() {
    return boardPanel;
  }

  @Override
  public HandPanel getHandPanel() {
    return handPanel;
  }

  @Override
  public ScorePanel getScorePanel() {
    return redScorePanel;
  }

  @Override
  public void setActivePlayer(int player) {
    this.activePlayer = player;

    this.remove(handPanel.asComponent());

    HandPanel newPanel = new SanguineHandPanel(model, player);
    this.handPanel = newPanel;

    if (listener != null) {
      handPanel.setCardClickListener(listener);
    }

    this.add(handPanel.asComponent(), BorderLayout.SOUTH);

    revalidate();
    repaint();
  }

  @Override
  public void setSelectedCardIndex(int index) {
    handPanel.setSelectedCardIndex(index);
  }

  @Override
  public Component asComponent() {
    return this;
  }
}
