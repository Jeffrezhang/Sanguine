package sanguine.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A class that implements SanguineModel which follows the basic rules of sanguine.
 */
public class BasicSanguine implements SanguineModel<Card> {

  private Player p1;
  private Player p2;

  boolean gameStarted;
  private char[][] textBoard;
  private int[][] scoreBoard;
  private Pawn[][] pawnBoard;
  private int concurrPasses;

  /**
   * The constructor of BasicSanguine that sets up all the boards and decks necessary.
   */
  public BasicSanguine() {
    concurrPasses = 0;
    scoreBoard = new int[5][2];
    textBoard = new char[5][7];
    pawnBoard = new Pawn[5][7];
  }

  @Override
  public List<Card> createNewDeck() {
    //goes through each "card" in the deck which is always 6 lines in the document and adds to deck
    ArrayList<Card> deck = new ArrayList<>();
    String filePath = "docs/example.deck";
    try (Scanner scanner = new Scanner(new File(filePath))) {
      int numLines = 0;
      String card = "";
      while (scanner.hasNext()) {
        card += scanner.nextLine() + System.lineSeparator();
        numLines++;
        if (numLines == 6) {
          numLines = 0;
          deck.add(new SanguineCard(card));
          card = "";
        }
      }
    } catch (FileNotFoundException e) {
      System.err.println("File not found: " + e.getMessage());
    }
    return deck;
  }

  @Override
  public void startGame(List<Card> p1deck, List<Card> p2deck,
                        PlayerType p1Type, PlayerType p2Type, int startingSize) {
    //checks for exceptions and throw when necessary
    if (gameStarted) {
      throw new IllegalStateException("Game has already been started");
    }
    if (p1deck == null || p2deck == null) {
      throw new IllegalArgumentException("p1deck and p2deck must not be null");
    }
    if (startingSize < 1 || startingSize > p1deck.size() / 3 || startingSize > p2deck.size() / 3) {
      throw new IllegalArgumentException(
          "starting size must be between 1 and ⌊deckSize/3⌋ for both players");
    }
    int needed = 3 * 5;
    if (p1deck.size() < needed || p2deck.size() < needed) {
      throw new IllegalArgumentException("Each deck must have at least " + needed + " cards");
    }
    if (hasMoreThanTwoCopies(p1deck) || hasMoreThanTwoCopies(p2deck)) {
      throw new IllegalArgumentException(
          "A deck may not contain more than two copies of any card (by name)");
    }
    //sets up the decks
    this.p1 = new SanguinePlayer(1, p1deck, p1Type);
    this.p2 = new SanguinePlayer(2, p2deck, p2Type);

    //adds pawns to the pawn board
    for (int r = 0; r < 5; r++) {
      pawnBoard[r][0] = new SanguinePawn(1, 1);
      pawnBoard[r][6] = new SanguinePawn(2, 1);

      scoreBoard[r][0] = 0;
      scoreBoard[r][1] = 0;
      for (int c = 0; c < 7; c++) {
        textBoard[r][c] = '_';
      }
    }
    //adds cards to the starting hand from the players respective deck
    for (int i = 0; i < startingSize; i++) {
      p1.drawCard();
      p2.drawCard();
    }
    gameStarted = true;
  }

  private boolean hasMoreThanTwoCopies(List<Card> deck) {
    //makes a hashmap since it keeps track of 'duplicates'
    java.util.Map<String, Integer> counts = new java.util.HashMap<>();
    for (Card c : deck) {
      if (c instanceof SanguineCard) {
        String name =  c.getName();
        counts.put(name, counts.getOrDefault(name, 0) + 1);
        if (counts.get(name) > 2) {
          return true; //found 3rd copy of same-named card
        }
      }
    }
    return false;
  }

