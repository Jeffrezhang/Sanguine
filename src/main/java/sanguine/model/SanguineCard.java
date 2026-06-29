package sanguine.model;

/**
 * class for the sanguine card implementing the card interface.
 */
public class SanguineCard implements Card {

  String name;
  int cost;
  int value;
  String[] rows;
  int[][] aoe;

  /**
   * Constructor that takes in a string of info that is formated properly then converts it into
   * the information of the card.
   *
   * @param info the information of the card in string form
   */
  public SanguineCard(String info) {
    String[] brokenDown = info.split(System.lineSeparator());
    String cardInfo = brokenDown[0];
    String[] cardInfoSplit = cardInfo.split(" ");
    if (cardInfoSplit.length != 3) {
      throw new IllegalArgumentException("Invalid card format");
    }
    if (brokenDown.length - 1 != 5) {
      throw new IllegalArgumentException("Invalid card format");
    }
    //initializes all the cards information and defines all info but the rows and aoe
    rows = new String[5];
    name = cardInfoSplit[0];
    cost = Integer.parseInt(cardInfoSplit[1]);
    value = Integer.parseInt(cardInfoSplit[2]);
    aoe = new int[5][5];
    System.arraycopy(brokenDown, 1, rows, 0, brokenDown.length - 1);
    //creates the aoe matrix
    for (int i = 0; i < rows.length; i++) {
      char[] curRow = rows[i].toCharArray();
      for (int j = 0; j < curRow.length; j++) {
        if (curRow[j] == 'X') {
          aoe[i][j] = 0;
        }
        if (curRow[j] == 'I') {
          aoe[i][j] = 1;
        }
        if (curRow[j] == 'C') {
          aoe[i][j] = 2;
        }
      }
    }
    //checks if the information extracted meets the criteria needed for the info
    if (cost < 1 || cost > 3) {
      throw new IllegalArgumentException("Invalid card cost (must be 1..3)");
    }
    if (value <= 0) {
      throw new IllegalArgumentException("Invalid card value (must be > 0)");
    }
    int centerCount = 0;
    for (int r = 0; r < 5; r++) {
      for (int c = 0; c < 5; c++) {
        if (aoe[r][c] == 2) {
          centerCount++;
        }
      }
    }
    if (centerCount != 1 || aoe[2][2] != 2) {
      throw new IllegalArgumentException("Card must have a single center C at (2,2)");
    }
  }

  @Override
  public int[][] getAoe() {
    int[][] toReturn = new int[5][5];
    for (int row = 0; row < 5; row++) {
      System.arraycopy(aoe[row], 0, toReturn[row], 0, 5);

    }
    return toReturn;

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
