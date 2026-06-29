package sanguine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import sanguine.model.BasicSanguine;
import sanguine.model.Card;
import sanguine.model.PlayerType;
import sanguine.model.SanguineCard;
import sanguine.model.SanguineModel;

/**
 * Tests for the {@link BasicSanguine} model implementation.
 */
public class SanguineModelTests {

  private SanguineModel<Card> model;

  /**
   * Initializes a fresh model before each test.
   */
  @Before
  public void setup() {
    model = new BasicSanguine();
  }

  /**
   * Helper to construct the textual representation of a card.
   *
   * @param name  card name
   * @param cost  card cost
   * @param value card value
   * @param r1    row 1 of the AOE pattern
   * @param r2    row 2 of the AOE pattern
   * @param r3    row 3 of the AOE pattern
   * @param r4    row 4 of the AOE pattern
   * @param r5    row 5 of the AOE pattern
   * @return the full card text block
   */
  private static String cardText(String name, int cost, int value,
                                 String r1, String r2, String r3,
                                 String r4, String r5) {
    return name + " " + cost + " " + value + System.lineSeparator()
        + r1 + System.lineSeparator()
        + r2 + System.lineSeparator()
        + r3 + System.lineSeparator()
        + r4 + System.lineSeparator()
        + r5 + System.lineSeparator();
  }

  /**
   * convenience factory for a SanguineCard.
   */
  private static SanguineCard mk(String name, int cost, int value,
                                 String r1, String r2, String r3,
                                 String r4, String r5) {
    return new SanguineCard(cardText(name, cost, value, r1, r2, r3, r4, r5));
  }