  @Override
  public void placeCard(int row, int col, int playerTurn, Card card) {
    //checks for exceptions and throws when necessary
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    if (row < 0 || row >= 5 || col < 0 || col >= 7) {
      throw new IllegalArgumentException("row or col out of bounds");
    }
    if (card == null) {
      throw new IllegalArgumentException("card must not be null");
    }

    int cost = card.getCost();
    Pawn pawns = pawnBoard[row][col];
    if (pawns == null || pawns.getOwner() != playerTurn || pawns.getCount() < cost) {
      throw new IllegalStateException("Illegal placement: not enough pawns or opponent present");
    }
    pawnBoard[row][col] = null;
    textBoard[row][col] = (playerTurn == 1) ? 'R' : 'B';

    if (playerTurn == 1) {
      p1.removeCard(card);
      scoreBoard[row][0] += card.getValue();
    } else {
      p2.removeCard(card);
      scoreBoard[row][1] += card.getValue();
    }
    applyInfluence(card, playerTurn, row, col);
    concurrPasses = 0;
  }

  private void applyInfluence(Card card, int playerTurn, int row, int col) {
    int[][] aoe = card.getAoe();
    int[] center = getaoeCardPosition(aoe);

    for (int aoeRow = 0; aoeRow < 5; aoeRow++) {
      for (int aoeCol = 0; aoeCol < 5; aoeCol++) {
        if (aoe[aoeRow][aoeCol] != 1) {
          continue;
        }
        int deltaRow = aoeRow - center[0];
        int deltaCol = aoeCol - center[1];
        if (playerTurn == 2) {
          deltaCol = -deltaCol;
        }

        int boardRow = row + deltaRow;
        int boardCol = col + deltaCol;
        if (boardRow < 0 || boardRow >= 5 || boardCol < 0 || boardCol >= 7) {
          continue;
        }
        if (boardRow == row && boardCol == col) {
          continue;
        }
        if (textBoard[boardRow][boardCol] == 'R' || textBoard[boardRow][boardCol] == 'B') {
          continue;
        }
        Pawn current = pawnBoard[boardRow][boardCol];
        if (current != null && current.getOwner() != playerTurn) {
          continue;
        }
        if (current == null) {
          current = new SanguinePawn(playerTurn, 1);
        } else {
          int newCount = Math.min(3, current.getCount() + 1);
          current = current.withCount(newCount);
        }
        pawnBoard[boardRow][boardCol] = current;
      }
    }
  }

  private int[] getaoeCardPosition(int[][] aoe) {
    int[] rowAndCol = new int[2];
    for (int row = 0; row < aoe.length; row++) {
      for (int col = 0; col < aoe[row].length; col++) {
        if (aoe[row][col] == 2) {
          rowAndCol[0] = row;
          rowAndCol[1] = col;
        }
      }
    }
    return rowAndCol;
  }

  @Override
  public void drawCard(int playerTurn) {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }

    if (playerTurn == 1) {
      p1.drawCard();
    } else {
      p2.drawCard();
    }
  }

  @Override
  public void pass() {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    concurrPasses++;
  }

  @Override
  public boolean isGameOver() {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    return concurrPasses == 2;
  }

  @Override
  public int getRowScore(int row, int player) {
    return scoreBoard[row][player - 1];
  }

  @Override
  public int getPlayerScore(int player) {

    int score = 0;
    for (int row = 0; row < getNumRows(); row++) {
      score += getRowScore(row, player);
    }

    return score;
  }

  @Override
  public boolean isPlayerAi(int player) {
    if (player == 1) {
      return p1.isAi();
    } else {
      return p2.isAi();
    }
  }

  @Override
  public PlayerType getPlayerType(int player) {
    if (player == 1) {
      return p1.getType();
    } else {
      return p2.getType();
    }
  }

  @Override
  public int getNumRows() {
    return textBoard.length;
  }

  @Override
  public int getNumCols() {
    return textBoard[0].length;
  }

  @Override
  public char getCell(int row, int col) {
    char cardChar = textBoard[row][col];
    if (cardChar == 'R' || cardChar == 'B') {
      return cardChar;
    }

    Pawn pawns = pawnBoard[row][col];

    if (pawns != null) {
      int count = pawns.getCount();
      if (count >= 1 && count <= 9) {
        return (char) ('0' + count);
      }
    }
    return '_';
  }

  @Override
  public Pawn getPawnAt(int row, int col) {
    return pawnBoard[row][col];
  }

  @Override
  public List<Card> getHand(int player) {
    return (player == 1)
        ? List.copyOf(p1.getHand())
        : List.copyOf(p2.getHand());
  }
}
