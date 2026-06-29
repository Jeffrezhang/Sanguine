package sanguine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import sanguine.controller.SanguineController;
import sanguine.controller.SanguineTextualController;
import sanguine.model.Card;
import sanguine.model.Pawn;
import sanguine.model.PlayerType;
import sanguine.model.SanguineModel;

/**
 * Tests for the textual Sanguine controller.
 */
public class SanguineControllerTests {

  /**
   * Simple card implementation for tests.
   */
  public static final class MockCard implements Card {
    private final String name;
    private final int cost;
    private final int value;

    /**
     * A mock card used for testing.
     *
     * @param name name of the card.
     * @param cost how much it costs
     * @param value its respective value.
     */
    public MockCard(String name, int cost, int value) {
      this.name = name;
      this.cost = cost;
      this.value = value;
    }

    @Override
    public String toString() {
      return name;
    }

    @Override
    public int[][] getAoe() {
      return new int[5][5];
    }

    @Override
    public int getValue() {
      return value;
    }

    @Override
    public int getCost() {
      return cost;
    }

    @Override
    public String getName() {
      return name;
    }
  }

  /**
   * Mock model used for controller tests.
   *
   * @param <C> the card type
   */
  public static final class MockModel<C extends Card> implements SanguineModel<C> {
    public boolean started;
    public boolean passed;
    public boolean gameOver;
    public boolean placed;
    public boolean drew;

    public int lastRow;
    public int lastCol;
    public int lastPlayer;
    public String lastName;

    public int startCalls;
    public int passCount;

    public List<C> hand1 = new ArrayList<>();
    public List<C> hand2 = new ArrayList<>();

    /**
     * Sets the hands for both players.
     */
    public void setHands(List<C> h1, List<C> h2) {
      hand1 = h1;
      hand2 = h2;
    }

    @Override
    public List<C> createNewDeck() {
      return new ArrayList<>();
    }

    @Override
    public void startGame(List<Card> p1deck, List<Card> p2deck,
                          PlayerType p1Type, PlayerType p2Type,
                          int startingSize) {
      // Not used in these tests.
    }

    /**
     * simplified startGame used by tests.
     */
    public void startGame(List<C> p1deck, List<C> p2deck, int startingSize) {
      started = true;
      startCalls++;
    }

    @Override
    public void placeCard(int row, int col, int playerTurn, Card card) {
      placed = true;
      lastRow = row;
      lastCol = col;
      lastPlayer = playerTurn;
      lastName = card.getName();
    }

    @Override
    public void drawCard(int playerTurn) {
      drew = true;
    }

    @Override
    public void pass() {
      passed = true;
      passCount++;
      if (passCount >= 2) {
        gameOver = true;
      }
    }

    @Override
    public boolean isGameOver() {
      return gameOver;
    }

    @Override
    public int getRowScore(int row, int player) {
      return 0;
    }

    @Override
    public int getPlayerScore(int player) {
      return 0;
    }

    @Override
    public boolean isPlayerAi(int player) {
      return false;
    }

    @Override
    public PlayerType getPlayerType(int player) {
      return null;
    }

    @Override
    public int getNumRows() {
      return 3;
    }

    @Override
    public int getNumCols() {
      return 5;
    }

    @Override
    public char getCell(int row, int col) {
      if (col == 0 || col == 4) {
        return '1';
      }
      return '_';
    }

    @Override
    public Pawn getPawnAt(int row, int col) {
      return null;
    }

    /**
     * returns a fixed pawn count.
     */
    public int getPawns(int player, int row, int col) {
      return 1;
    }

    @Override
    public List<Card> getHand(int player) {
      if (player == 1) {
        return new ArrayList<>(hand1);
      }
      return new ArrayList<>(hand2);
    }
  }

  /**
   * Model that throws when placeCard is called.
   */
  public static final class ThrowingPlaceModel implements SanguineModel<Card> {
    public List<Card> hand1 = new ArrayList<>();
    public List<Card> hand2 = new ArrayList<>();
    public boolean started;

