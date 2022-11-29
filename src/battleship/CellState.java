package battleship;

public enum CellState {
  FOG, SHIP, HIT, MISS;

  @Override
  public String toString() {
    return switch (this) {
      case FOG -> "~";
      case SHIP -> "O";
      case HIT -> "X";
      case MISS -> "M";
    };
  }

}
