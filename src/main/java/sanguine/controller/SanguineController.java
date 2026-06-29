package sanguine.controller;

import java.util.List;
import sanguine.model.Card;
import sanguine.model.SanguineModel;

/**
 * Interface to represent the necessary functions to control a game of Sanguine.
 */
public interface SanguineController {

  /**
   * Plays a game of sanguine.
   *
   * @param model the model of sanguine being played
   * @param p1deck the deck of the player 1
   * @param p2deck the deck of the player 2
   * @param startingSize the starting size of the hands of each player
   * @param <C> ensures that all objects passed through the parameters extends the Card interface
   */
  <C extends Card> void playGame(SanguineModel<C> model, List<C> p1deck, List<C> p2deck,
                                 int startingSize);

}
