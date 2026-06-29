package sanguine.gui;

import java.util.List;
import sanguine.gui.controller.FillFirstAi;
import sanguine.gui.controller.GameCoordinator;
import sanguine.gui.controller.ProxyController;
import sanguine.gui.controller.ProxyModel;
import sanguine.gui.controller.ProxySanguineModel;
import sanguine.gui.controller.RowScoreAi;
import sanguine.gui.controller.SanguineCommunicator;
import sanguine.gui.controller.SanguineGuiController;
import sanguine.gui.controller.SanguineProxyController;
import sanguine.gui.player.AiGamePlayer;
import sanguine.gui.player.GamePlayer;
import sanguine.gui.player.HumanGamePlayer;
import sanguine.gui.view.SanguineGuiView;
import sanguine.gui.view.SanguineSwingView;
import sanguine.model.BasicSanguine;
import sanguine.model.Card;
import sanguine.model.PlayerType;
import sanguine.model.ReadOnlyBasicSanguine;
import sanguine.model.ReadOnlySanguineModel;
import sanguine.model.SanguineModel;

/**
 * Starts and plays a game of Sanguine that includes two different players, these players are
 * represented by their own class. Players have their own controllers as well.
 */
public class SanguineGame {

  /**
   * Plays a game of Sanguine.
   *
   * @param args the arguments used to set up the game, may whether the red player is human or
   *             AI and whether the blue player is human or AI. Defaults are set as human if no
   *             arguments are given.
   */
  public static void main(String[] args) {

    String redTypeArg = (args.length > 0) ? args[0].toLowerCase() : "human";
    String blueTypeArg = (args.length > 1) ? args[1].toLowerCase() : "human";
    SanguineModel<Card> real = new BasicSanguine();
    ReadOnlySanguineModel<Card> readOnly = new ReadOnlyBasicSanguine(real);
    ProxyController proxyController = new SanguineProxyController(real);
    SanguineCommunicator communicator =
        new SanguineCommunicator((SanguineProxyController) proxyController);
    ProxyModel proxyModel =
        new ProxySanguineModel(readOnly, communicator);

    List<Card> p1Deck = real.createNewDeck();
    List<Card> p2Deck = real.createNewDeck();
    PlayerType p1Type = parsePlayerType(redTypeArg);
    PlayerType p2Type = parsePlayerType(blueTypeArg);

    real.startGame(p1Deck, p2Deck, p1Type, p2Type, 5);
    SanguineGuiView view1 = new SanguineSwingView(readOnly);
    SanguineGuiView view2 = new SanguineSwingView(readOnly);

    GamePlayer player1 = makeGamePlayer(1, p1Type, readOnly, proxyModel);
    GamePlayer player2 = makeGamePlayer(2, p2Type, readOnly, proxyModel);
    boolean redIsHuman = (p1Type == PlayerType.Human);
    boolean blueIsHuman = (p2Type == PlayerType.Human);

    SanguineGuiController controller1 =
        new SanguineGuiController(
            1,
            redIsHuman,
            view1,
            readOnly,
            (ProxySanguineModel) proxyModel,
            player1);
    SanguineGuiController controller2 =
        new SanguineGuiController(
            2,
            blueIsHuman,
            view2,
            readOnly,
            (ProxySanguineModel) proxyModel,
            player2);

    GameCoordinator coordinator = new GameCoordinator(controller1, controller2);
    controller1.setTurnListener(coordinator);
    controller2.setTurnListener(coordinator);
    coordinator.startGame();
  }

  /**
   * Helper to turn the player type string into a player type enum.
   *
   * @param s the string representing the player type
   * @return the enum of the corresponding player type
   */
  private static PlayerType parsePlayerType(String s) {
    return switch (s) {
      case "human" -> PlayerType.Human;
      case "fillfirst" -> PlayerType.AI;
      case "rowscore" -> PlayerType.ROWSCOREAI;
      default -> {
        System.out.println("Unknown player type '" + s
            + "', defaulting to human.");
        yield PlayerType.Human;
      }
    };
  }

  /**
   * Builds a Sanguine game player with the given parameters.
   *
   * @param id the id of the player.
   * @param type the player type of the player
   * @param model the model the player is using.
   * @param proxy the proxy model the player is using
   * @return the player after creation.
   */
  private static GamePlayer makeGamePlayer(
      int id,
      PlayerType type,
      ReadOnlySanguineModel<Card> model,
      ProxyModel<Card> proxy) {

    return switch (type) {
      case Human -> new HumanGamePlayer(id);
      case AI ->
          new AiGamePlayer(id, new FillFirstAi(), model, proxy);
      case ROWSCOREAI -> new AiGamePlayer(id, new RowScoreAi(), model, proxy);
      default ->
          new HumanGamePlayer(id);
    };
  }
}
