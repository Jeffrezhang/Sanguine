package sanguine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import sanguine.model.BasicSanguine;
import sanguine.model.Card;
import sanguine.model.PlayerType;
import sanguine.model.SanguineCard;
import sanguine.model.SanguineModel;
import sanguine.view.SanguineTextualView;
import sanguine.view.TextualView;

/**
 * Tests for the textual view of the Sanguine game.
 */
public class SanguineViewTests {

  /**
   * The model used in the tests.
   */
  private SanguineModel<Card> model;

  /**
   * Sets up a fresh model before each test.
   */
  @Before
  public void setup() {
    model = new BasicSanguine();
  }

  /**
   * builds the text for a card configuration.
   *
   * @param name  the card name
   * @param cost  the card cost
   * @param value the card value
   * @param r1    first AOE row
   * @param r2    second AOE row
   * @param r3    third AOE row
   * @param r4    fourth AOE row
   * @param r5    fifth AOE row
   * @return the text describing the card
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
   * Helper for creating a SanguineCard.
   *
   * @param name  the card name
   * @param cost  the card cost
   * @param value the card value
   * @param r1    first AOE row
   * @param r2    second AOE row
   * @param r3    third AOE row
   * @param r4    fourth AOE row
   * @param r5    fifth AOE row
   * @return the constructed {@code SanguineCard}
   */
  private static SanguineCard mk(String name, int cost, int value,
                                 String r1, String r2, String r3,
                                 String r4, String r5) {
    return new SanguineCard(cardText(name, cost, value, r1, r2, r3, r4, r5));
  }

  /**
   * produces a valid deck of exactly 15 cards.
   *
   * @return a list of 15 valid cards
   */
  private static List<Card> validDeck15() {
    List<Card> d = new ArrayList<>();
    for (int i = 0; i < 15; i++) {
      d.add(mk("C" + i,
          1 + (i % 3),
          1 + (i % 5),
          "XXXXX", "XXXXX", "XXCXX", "XXXXX", "XXXXX"));
    }
    return d;
  }

  /**
   * Expected initial board string for a newly started game.
   *
   * @return textual representation of the initial board
   */
  private static String expectedInitialBoardString() {
    String nl = System.lineSeparator();
    String row = "0 1___1 0" + nl;
    return row + row + row + nl;
  }

  /**
   * Ensures the constructor rejects a null model.
   */
  @Test
  public void constructorRejectsNullModelSingleArg() {
    assertThrows(NullPointerException.class,
        () -> new SanguineTextualView(null));
  }

  /**
   * the constructor rejects any null argument.
   */
  @Test
  public void constructorRejectsNullsTwoArg() {
    assertThrows(NullPointerException.class,
        () -> new SanguineTextualView(null, new StringBuilder()));
    assertThrows(NullPointerException.class,
        () -> new SanguineTextualView(model, null));
  }

  /**
   * verifies that rendering to standard out prints the expected initial layout.
   */
  @Test
  public void renderPrintsExpectedInitialLayoutToStdout() {
    model.startGame(
        validDeck15(),
        validDeck15(),
        PlayerType.Human,
        PlayerType.Human,
        5);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream prev = System.out;
    System.setOut(new PrintStream(baos));

    try {
      TextualView view = new SanguineTextualView(model);
      view.render();
      String out = baos.toString();
      assertEquals(expectedInitialBoardString(), out);
    } finally {
      System.setOut(prev);
    }
  }

  /**
   * rendering with an appendable writes only to that appendable
   * prints the same layout when captured from stdout.
   */
  @Test
  public void renderUsesProvidedAppendableContent() {
    model.startGame(
        validDeck15(),
        validDeck15(),
        PlayerType.Human,
        PlayerType.Human,
        5);

    StringBuilder sb = new StringBuilder();
    TextualView view = new SanguineTextualView(model, sb);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream prev = System.out;
    System.setOut(new PrintStream(baos));

    try {
      view.render();
      String printed = baos.toString();
      String built = sb.toString();
      assertEquals(expectedInitialBoardString(), printed);
      assertEquals(expectedInitialBoardString(), built);
    } finally {
      System.setOut(prev);
    }
  }

  /**
   * Appendable that always throws IOException.
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

  /**
   * Ensures that any failure while appending is wrapped as an
   * IllegalStateException by the view.
   */
  @Test
  public void renderWrapsAppendFailuresAsIllegalState() {
    model.startGame(
        validDeck15(),
        validDeck15(),
        PlayerType.Human,
        PlayerType.Human,
        5);

    TextualView view =
        new SanguineTextualView(model, new FailingAppendable());
    assertThrows(IllegalStateException.class, view::render);
  }

  /**
   * verifies that the board reflects scores and cell contents after plays.
   */
  @Test
  public void renderReflectsScoreAndCellsAfterPlays() {
    model.startGame(
        validDeck15(),
        validDeck15(),
        PlayerType.Human,
        PlayerType.Human,
        5);

    Card r = model.getHand(1).stream()
        .filter(c -> c.getCost() == 1)
        .findFirst()
        .orElse(model.getHand(1).get(0));

    Card b = model.getHand(2).stream()
        .filter(c -> c.getCost() == 1)
        .findFirst()
        .orElse(model.getHand(2).get(0));

    model.placeCard(0, 0, 1, r);
    model.placeCard(0, 4, 2, b);

    StringBuilder sb = new StringBuilder();
    TextualView view = new SanguineTextualView(model, sb);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream prev = System.out;
    System.setOut(new PrintStream(baos));

    try {
      view.render();
      String s = sb.toString();
      String nl = System.lineSeparator();

      String row0 = model.getRowScore(0, 1) + " "
          + "R___B" + " "
          + model.getRowScore(0, 2) + nl;
      String row1 = model.getRowScore(1, 1) + " "
          + "1___1" + " "
          + model.getRowScore(1, 2) + nl;
      String row2 = model.getRowScore(2, 1) + " "
          + "1___1" + " "
          + model.getRowScore(2, 2) + nl;

      String expected = row0 + row1 + row2 + nl;

      assertEquals(expected, s);
      assertEquals(expected, baos.toString());
    } finally {
      System.setOut(prev);
    }
  }
}
