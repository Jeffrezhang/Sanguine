package sanguine.gui.controller;

import sanguine.gui.player.GamePlayer;
import sanguine.gui.player.PlayerActionListener;
import sanguine.gui.view.SanguineGuiView;
import sanguine.model.Card;
import sanguine.model.ReadOnlySanguineModel;


/**
 * The main GUI controller for the Sanguine game.
 */
public class SanguineGuiController implements GuiClickListener, PlayerActionListener {

  private final int playerId;
  private final boolean isHuman;

  private final SanguineGuiView view;
  private final ProxySanguineModel proxy;
  private final ReadOnlySanguineModel<Card> model;
  private final GamePlayer player;
  private TurnListener turnListener;

  private boolean myTurn;
  private int selectedCardIndex = -1;
  private int pendingRow = -1;
  private int pendingCol = -1;


  /**
   * Constructor for the gui Controller. Takes in the gui view of sanguine alongside the
   * read only model and proxy model.
   *
   * @param view the gui view of sanguine
   * @param model the read only model of sanguine
   * @param proxy the proxy model of sanguine
   */
  public SanguineGuiController(int playerId,
                               boolean isHuman,
                               SanguineGuiView view,
                               ReadOnlySanguineModel<Card> model,
                               ProxySanguineModel proxy,
                               GamePlayer player) {

    this.playerId = playerId;
    this.isHuman = isHuman;
    this.view = view;
    this.model = model;
    this.proxy = proxy;
    this.player = player;

    this.view.setClickListener(this);
    this.player.setActionListener(this);
    this.view.setActivePlayer(playerId);
    this.view.setSelectedCardIndex(-1);
    this.view.render();
  }

  /**
   * Sets the listener for the controller.
   *
   * @param listener the listener that the controllers listener is being set to.
   */
  public void setTurnListener(TurnListener listener) {
    this.turnListener = listener;
  }

  /**
   * Called by some game coordinator when it becomes this player's turn.
   */
  public void beginTurn() {
    if (model.isGameOver()) {
      checkGameOver();
      return;
    }

    myTurn = true;
    selectedCardIndex = -1;
    pendingRow = -1;
    pendingCol = -1;

    view.setActivePlayer(playerId);
    view.setSelectedCardIndex(-1);
    view.getBoardPanel().clearHighlight();
    view.render();

    // Notify the player object (AI will compute a move; human usually does nothing here)
    player.onYourTurn();
  }

  /**
   * Called when this player is no longer the active player.
   * A coordinator should call beginTurn() on the other player's controller.
   */
  public void endTurn() {
    myTurn = false;
    selectedCardIndex = -1;
    pendingRow = -1;
    pendingCol = -1;
    view.getBoardPanel().clearHighlight();
    view.setSelectedCardIndex(-1);
    view.render();
  }

  @Override
  public void onCardClicked(int cardIndex) {

    System.out.println("Card clicked, index = " + cardIndex);
    // Ignore if it's not this player's turn, or if this is actually an AI player.
    if (!myTurn || !isHuman) {
      return;
    }

    if (cardIndex < 0 || cardIndex >= model.getHand(playerId).size()) {
      view.showMessage("Invalid card selection.");
      return;
    }

    selectedCardIndex = cardIndex;
    pendingRow = -1;
    pendingCol = -1;

    Card card = model.getHand(playerId).get(cardIndex);
    view.setSelectedCardIndex(cardIndex);
    view.showMessage("Selected card: " + card.getName());
    view.render();
  }

  @Override
  public void onBoardClicked(int row, int col) {
    if (!myTurn || !isHuman) {
      return;
    }

    if (selectedCardIndex == -1) {
      System.out.println("No card selected when board clicked.");
      view.showMessage("No card selected.");
      return;
    }

    // First click on a cell = "preview" / legality check + highlight
    if (pendingRow == -1) {
      boolean legal = proxy.isLegalPlacement(playerId, selectedCardIndex, row, col);
      if (!legal) {
        view.showMessage("Illegal placement: not enough pawns or cell blocked.");
        return;
      }

      pendingRow = row;
      pendingCol = col;
      view.getBoardPanel().setHighlight(row, col);
      view.showMessage("Click again to confirm placement at (" + row + "," + col + ").");
      return;
    }

    if (pendingRow == row && pendingCol == col) {
      placeCard(selectedCardIndex, row, col);
      return;
    }

    pendingRow = -1;
    pendingCol = -1;
    view.getBoardPanel().clearHighlight();
    view.showMessage("Cell changed — click again to highlight.");
  }

  @Override
  public void onPassClicked() {
    if (!myTurn || !isHuman) {
      return;
    }
    handlePass();
  }

  @Override
  public void onPlayCard(int cardIndex, int row, int col) {
    if (!myTurn) {
      return;
    }
    placeCard(cardIndex, row, col);
  }

  @Override
  public void onPass() {
    if (!myTurn) {
      return;
    }
    handlePass();
  }

  /**
   * Sends the place card command to the proxy model.
   *
   * @param row the row to place the card
   * @param col the column to place the card
   */
  private void placeCard(int cardIndex, int row, int col) {
    try {
      proxy.placeCard(playerId, cardIndex, row, col);
    } catch (RuntimeException e) {
      // Shouldn't normally happen if we checked legality, but show it if it does.
      view.showMessage("Invalid move: " + e.getMessage());
      return;
    }

    view.getBoardPanel().clearHighlight();
    selectedCardIndex = -1;
    pendingRow = -1;
    pendingCol = -1;
    view.setSelectedCardIndex(-1);
    view.render();

    finishTurn();
  }

  /**
   * Executes a pass for this player and finishes their turn.
   */
  private void handlePass() {
    proxy.pass(playerId);
    view.getBoardPanel().clearHighlight();
    selectedCardIndex = -1;
    pendingRow = -1;
    pendingCol = -1;
    view.setSelectedCardIndex(-1);
    view.render();

    finishTurn();
  }

  /**
   * Common end-of-turn logic: check for game over, otherwise notify the turnListener.
   */
  private void finishTurn() {
    myTurn = false;

    if (checkGameOver()) {
      // Game over; no next turn.
      return;
    }

    if (turnListener != null) {
      turnListener.onTurnFinished(playerId);
    }
  }

  /**
   * Checks if game is over.
   */
  private boolean checkGameOver() {
    if (!model.isGameOver()) {
      return false;
    }
    view.showMessage("Game Over!\n"
        + "Red Score: " + model.getPlayerScore(1) + "\n"
        + "Blue Score: " + model.getPlayerScore(2) + "\n"
        + winnerText());

    view.setClickListener(null);
    return true;
  }

  /**
   * Creates the text to show when the game is over.
   *
   * @return the text created when game is over
   */
  private String winnerText() {
    int r = model.getPlayerScore(1);
    int b = model.getPlayerScore(2);

    if (r > b) {
      return "Winner: RED";
    }
    if (b > r) {
      return "Winner: BLUE";
    }
    return "It's a tie!";
  }

}
