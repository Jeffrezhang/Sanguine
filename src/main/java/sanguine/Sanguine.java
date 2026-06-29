package sanguine;

import java.io.InputStreamReader;
import java.util.List;
import sanguine.controller.SanguineTextualController;
import sanguine.model.BasicSanguine;
import sanguine.model.Card;
import sanguine.model.SanguineModel;

/**
 * Represents the game of Sanguine in a playable format.
 */
public class Sanguine {

  /**
   * Plays sanguine using the arguments which represents the starting hand. If arguments are not
   * stated then starting hand is set to the default of 5.
   *
   * @param args the starting hand size
   */
  public static void main(String[] args) {

    int startingSize = 5;
    if (args.length == 1) {
      startingSize = Integer.parseInt(args[0]);
    }
    if (args.length > 1) {
      throw new IllegalArgumentException("Must provide a single argument: the starting Size");
    }

    SanguineModel<Card> model = new BasicSanguine();
    List<Card> p1deck = model.createNewDeck();
    List<Card> p2deck = model.createNewDeck();

    SanguineTextualController controller =
        new SanguineTextualController(new InputStreamReader(System.in), System.out);

    controller.playGame(model, p1deck, p2deck, startingSize);
  }
}
