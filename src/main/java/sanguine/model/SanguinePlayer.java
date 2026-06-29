package sanguine.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Represents a player in the game of sanguine can be human can be AI.
 */
public class SanguinePlayer implements Player {

  private final int id;
  private final PlayerType playerType;
  private final Queue<Card> deck;
  private final List<Card> hand;

  /**
   * Constructs a Player after taking in the players id and starting deck.
   *
   * @param id player id
   * @param startingDeck players starting deck
   */
  public SanguinePlayer(int id, List<Card> startingDeck, PlayerType playerType) {
    this.id = id;
    this.playerType = playerType;
    this.deck = new LinkedList<>(startingDeck);
    this.hand = new ArrayList<>();
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public Queue<Card> getDeck() {
    return deck;
  }

  @Override
  public List<Card> getHand() {
    return hand;
  }

  @Override
  public boolean isAi() {
    return playerType != PlayerType.Human;
  }

  @Override
  public PlayerType getType() {
    return playerType;
  }

  @Override
  public void drawCard() {
    if (!deck.isEmpty()) {
      hand.add(deck.remove());
    }
  }

  @Override
  public void removeCard(Card c) {
    hand.remove(c);
  }
}