    /**
     * Sets the hands for the players.
     *
     * @param h1 hand 1.
     * @param h2 hand 2
     */
    public void setHands(List<Card> h1, List<Card> h2) {
      hand1 = h1;
      hand2 = h2;
    }

    @Override
    public List<Card> createNewDeck() {
      return new ArrayList<>();
    }

    @Override
    public void startGame(List<Card> p1deck, List<Card> p2deck,
                          PlayerType p1Type, PlayerType p2Type,
                          int startingSize) {
      // Not used.
    }

    /**
     * simplified startGame version.
     */
    public void startGame(List<Card> p1deck, List<Card> p2deck, int startingSize) {
      started = true;
    }

    @Override
    public void placeCard(int row, int col, int playerTurn, Card card) {
      throw new IllegalStateException("bad");
    }

    @Override
    public void drawCard(int playerTurn) {
      // no-op
    }

    @Override
    public void pass() {
      // no-op
    }

    @Override
    public boolean isGameOver() {
      return true;
    }

    @Override
    public int getRowScore(int row, int player) {
      return 0;
    }

    @Override
    public int getPlayerScore(int player) {
      return 0;
    }

    @Override
    public boolean isPlayerAi(int player) {
      return false;
    }

    @Override
    public PlayerType getPlayerType(int player) {
      return null;
    }

    @Override
    public int getNumRows() {
      return 3;
    }

    @Override
    public int getNumCols() {
      return 5;
    }

    @Override
    public char getCell(int row, int col) {
      if (col == 0 || col == 4) {
        return '1';
      }
      return '_';
    }

    @Override
    public Pawn getPawnAt(int row, int col) {
      return null;
    }

    /**
     * Returns a fixed pawn count.
     */
    public int getPawns(int player, int row, int col) {
      return 1;
    }

    @Override
    public List<Card> getHand(int player) {
      if (player == 1) {
        return new ArrayList<>(hand1);
      }
      return new ArrayList<>(hand2);
    }
  }

  /**
   * appendable that always throws.
   */
  public static final class FailingAppendable implements Appendable {

    @Override
    public Appendable append(CharSequence csq) throws IOException {
      throw new IOException("fail");
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end)
        throws IOException {
      throw new IOException("fail");
    }

    @Override
    public Appendable append(char c) throws IOException {
      throw new IOException("fail");
    }

    @Override
    public String toString() {
      return "";
    }
  }

  private MockModel<Card> model;

  /**
   * a fresh mock model before each test.
   */
  @Before
  public void setup() {
    model = new MockModel<>();
  }

