package sanguine.model;

/**
 * Interface for the pawns from the aoe.
 */
public interface Pawn {

  /**
   * Returns the owner (1 or 2).
   */
  int getOwner();

  /**
   * Returns how many pawns are in this cell.
   */
  int getCount();

  /**
   * Returns a new Pawn with the same owner and the given count.
   */
  Pawn withCount(int newCount);
}
