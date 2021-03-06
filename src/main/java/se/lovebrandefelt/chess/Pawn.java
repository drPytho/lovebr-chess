package se.lovebrandefelt.chess;

import static se.lovebrandefelt.chess.Color.WHITE;
import static se.lovebrandefelt.chess.Piece.CaptureRule.CANT_CAPTURE;
import static se.lovebrandefelt.chess.Piece.CaptureRule.MUST_CAPTURE;

import java.util.HashMap;
import java.util.Map;

public class Pawn extends Piece {
  public Pawn(Color color) {
    super(color, 'P');
  }

  @Override
  public Map<Pos, Move> legalMoves() {
    Map<Pos, Move> legalMoves = new HashMap<>();
    addMovesInDirection(new Pos(moveDirection(), 0), legalMoves, Move::new, CANT_CAPTURE, 1);
    addMovesInDirection(new Pos(moveDirection(), -1), legalMoves, Move::new, MUST_CAPTURE, 1);
    addMovesInDirection(new Pos(moveDirection(), 1), legalMoves, Move::new, MUST_CAPTURE, 1);

    if (getBoard().getHistory().stream().noneMatch((move) -> move.getPiece() == this)
        && getBoard().isEmpty(getPos().offset(new Pos(moveDirection(), 0)))) {
      addMovesInDirection(new Pos(2 * moveDirection(), 0), legalMoves, Move::new, CANT_CAPTURE, 1);
    }

    // Checks for available en passant moves
    if (!getBoard().getHistory().empty()) {
      Move lastMove = getBoard().getHistory().peek();
      if (lastMove.getPiece().getTypeId() == 'P'
          && lastMove
              .getFrom()
              .subtract(lastMove.getTo())
              .equals(new Pos(2 * moveDirection(), 0))) {
        Pos difference = lastMove.getTo().subtract(getPos());
        if (difference.equals(new Pos(0, -1)) || difference.equals(new Pos(0, 1))) {
          Pos to = getPos().offset(new Pos(moveDirection(), 0)).offset(difference);
          legalMoves.put(to, new EnPassantMove(getPos(), to));
        }
      }
    }
    return legalMoves;
  }

  /**
   * Returns the direction this pawn moves in.
   *
   * @return the direction this pawn moves in
   */
  public int moveDirection() {
    if (getColor() == WHITE) {
      return 1;
    } else {
      return -1;
    }
  }

  /**
   * Returns whether this pawn can promote.
   *
   * @return whether this pawn can promote
   */
  public boolean canPromote() {
    return ((getPos().getRow() + moveDirection() + (getBoard().rows() + 1))
        % (getBoard().rows() + 1)
        == getBoard().rows());
  }

  /**
   * Promotes this pawn into a piece of the type specified by typeId.
   *
   * @param typeId the type of the piece to promote into
   */
  public void promoteInto(char typeId) {
    switch (typeId) {
      case 'B':
        getBoard().add(new Bishop(getColor()), getPos());
        break;
      case 'N':
        getBoard().add(new Knight(getColor()), getPos());
        break;
      case 'R':
        getBoard().add(new Rook(getColor()), getPos());
        break;
      case 'Q':
        getBoard().add(new Queen(getColor()), getPos());
        break;
      default:
    }
  }
}
