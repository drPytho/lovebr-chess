package se.lovebrandefelt.chess;

public class Move {
  private Piece piece;
  private Pos from;
  private Pos to;
  private Piece captured;

  protected Move(Pos from, Pos to) {
    this.from = from;
    this.to = to;
  }

  public Piece getPiece() {
    return piece;
  }

  protected void setPiece(Piece piece) {
    this.piece = piece;
  }

  public Pos getFrom() {
    return from;
  }

  public Pos getTo() {
    return to;
  }

  public Piece getCaptured() {
    return captured;
  }

  protected void setCaptured(Piece captured) {
    this.captured = captured;
  }

  /**
   * Performs this move on the specified board.
   * @param board the board to perform this move on.
   */
  protected void perform(Board board) {
    captured = board.remove(to);
    piece = board.add(board.get(from), to);
    board.remove(from);
  }

  /**
   * Undoes this move on the specified board.
   * @param board the board to undo this move on.
   */
  protected void undo(Board board) {
    board.add(piece, from);
    if (captured != null) {
      board.add(captured, to);
    } else {
      board.remove(to);
    }
  }
}
