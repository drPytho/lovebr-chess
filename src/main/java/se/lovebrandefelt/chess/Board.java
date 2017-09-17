package se.lovebrandefelt.chess;

import static se.lovebrandefelt.chess.Color.BLACK;
import static se.lovebrandefelt.chess.Color.WHITE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Board {
  private Piece[][] squares;
  private Map<Color, List<Piece>> pieces;
  private Stack<Move> history;

  /**
   * Creates a new board with the specified number of rows and the specified number of columns.
   *
   * @param rows the number of columns
   * @param cols the number of rows
   */
  public Board(int rows, int cols) {
    squares = new Piece[rows][cols];
    pieces = new HashMap<>();
    pieces.put(WHITE, new ArrayList<>());
    pieces.put(BLACK, new ArrayList<>());
    history = new Stack<>();
  }

  /**
   * Returns the number of rows of this board.
   *
   * @return the number of rows
   */
  public int rows() {
    return squares.length;
  }

  /**
   * Returns the number of columns of this board.
   *
   * @return the number of columns
   */
  public int cols() {
    return squares[0].length;
  }

  /**
   * Returns whether the specified position is inside the bounds of this board.
   *
   * @param pos the position to check
   * @return whether the specified position is inside the bounds
   */
  public boolean isInsideBounds(Pos pos) {
    return pos.getRow() >= 0 && pos.getRow() < rows() && pos.getCol() >= 0 && pos.getCol() < cols();
  }

  /**
   * Returns whether the specified position is empty.
   *
   * @param pos the position to check
   * @return whether the specified position is empty
   */
  public boolean isEmpty(Pos pos) {
    return get(pos) == null;
  }

  /**
   * Returns the piece at the specified position.
   *
   * @param pos the position to get the piece at
   * @return the piece at the specified position
   */
  public Piece get(Pos pos) {
    return squares[pos.getRow()][pos.getCol()];
  }

  /**
   * Returns whether the specified position is threatened by the specified color.
   *
   * @param pos the position to check
   * @param by the color to check for
   * @return whether the specified position is threatened by the specified color
   */
  public boolean isThreatened(Pos pos, Color by) {
    return pieces
        .get(by)
        .stream()
        .anyMatch((piece -> piece.recursionSafeLegalMoves().containsKey(pos)));
  }

  /**
   * Returns whether the king of the specified color is in check.
   *
   * @param color the color to check
   * @return whether the king of the specified color is in check
   */
  public boolean kingInCheck(Color color) {
    return pieces
        .get(color)
        .stream()
        .anyMatch(
            (piece) -> piece.getTypeId() == 'K' && isThreatened(piece.getPos(), color.next()));
  }

  /**
   * Adds the specified piece at the specified position.
   *
   * @param piece the piece to add
   * @param pos the position to add the piece at
   * @return the added piece
   */
  public Piece add(Piece piece, Pos pos) {
    if (!isEmpty(pos)) {
      pieces.get(get(pos).getColor()).remove(get(pos));
    }
    squares[pos.getRow()][pos.getCol()] = piece;
    piece.setBoard(this);
    piece.setPos(pos);
    pieces.get(piece.getColor()).add(piece);
    return piece;
  }

  /**
   * Fills the specified row with pawns of the specified color.
   *
   * @param row the row to fill
   * @param color the color of the pawns
   */
  public void addPawnRow(int row, Color color) {
    for (int i = 0; i < cols(); i++) {
      add(new Pawn(color), new Pos(row, i));
    }
  }

  /**
   * Removes the piece at the specified position.
   *
   * @param pos the position to remove the piece at
   * @return the removed piece
   */
  public Piece remove(Pos pos) {
    Piece piece = get(pos);
    if (piece != null) {
      pieces.get(piece.getColor()).remove(piece);
    }
    squares[pos.getRow()][pos.getCol()] = null;
    return piece;
  }

  /**
   * Performs the specified move.
   * @param move the move to perform.
   */
  public void move(Move move) {
    move.perform(this);
    history.push(move);
  }

  /**
   * Undoes the last move.
   */
  public void undoMove() {
    history.pop().undo(this);
  }

  public Map<Color, List<Piece>> getPieces() {
    return pieces;
  }

  public Stack<Move> getHistory() {
    return history;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("");
    StringBuilder colStringBuilder = new StringBuilder("  ");
    for (int col = 0; col < cols(); col++) {
      colStringBuilder.append(String.format("%-2s", Pos.colToString(col)));
    }
    colStringBuilder.append("  ");
    stringBuilder.append(colStringBuilder);
    stringBuilder.append("Captures: ");
    history
        .stream()
        .filter((move) -> move.getCaptured() != null && move.getCaptured().getColor() == WHITE)
        .map((move) -> move.getCaptured().getTypeId())
        .sorted()
        .forEach((typeId) -> stringBuilder.append(typeId).append(' '));
    stringBuilder.append('\n');
    for (int row = rows() - 1; row >= 0; row--) {
      String rowString = String.format("%-2s", Pos.rowToString(row));
      stringBuilder.append(rowString);
      for (Piece piece : squares[row]) {
        if (piece == null) {
          stringBuilder.append("- ");
        } else {
          if (piece.getColor() == WHITE) {
            stringBuilder.append(piece.getTypeId()).append(" ");
          } else {
            stringBuilder.append(Character.toLowerCase(piece.getTypeId())).append(" ");
          }
        }
      }
      stringBuilder.append(rowString);
      stringBuilder.append('\n');
    }
    stringBuilder.append(colStringBuilder.toString().toLowerCase());
    stringBuilder.append("Captures: ");
    history
        .stream()
        .filter((move) -> move.getCaptured() != null && move.getCaptured().getColor() == BLACK)
        .map((move) -> move.getCaptured().getTypeId())
        .sorted()
        .forEach((typeId) -> stringBuilder.append(typeId).append(' '));
    return stringBuilder.toString();
  }
}
