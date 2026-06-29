package sanguine.gui.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JPanel;
import sanguine.gui.controller.GuiClickListener;
import sanguine.model.Card;
import sanguine.model.ReadOnlySanguineModel;

/**
 * This class uses the JPanel to help create the GUI.
 */
public class SanguineHandPanel extends JPanel implements HandPanel {

  private final ReadOnlySanguineModel<Card> model;
  private final int player;

  private GuiClickListener listener;
  private int selectedIndex = -1;

  /**
   * Constructs a hand panel for the given player.
   *
   * @param model  the read-only model to query hand contents
   * @param player the player whose hand will be shown (1 or 2)
   */
  public SanguineHandPanel(ReadOnlySanguineModel<Card> model, int player) {
    this.model = model;
    this.player = player;
    setLayout(new FlowLayout());
  }

  @Override
  public void refresh() {
    removeAll();
    List<Card> hand = model.getHand(player);
    Color ownerColor = (player == 1) ? Color.RED : Color.BLUE;

    for (int i = 0; i < hand.size(); i++) {
      Card card = hand.get(i);
      boolean isSelected = (i == selectedIndex);
      SanguineCardWidget widget = new SanguineCardWidget(card, i, ownerColor, isSelected);
      Component comp = widget.asComponent();
      final int idx = i;

      comp.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if (listener != null) {
            listener.onCardClicked(idx);
          }
        }
      });
      add(comp);
    }
    revalidate();
    repaint();
  }

  @Override
  public void setCardClickListener(GuiClickListener listener) {
    this.listener = listener;
  }

  @Override
  public void setSelectedCardIndex(int index) {
    this.selectedIndex = index;
    refresh();
  }

  @Override
  public Component asComponent() {
    return this;
  }
}
