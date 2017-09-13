package se.lovebrandefelt.chess.console;

import static se.lovebrandefelt.chess.Color.WHITE;
import static se.lovebrandefelt.chess.Game.State.IN_PROGRESS;
import static se.lovebrandefelt.chess.Game.defaultSetup;

import java.util.Scanner;
import se.lovebrandefelt.chess.Game;
import se.lovebrandefelt.chess.Pos;

public class ConsoleMain {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Game game = new Game(defaultSetup(), WHITE);
    while (game.result() == IN_PROGRESS) {
      System.out.println(game.getBoard());
      if (game.getBoard().kingInCheck(game.getCurrentPlayer())) {
        System.out.println(game.getCurrentPlayer() + "'s turn - In Check");
      } else {
        System.out.println(game.getCurrentPlayer() + "'s turn");
      }
      Pos from;
      Pos to;
      while (true) {
        System.out.print("Move from: ");
        try {
          from = new Pos(scanner.next());
        } catch (IllegalArgumentException e) {
          System.out.println("That is not a square!");
          continue;
        }
        if (game.validFroms().contains(from)) {
          break;
        }
        System.out.println("You don't have a piece on that square!");
      }
      while (true) {
        System.out.print("Move to: ");
        try {
          to = new Pos(scanner.next());
        } catch (IllegalArgumentException e) {
          System.out.println("That is not a square!");
          continue;
        }
        if (game.legalMovesWithCheck(from).contains(to)) {
          break;
        }
        System.out.println("That move is not legal!");
      }
      game.makeMove(from, to);
    }
    switch (game.result()) {
      case WHITE_WON:
        System.out.println("WHITE won");
        break;
      case BLACK_WON:
        System.out.println("BLACK won");
        break;
      case DRAW:
        System.out.println("DRAW");
        break;
      default:
    }
  }
}