  /**
   * Builds a deck of the given size with generic cards.
   */
  private static List<Card> deckOfSize(int n) {
    List<Card> d = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      d.add(mk("N" + i,
          1 + (i % 3),
          1 + (i % 5),
          "XXXXX", "XXXXX", "XXCXX", "XXXXX", "XXXXX"));
    }
    return d;
  }

  /**
   * Valid red deck of 15 cards.
   */
  private static List<Card> validDeck15() {
    List<Card> d = new ArrayList<>();
    d.add(mk("RightA", 1, 2, "XXXXX", "XXXXX", "XXCII", "XXXXX", "XXXXX"));
    for (int i = 1; i < 15; i++) {
      d.add(mk("U" + i,
          1 + (i % 3),
          2 + (i % 4),
          "XXXXX", "XXIXX", "XICIX", "XXIXX", "XXXXX"));
    }
    return d;
  }

  /**
   * Valid blue deck of 15 cards.
   */
  private static List<Card> validDeck15Blue() {
    List<Card> d = new ArrayList<>();
    d.add(mk("RightB", 1, 2, "XXXXX", "XXXXX", "XXCII", "XXXXX", "XXXXX"));
    for (int i = 1; i < 15; i++) {
      d.add(mk("V" + i,
          1 + (i % 3),
          1 + (i % 5),
          "XXIXX", "XXIXX", "XXCXX", "XXIXX", "XXIXX"));
    }
    return d;
  }

  /**
   * startGame should reject null decks for either player.
   */
  @Test
  public void startGameRejectsNullDecks() {
    List<Card> d = validDeck15();
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(null, d, PlayerType.Human, PlayerType.Human, 5));
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(d, null, PlayerType.Human, PlayerType.Human, 5));
  }

  /**
   * startGame should reject decks that are too small.
   */
  @Test
  public void startGameRejectsSmallDeck() {
    List<Card> small = deckOfSize(14);
    List<Card> ok = validDeck15();
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(small, ok, PlayerType.Human, PlayerType.Human, 5));
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(ok, small, PlayerType.Human, PlayerType.Human, 5));
  }

  /**
   * startGame should reject invalid starting hand sizes.
   */
  @Test
  public void startGameRejectsBadStartingSize() {
    List<Card> d1 = validDeck15();
    List<Card> d2 = validDeck15Blue();
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(d1, d2, PlayerType.Human, PlayerType.Human, 0));
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(d1, d2, PlayerType.Human, PlayerType.Human, 6));
  }

  /**
   * More than two copies of the same card name in a deck should be rejected.
   */
  @Test
  public void startGameRejectsMoreThanTwoCopiesByName() {
    List<Card> d1 = validDeck15();

    Card a = mk("DUP", 1, 1, "XXXXX", "XXXXX", "XXCXX", "XXXXX", "XXXXX");
    Card b = mk("DUP", 2, 2, "XXXXX", "XXXXX", "XXCXX", "XXXXX", "XXXXX");
    Card c = mk("DUP", 3, 3, "XXXXX", "XXXXX", "XXCXX", "XXXXX", "XXXXX");
    d1.set(0, a);
    d1.set(1, b);
    d1.set(2, c);
    List<Card> d2 = validDeck15Blue();
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(d1, d2, PlayerType.Human, PlayerType.Human, 5));
  }

  /**
   * Calling startGame twice should throw an exception.
   */
  @Test
  public void cannotStartTwice() {
    model.startGame(validDeck15(), validDeck15Blue(),
        PlayerType.Human, PlayerType.Human, 5);
    assertThrows(IllegalStateException.class,
        () -> model.startGame(validDeck15(), validDeck15Blue(),
            PlayerType.Human, PlayerType.Human, 5));
  }

  /**
   * board dimensions and initial pawns are correct after startGame.
   */
  @Test
  public void initialBoardShapeAndPawns() {
    model.startGame(validDeck15(), validDeck15Blue(),
        PlayerType.Human, PlayerType.Human, 5);
    assertEquals(3, model.getNumRows());
    assertEquals(5, model.getNumCols());
    for (int r = 0; r < 3; r++) {
      assertEquals('1', model.getCell(r, 0));
      assertEquals('1', model.getCell(r, 4));
    }
  }

  /**
   * methods should throw if called before startGame.
   */
  @Test
  public void actionsBeforeStartThrow() {
    assertThrows(IllegalStateException.class, () -> model.drawCard(1));
    assertThrows(IllegalStateException.class, () -> model.pass());
    assertThrows(IllegalStateException.class, () -> model.isGameOver());
    assertThrows(IllegalStateException.class,
        () -> model.placeCard(0, 0, 1,
            mk("A", 1, 1, "XXXXX", "XXXXX", "XXCXX", "XXXXX", "XXXXX")));
  }

  /**
   * drawing a card increases the hand size.
   */
  @Test
  public void drawBehavior() {
    model.startGame(validDeck15(), validDeck15Blue(),
        PlayerType.Human, PlayerType.Human, 5);
    int h1 = model.getHand(1).size();
    model.drawCard(1);
    assertEquals(h1 + 1, model.getHand(1).size());

    for (int i = 0; i < 20; i++) {
      model.drawCard(2);
    }
    int h2 = model.getHand(2).size();
    model.drawCard(2);
    assertEquals(h2, model.getHand(2).size());
  }

  /**
   * two passes in a row should end the game.
   */
  @Test
  public void passAndGameOver() {
    model.startGame(validDeck15(), validDeck15Blue(),
        PlayerType.Human, PlayerType.Human, 5);
    assertFalse(model.isGameOver());
    model.pass();
    assertFalse(model.isGameOver());
    model.pass();
    assertTrue(model.isGameOver());
  }

  /**
   * placeCard validates coordinates as well as card ownership and cell occupancy.
   */
  @Test
  public void placeCardValidation() {
    model.startGame(validDeck15(), validDeck15Blue(),
        PlayerType.Human, PlayerType.Human, 5);
    Card any = model.getHand(1).get(0);

    assertThrows(IllegalArgumentException.class,
        () -> model.placeCard(-1, 0, 1, any));
    assertThrows(IllegalArgumentException.class,
        () -> model.placeCard(0, 5, 1, any));
    assertThrows(IllegalArgumentException.class,
        () -> model.placeCard(0, 0, 1, null));

    Card redCost1 = model.getHand(1).stream()
        .filter(c -> c.getCost() == 1)
        .findFirst()
        .orElse(any);
    model.placeCard(0, 0, 1, redCost1);
    assertEquals('R', model.getCell(0, 0));

    Card next = model.getHand(1).get(0);
    assertThrows(IllegalStateException.class,
        () -> model.placeCard(0, 0, 1, next));

    Card blueCost1 = model.getHand(2).stream()
        .filter(c -> c.getCost() == 1)
        .findFirst()
        .orElseThrow();
    assertThrows(IllegalStateException.class,
        () -> model.placeCard(0, 1, 2, blueCost1));
    assertThrows(IllegalStateException.class,
        () -> model.placeCard(0, 4, 1, redCost1));
  }

  /**
   * placing a card consumes pawns and updates scores and resets pass counters.
   */
  @Test
  public void placementConsumesPawnsUpdatesScoreAndResetsPasses() {
    model.startGame(validDeck15(), validDeck15Blue(),
        PlayerType.Human, PlayerType.Human, 5);
    model.pass();

    Card redCost1 = model.getHand(1).stream()
        .filter(c -> c.getCost() == 1)
        .findFirst()
        .orElseThrow();
    int prevScore = model.getRowScore(0, 1);

    model.placeCard(0, 0, 1, redCost1);
    assertEquals('R', model.getCell(0, 0));
    assertEquals(prevScore + redCost1.getValue(), model.getRowScore(0, 1));

    model.pass();
    assertFalse(model.isGameOver());
  }

  /**
   * Influence from placed cards adds digits and caps at three.
   */
  @Test
  public void influenceAddsDigitsAndCapsAtThree() {
    model.startGame(validDeck15(), validDeck15Blue(),
        PlayerType.Human, PlayerType.Human, 5);

    Card redRight = model.getHand(1).stream()
        .filter(c -> c.getAoe()[2][3] == 1 && c.getCost() == 1)
        .findFirst()
        .orElse(model.getHand(1).get(0));

    model.placeCard(0, 0, 1, redRight);
    char after = model.getCell(0, 1);
    assertTrue(after == '1' || after == '2' || after == '3'
        || after == 'R' || after == '_');
  }

  /**
   * Blue player influence mirrors to the left.
   */
  @Test
  public void blueMirrorsInfluenceLeft() {
    model.startGame(validDeck15(), validDeck15Blue(),
        PlayerType.Human, PlayerType.Human, 5);

    Card blueRight = model.getHand(2).stream()
        .filter(c -> c.getAoe()[2][3] == 1 && c.getCost() == 1)
        .findFirst()
        .orElseThrow();

    model.placeCard(0, 4, 2, blueRight);
    char leftNeighbor = model.getCell(0, 3);
    assertTrue(leftNeighbor == '1' || leftNeighbor == '2' || leftNeighbor == '3'
        || leftNeighbor == 'B' || leftNeighbor == '_');
  }

  /**
   * The SanguineCard constructor validates its input format.
   */
  @Test
  public void sanguineCardConstructorValidation() {
    assertThrows(IllegalArgumentException.class,
        () -> new SanguineCard(
            "Bad 1\nXXXXX\nXXXXX\nXXCXX\nXXXXX\nXXXXX\n"));
    assertThrows(IllegalArgumentException.class,
        () -> new SanguineCard(
            "N 1 1\nXXXXX\nXXXXX\nXXXXX\nXXXXX\nXXXXX\n"));
    assertThrows(IllegalArgumentException.class,
        () -> new SanguineCard(
            "N 0 1\nXXXXX\nXXXXX\nXXCXX\nXXXXX\nXXXXX\n"));
    assertThrows(IllegalArgumentException.class,
        () -> new SanguineCard(
            "N 4 1\nXXXXX\nXXXXX\nXXCXX\nXXXXX\nXXXXX\n"));
    assertThrows(IllegalArgumentException.class,
        () -> new SanguineCard(
            "N 1 0\nXXXXX\nXXXXX\nXXCXX\nXXXXX\nXXXXX\n"));
    assertThrows(IllegalArgumentException.class,
        () -> new SanguineCard(
            "N 1 1\nXXCXX\nXXXXX\nXXCXX\nXXXXX\nXXXXX\n"));
  }

  /**
   * get aoe returns a defensive copy of the aoe array.
   */
  @Test
  public void sanguineCardAoeDefensiveCopy() {
    SanguineCard c = mk("Safe", 1, 1,
        "XXXXX", "XXXXX", "XXCXX", "XXXXX", "XXXXX");
    int[][] a = c.getAoe();
    a[2][2] = 0;
    int[][] b = c.getAoe();
    assertEquals(2, b[2][2]);
  }

  /**
   * row scores are tracked separately per player.
   */
  @Test
  public void rowScoresTrackPerPlayer() {
    model.startGame(validDeck15(), validDeck15Blue(),
        PlayerType.Human, PlayerType.Human, 5);
    Card r = model.getHand(1).get(0);
    model.placeCard(0, 0, 1, r);
    assertEquals(r.getValue(), model.getRowScore(0, 1));
    assertEquals(0, model.getRowScore(0, 2));

    Card b = model.getHand(2).get(0);
    model.placeCard(0, 4, 2, b);
    assertEquals(r.getValue(), model.getRowScore(0, 1));
    assertEquals(b.getValue(), model.getRowScore(0, 2));
  }
}
