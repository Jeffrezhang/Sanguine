package sanguine.controller;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import sanguine.model.Card;
import sanguine.model.PlayerType;
import sanguine.model.SanguineModel;
import sanguine.view.SanguineTextualView;

/**
 * The implementation of the SanguineController in the textual form: all inputs and outputs
 * are through text.
 */
public class SanguineTextualController implements SanguineController {

  private Readable reader;
  private Appendable builder;
  private SanguineModel model;
  private SanguineTextualView view;

  /**
   * The constructor of the textual controller that takes in a readable and appendable to set the
   * controllers reader and builder equal to.
   *
   * @param rd the readable that is being taken in
   * @param ap the appendable that is being taken in
   */
  public SanguineTextualController(Readable rd, Appendable ap) {

    if (rd == null || ap == null) {
      throw new IllegalArgumentException("Arguments cannot be null");
    }

    reader = rd;
    builder = ap;
  }

  @Override
  public <C extends Card> void playGame(SanguineModel<C> model, List<C> p1deck, List<C> p2deck,
                                        int startingSize) {
    if (model == null || p1deck == null || p2deck == null) {
      throw new IllegalArgumentException("Arguments cannot be null");
    }
    if (startingSize < 0) {
      writeMessage("Starting size cannot be negative");
      return;
    }

    this.model = model;
    this.model.startGame(p1deck, p2deck, PlayerType.Human, PlayerType.Human, startingSize);
    view = new SanguineTextualView(this.model, builder);

    Scanner sc = new Scanner(reader);
    String name;
    int row;
    int col;
    int playerTurn = 0;

    welcomeMessage();
    do {
      //renders the view and then prompts the current player for a move
      view.render();
      if (!model.isGameOver()) {
        writeMessage("Player " + ((playerTurn % 2) + 1) + "'s turn." + System.lineSeparator());
        writeMessage("Type Instructions (Card Name, Row, Column) or 'pass': "
            + System.lineSeparator());
      }
      String line = sc.nextLine().trim();
      //if the player chooses to pass it passes the turn then renders the board and continues
      //the loop
      if (line.trim().equalsIgnoreCase("pass")) {
        model.pass();
        if (!model.isGameOver()) {
          writeMessage("Turn passed. Next players move." + System.lineSeparator());
          view.render();
          writeMessage("Type Instructions (Card Name, Row, Column) or 'pass': "
              + System.lineSeparator());
        }
        playerTurn++;
        continue;
      }

      //if the player didn't pass then that means to number of tokens should be equal to 3
      String[] tokens = line.split(" ");
      if (tokens.length != 3) {
        writeMessage("Wrong number of tokens: Format: <CardName> <Row> <Col> or 'pass'"
            + System.lineSeparator());
        continue;
      }

      //sets the variables equal to the corresponding tokens while checking for exceptions
      name = tokens[0];
      try {
        row = Integer.parseInt(tokens[1]);
        col = Integer.parseInt(tokens[2]);
      } catch (NumberFormatException e) {
        writeMessage("Invalid coordinates. Please enter numbers for row and col."
            + System.lineSeparator());
        continue;
      }

      //checks if the player actually has the card they want to play in hand
      int currentPlayer = (playerTurn % 2) + 1;
      List<Card> hand = model.getHand(currentPlayer);
      Card cardInHand = null;
      for (Card card : hand) {
        if (card.getName().equalsIgnoreCase(name)) {
          cardInHand = card;
          break;
        }
      }

      //if the card is in player hand then attempt to make the move
      if (cardInHand != null) {
        try {
          model.placeCard(row, col, currentPlayer, cardInHand);
          model.drawCard(currentPlayer);
          writeMessage("Move successful! Player " + currentPlayer + " placed " + name + "."
              + System.lineSeparator());
          writeMessage("Next player's turn." + System.lineSeparator());
          writeMessage("Type Instructions (Card Name, Row, Column) or 'pass': "
              + System.lineSeparator());
        } catch (IllegalArgumentException | IllegalStateException e) {
          writeMessage("Invalid move: " + e.getMessage() + System.lineSeparator());
          continue;
        }
      } else {
        writeMessage("Card not in hand");
        continue;
      }

      playerTurn++;
    } while (!model.isGameOver() && sc.hasNextLine());

    int p1Score = model.getPlayerScore(1);
    int p2Score = model.getPlayerScore(2);

    writeMessage("P1 score: " + p1Score + System.lineSeparator());
    writeMessage("P2 score: " + p2Score + System.lineSeparator());
    if (p1Score > p2Score) {
      writeMessage("P1 WIN" + System.lineSeparator());
    } else if (p2Score > p1Score) {
      writeMessage("P2 WIN" + System.lineSeparator());
    } else {
      writeMessage("Tie Game No Winner" + System.lineSeparator());
    }
    farewellMessage();
  }

  private void writeMessage(String message) throws IllegalStateException {
    try {
      builder.append(message);

    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  private void welcomeMessage() throws IllegalStateException {
    writeMessage("Welcome to Sanguine (Queens Blood)!" + System.lineSeparator());
  }

  private void farewellMessage() throws IllegalStateException {
    writeMessage("Thank you for using playing!");
  }
}
