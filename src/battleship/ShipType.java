package battleship;

public enum ShipType {
  AIRCRAFT_CARRIER("Aircraft Carrier", 5),
  BATTLESHIP("Battleship", 4),
  SUBMARINE("Submarine", 3),
  CRUISER("Cruiser", 3),
  DESTROYER("Destroyer", 2);

  final int size;
  final String name;

  ShipType(String name, int size) {
    this.name = name;
    this.size = size;
  }
}