  private static List<Card> sampleDeck(int n) {
    List<Card> deck = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      deck.add(new MockCard("C" + i, 1, 1));
    }
    return deck;
  }

  @Test
  public void constructorRejectsNulls() {
    assertThrows(IllegalArgumentException.class,
        () -> new SanguineTextualController(null, new StringBuilder()));
    assertThrows(IllegalArgumentException.class,
        () -> new SanguineTextualController(new StringReader(""), null));
  }

  @Test
  public void playGameRejectsNullArgs() {
    SanguineController controller =
        new SanguineTextualController(new StringReader(""), new StringBuilder());

    assertThrows(IllegalArgumentException.class,
        () -> controller.playGame(null, sampleDeck(15), sampleDeck(15), 5));
    assertThrows(IllegalArgumentException.class,
        () -> controller.playGame(model, null, sampleDeck(15), 5));
    assertThrows(IllegalArgumentException.class,
        () -> controller.playGame(model, sampleDeck(15), null, 5));
  }

  @Test
  public void negativeStartingSizePrintsAndReturns() {
    StringBuilder out = new StringBuilder();
    SanguineController controller =
        new SanguineTextualController(new StringReader(""), out);

    controller.playGame(model, sampleDeck(15), sampleDeck(15), -1);

    assertTrue(out.toString().contains("Starting size cannot be negative"));
    assertFalse(model.started);
  }

  @Test
  public void passTwiceEndsGame() {
    String input = "pass\npass\n";
    StringBuilder out = new StringBuilder();
    SanguineController controller =
        new SanguineTextualController(new StringReader(input), out);

    controller.playGame(model, sampleDeck(15), sampleDeck(15), 5);

    assertTrue(model.started);
    assertTrue(model.passed);
    assertTrue(model.gameOver);
    assertTrue(out.toString().contains("Welcome to Sanguine"));
    assertTrue(out.toString().contains("Turn passed"));
    assertTrue(out.toString().contains("Thank you for using playing!"));
  }

  @Test
  public void wrongTokenCountShowsMessage() {
    String input = "badline\npass\npass\n";
    StringBuilder out = new StringBuilder();
    SanguineController controller =
        new SanguineTextualController(new StringReader(input), out);

    controller.playGame(model, sampleDeck(15), sampleDeck(15), 5);

    assertTrue(out.toString().contains("Wrong number of tokens"));
  }

  @Test
  public void invalidCoordinatesShowsMessage() {
    String input = "A x y\npass\npass\n";
    StringBuilder out = new StringBuilder();
    SanguineController controller =
        new SanguineTextualController(new StringReader(input), out);

    model.setHands(List.of(new MockCard("A", 1, 1)), new ArrayList<>());

    controller.playGame(model, sampleDeck(15), sampleDeck(15), 5);

    assertTrue(out.toString().contains(
        "Invalid coordinates. Please enter numbers for row and col."));
  }

  @Test
  public void cardNotInHandShowsMessage() {
    String input = "Z 0 0\npass\npass\n";
    StringBuilder out = new StringBuilder();
    SanguineController controller =
        new SanguineTextualController(new StringReader(input), out);

    model.setHands(new ArrayList<>(), new ArrayList<>());

    controller.playGame(model, sampleDeck(15), sampleDeck(15), 5);

    assertTrue(out.toString().contains("Card not in hand"));
  }

  @Test
  public void successfulMoveCallsPlaceAndDrawAndMessages() {
    String input = "A 0 0\npass\npass\n";
    StringBuilder out = new StringBuilder();
    SanguineController controller =
        new SanguineTextualController(new StringReader(input), out);

    model.setHands(List.of(new MockCard("A", 1, 1)), new ArrayList<>());

    controller.playGame(model, sampleDeck(15), sampleDeck(15), 5);

    assertTrue(model.placed);
    assertTrue(model.drew);
    assertEquals(0, model.lastRow);
    assertEquals(0, model.lastCol);
    assertEquals(1, model.lastPlayer);
    assertEquals("A", model.lastName);
    assertTrue(out.toString().contains("Move successful! Player 1 placed A."));
  }

  @Test
  public void invalidMoveExceptionIsReported() {
    String input = "A 0 0\npass\npass\n";
    StringBuilder out = new StringBuilder();
    SanguineController controller =
        new SanguineTextualController(new StringReader(input), out);

    ThrowingPlaceModel throwing = new ThrowingPlaceModel();
    throwing.setHands(List.of(new MockCard("A", 1, 1)), new ArrayList<>());

    controller.playGame(throwing, sampleDeck(15), sampleDeck(15), 5);

    assertTrue(out.toString().contains("Invalid move: bad"));
  }

  @Test
  public void appendFailuresThrowIllegalState() {
    String input = "";
    FailingAppendable failing = new FailingAppendable();
    SanguineController controller =
        new SanguineTextualController(new StringReader(input), failing);

    assertThrows(IllegalStateException.class,
        () -> controller.playGame(model, sampleDeck(15), sampleDeck(15), -1));
  }

  @Test
  public void promptsAndTurnTextAppear() {
    String input = "pass\npass\n";
    StringBuilder out = new StringBuilder();
    SanguineController controller =
        new SanguineTextualController(new StringReader(input), out);

    controller.playGame(model, sampleDeck(15), sampleDeck(15), 5);

    String s = out.toString();
    assertTrue(s.contains("Player 1's turn."));
    assertTrue(s.contains("Type Instructions (Card Name, Row, Column) or 'pass':"));
    assertTrue(s.contains("Next player’s move.") || s.contains("Next player's"));
  }
}
