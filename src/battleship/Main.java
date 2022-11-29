package battleship;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

  ArrayList<Player> players = new ArrayList<>();
  BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  boolean running = true;

  public static void main(String[] args) {
    Main game = new Main();
    game.start();
  }


  Main() {
    players.add(new Player("Player 1"));
    players.add(new Player("Player 2"));
    for (int i = 0; i < players.size(); i++) {
      players.get(i).setOpponent(players.get((i + 1) % players.size()));
    }
  }

  void start() {
    placeShips();
    shelling();
  }

  private void placeShips() {
    for (Player player : players) {
      System.out.printf("%s, place your ships on the game field%n%n", player.getName());
      for (ShipType shipType : ShipType.values()) {
        player.printField();
        System.out.printf("Enter the coordinates of the %s (%d cells):%n", shipType.name,
            shipType.size);
        boolean placed = false;
        while (!placed) {
          List<Point> coordinates = getTwoCoordinates();
          if (coordinates == null) {
            continue;
          }
          Point coordinate1 = coordinates.get(0);
          Point coordinate2 = coordinates.get(1);
          placed = player.getField().placeShip(coordinate1, coordinate2, shipType);
        }
      }
      player.printField();
      endTurn();
    }
  }

  private List<Point> getTwoCoordinates() {
    String[] tokens = getInput();
    if (tokens.length != 2) {
      System.out.println("Error! Wrong number of arguments! Try again:");
      return null;
    }
    List<Point> coordinates = new ArrayList<>(tokens.length);
    for (String token : tokens) {
      coordinates.add(parseCoordinate(token));
    }
    return coordinates.contains(null) ? null : coordinates;
  }

  private Point getOneCoordinate() {
    String[] tokens = getInput();
    if (tokens.length != 1) {
      System.out.println("Error! Wrong number of arguments! Try again:");
      return null;
    }
    return parseCoordinate(tokens[0]);
  }

  private Point parseCoordinate(String token) {
    if (token.length() < 2) {
      System.out.println("Error! You entered the wrong coordinates! Try again:");
      return null;
    }
    char letter = token.charAt(0);
    int number;
    try {
      number = Integer.parseInt(token.substring(1));
    } catch (NumberFormatException e) {
      System.out.println("Error! You entered the wrong coordinates! Try again:");
      return null;
    }
    if (!Field.isNumberCorrect(number) || !Field.isLetterCorrect(letter)) {
      System.out.println("Error! You entered the wrong coordinates! Try again:");
      return null;
    }
    return new Point(Field.decodeLetter(letter), Field.decodeNumber(number));
  }

  private String[] getInput() {
    String input;
    System.out.println();
    try {
      input = reader.readLine();
      if (input == null || input.isBlank()) {
        return new String[0];
      }
      return input.strip().toUpperCase().split("\\s+");

    } catch (IOException e) {
      e.printStackTrace();
      return new String[0];
    } finally {
      System.out.println();
    }
  }

  private void shelling() {
    System.out.println("The game starts!\n");
    int currentPlayerIndex = 0;
    while (running) {
      shoot(players.get(currentPlayerIndex));
      currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
  }


  private void shoot(Player player) {
    player.printPlayerBoard();
    System.out.printf("%s, it's your turn:", player.getName());

    boolean isDone = false;
    Field opponentField = player.getOpponent().getField();
    Point coordinate;
    while (!isDone) {
      coordinate = getOneCoordinate();
      if (coordinate == null) {
        continue;
      }
      CellState state = opponentField.getCellState(coordinate.x, coordinate.y);
      switch (state) {
        case SHIP -> hitShip(opponentField, coordinate);
        case FOG -> {
          opponentField.setCellToMiss(coordinate.x, coordinate.y);
          System.out.println("You missed.");
          endTurn();
        }
        case MISS -> {
          System.out.println("You missed.");
          endTurn();
        }
        case HIT -> {
          System.out.println("You hit a ship!");
          endTurn();
        }
        default -> {
          System.out.println("You already shoot there!");
          endTurn();
        }
      }
      isDone = true;
    }
  }

  private void hitShip(Field opponentField, Point coordinate) {
    opponentField.setCellToHit(coordinate.x, coordinate.y);
    for (Ship ship : opponentField.getShips()) {
      if (ship.getShipCells().contains(coordinate)) {
        ship.hitCell(coordinate);
        if (ship.isSinking()) {
          opponentField.removeShip(ship);
          if (opponentField.getShips().size() > 0) {
            System.out.println("You sank a ship! Specify a new target:\n");
            endTurn();
          } else {
            System.out.println("You sank the last ship. You won. Congratulations!");
            endGame();
          }
        } else {
          System.out.println("You hit a ship!");
          endTurn();
        }
        return;
      }
    }
  }

  private void endGame() {
    running = false;
  }

  private void endTurn() {
    System.out.println("Press Enter and pass the move to another player");
    getInput();
    // clean the screen
    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
  }
}
