package battleship;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Ship {

  ShipType type;
  boolean horizontal;
  boolean sunk;
  List<Point> shipCells = new ArrayList<>();

  Ship(ShipType type, boolean horizontal, Point start) {
    this.horizontal = horizontal;
    this.type = type;
    this.sunk = false;

    int x = start.x;
    int y = start.y;
    int dx = horizontal ? 0 : 1;
    int dy = horizontal ? 1 : 0;
    for (int i = 0; i < type.size; i++) {
      shipCells.add(new Point(x, y));
      x += dx;
      y += dy;
    }
  }

  protected void hitCell(Point point) {
    shipCells.remove(point);
  }

  protected boolean isSinking() {
    return getShipCells().size() == 0;
  }

  protected List<Point> getShipCells() {
    List<Point> copy = new ArrayList<>(shipCells.size());
    for (Point cell : shipCells) {
      copy.add((Point) cell.clone());
    }
    return copy;
  }

  @Override
  public String toString() {
    return type.toString();
  }
}
