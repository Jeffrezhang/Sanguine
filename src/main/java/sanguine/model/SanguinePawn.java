package sanguine.model;

/**
 * Immutable value object representing pawns on a single board cell.
 * owner is 1 or 2; count is how many pawns for that owner.
 */
public class SanguinePawn implements Pawn {

  private final int owner;   // 1 or 2
  private final int count;

  /**
   * Constructs a pawn stack for a given owner and count.
   *
   * @param owner the owner player (1 or 2)
   * @param count how many pawns (must be >= 1)
   */
  public SanguinePawn(int owner, int count) {
    if (owner != 1 && owner != 2) {
      throw new IllegalArgumentException("owner must be 1 or 2");
    }
    if (count < 1) {
      throw new IllegalArgumentException("count must be >= 1");
    }
    this.owner = owner;
    this.count = count;
  }

  @Override
  public int getOwner() {
    return owner;
  }

  @Override
  public int getCount() {
    return count;
  }

  @Override
  public Pawn withCount(int newCount) {
    return new SanguinePawn(this.owner, newCount);
  }
}
