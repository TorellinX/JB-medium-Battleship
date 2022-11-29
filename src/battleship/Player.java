package battleship;

public class Player {

  String name;
  Field field;
  Player opponent;

  Player(String name) {
    this.name = name;
    field = new Field();
  }

  protected void setOpponent(Player opponent) {
    this.opponent = opponent;
  }

  protected Player getOpponent() {
    return this.opponent;
  }

  protected Field getField() {
    return field;
  }

  String getName() {
    return name;
  }

  void printField() {
    System.out.println(field);
  }

  void printFoggedField() {
    System.out.println(field.toFoggedString());
  }

  void printPlayerBoard() {
    getOpponent().printFoggedField();
    System.out.println("---------------------");
    printField();
  }

}
