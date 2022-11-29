package battleship;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Field {

  static int SIZE = 10;
  CellState[][] field = new CellState[SIZE][SIZE];

  List<Ship> ships = new ArrayList<>();

  Field() {
    for (CellState[] row : field) {
      Arrays.fill(row, CellState.FOG);
    }
  }

  public void setCellToShip(int x, int y) {
    setCell(x, y, CellState.SHIP);
  }

  public void setCellToMiss(int x, int y) {
    setCell(x, y, CellState.MISS);
  }

  public void setCellToHit(int x, int y) {
    setCell(x, y, CellState.HIT);
  }

  void setCell(int x, int y, CellState state) {
    field[x][y] = state;
  }

  public static int decodeLetter(char letter) {
    // from A = 65 to J = 74
    int coordinate = (int) letter - 'A';
    if (coordinate >= SIZE) {
      throw new IllegalArgumentException("Wrong coordinate: " + letter);
    }
    return coordinate;
  }

  public static int decodeNumber(int num) {
    int coordinate = num - 1;
    if (coordinate >= SIZE) {
      throw new IllegalArgumentException("Wrong coordinate: " + num);
    }
    return coordinate;
  }

  public static boolean isNumberCorrect(int num) {
    // number 1...10
    int coordinate = num - 1;
    return (coordinate < SIZE) && coordinate >= 0;
  }

  public static boolean isLetterCorrect(char letter) {
    // letter A = 65 ... J = 74
    int coordinate = (int) letter - 'A';
    return (coordinate < SIZE) && coordinate >= 0;
  }

  protected boolean isSpaceAvailable(Point begin, Point end, Boolean horizontal) {
    int x1 = begin.x;
    int y1 = begin.y;
    int x2 = end.x;
    int y2 = end.y;

    if (areDiagonalNeighboursTaken(x1, y1) || areDiagonalNeighboursTaken(x2, y2)) {
      return false;
    }

    for (int row = x1; row <= x2; row++) {
      for (int column = y1; column <= y2; column++) {
        if (getCellState(row, column) != CellState.FOG) {
          return false;
        }
        // avoid multiple neighbours-checking
        if (row != 0 && getCellState(row - 1, column) != CellState.FOG ||
            row != Field.SIZE - 1 && getCellState(row + 1, column) != CellState.FOG ||
            column != 0 && getCellState(row, column - 1) != CellState.FOG ||
            column != Field.SIZE - 1 && getCellState(row, column + 1) != CellState.FOG) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean areDiagonalNeighboursTaken(int x, int y) {
    return x != 0 && y != 0 && getCellState(x - 1, y - 1) != CellState.FOG ||
        x != 0 && y != Field.SIZE - 1 && getCellState(x - 1, y + 1) != CellState.FOG ||
        x != Field.SIZE - 1 && y != 0 && getCellState(x + 1, y - 1) != CellState.FOG ||
        x != Field.SIZE - 1 && y != Field.SIZE - 1 && getCellState(x + 1, y + 1) != CellState.FOG;
  }

  protected void addShip(ShipType type, boolean horizontal, Point begin) {
    ships.add(new Ship(type, horizontal, begin));
  }

  public CellState getCellState(int x, int y) {
    return field[x][y];
  }

  protected List<Ship> getShips() {
    return ships;
  }

  protected void removeShip(Ship ship) {
    ships.remove(ship);
  }

  protected boolean placeShip(Point begin, Point end, ShipType shipType) {
    int x1 = Math.min(begin.x, end.x);
    int y1 = Math.min(begin.y, end.y);
    int x2 = Math.max(begin.x, end.x);
    int y2 = Math.max(begin.y, end.y);
    if ((x1 == x2 && y1 == y2) || (x1 != x2 && y1 != y2)) {
      System.out.println("Error! Wrong ship location! Try again:");
      return false;
    }
    boolean horizontal = x1 == x2;
    if ((x1 == x2 && (y2 - y1) + 1 != shipType.size) ||
        (y1 == y2 && (x2 - x1) + 1 != shipType.size)) {
      System.out.printf("Error! Wrong length of the %s! Try again:%n", shipType.name);
      return false;
    }
    if (!isSpaceAvailable(begin, end, horizontal)) {
      System.out.println("Error! You placed it too close to another one. Try again:");
      return false;
    }
    setShip(shipType, horizontal, new Point(x1, y1));
    return true;
  }

  private void setShip(ShipType type, boolean horizontal, Point begin) {
    int x1 = begin.x;
    int y1 = begin.y;
    int xSize = horizontal ? 1 : type.size;
    int ySize = horizontal ? type.size : 1;
    for (int row = x1; row <= x1 + xSize - 1; row++) {
      for (int column = y1; column <= y1 + ySize - 1; column++) {
        System.out.println(row + " " + column);
        setCellToShip(row, column);
      }
    }
    addShip(type, horizontal, begin);
  }

  public String toFoggedString() {
    StringBuilder builder = new StringBuilder();
    builder.append("  1 2 3 4 5 6 7 8 9 10\n");
    CellState state;
    for (int i = 65; i <= 74; i++) {
      builder.append((char) i).append(" ");
      for (int j = 1; j <= 10; j++) {
        state = getCellState(decodeLetter((char) i), decodeNumber(j));
        builder.append(state == CellState.SHIP ? CellState.FOG : state).append(" ");
      }
      builder.delete(builder.length() - 1, builder.length());
      builder.append("\n");
    }
    return builder.toString();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("  1 2 3 4 5 6 7 8 9 10\n");
    for (int i = 65; i <= 74; i++) {
      builder.append((char) i).append(" ");
      for (int j = 1; j <= 10; j++) {
        builder.append(getCellState(decodeLetter((char) i), decodeNumber(j))).append(" ");
      }
      builder.delete(builder.length() - 1, builder.length());
      builder.append("\n");
    }
    return builder.toString();
  }

}
