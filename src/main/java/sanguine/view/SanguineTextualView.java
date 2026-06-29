package sanguine.view;

import static java.lang.System.out;

import java.io.IOException;
import sanguine.model.Card;
import sanguine.model.SanguineModel;

/**
 * implements TextualView to represent a SanguineModel in a textual view.
 */
public class SanguineTextualView implements TextualView {

  private final SanguineModel<Card> model;
  private Appendable builder;

  /**
   * The constructor of the textual view that takes in a model and sets its model equal to the
   * model.
   *
   * @param model the model of Sanguine being taken in
   */
  public SanguineTextualView(SanguineModel<Card> model) {

    if (model == null) {
      throw new NullPointerException("model is null");
    }
    this.model = model;
    this.builder = new StringBuilder();
  }

  /**
   * This is the constructor for the textual view version.
   *
   * @param model takes in the model to know what to display.
   * @param builder the builder to make it.
   */
  public SanguineTextualView(SanguineModel<Card> model, Appendable builder) {

    if (model == null || builder == null) {
      throw new NullPointerException("arguments cannot be null");
    }

    this.model = model;
    this.builder = builder;
  }

  private Appendable build() {

    //builds the board in a String representation
    try {

      int numRows = model.getNumRows();
      int numCols = model.getNumCols();

      for (int row = 0; row < numRows; row++) {
        builder.append(String.valueOf(model.getRowScore(row, 1))).append(" ");
        for (int col = 0; col < numCols; col++) {

          builder.append(model.getCell(row, col));
        }
        builder.append(" ").append(String.valueOf(model.getRowScore(row, 2)))
            .append(System.lineSeparator());
      }
      builder.append(System.lineSeparator());
    } catch (IOException e) {
      throw new IllegalStateException("Append Failed", e);
    }
    return builder;
  }

  @Override
  public void render() {

    //renders the board that has been built
    out.append(build().toString());
  }
}
