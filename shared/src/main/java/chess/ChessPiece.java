package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    boolean valid(ChessPosition position) {
        if (position.getRow() > 8 || position.getRow() < 1) {
            return false;
        }
        if (position.getColumn() > 8 || position.getColumn() < 1) {
            return false;
        }
        return true;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);

        List<ChessMove> movesPossible = new ArrayList<>();

        if (piece.getPieceType() == PieceType.BISHOP) {
            int row = myPosition.getRow() + 1;
            int col = myPosition.getColumn() + 1;
            ChessPosition newPosition = new ChessPosition(row, col);

            //up up
            while (valid(newPosition)) {
                row++;
                col++;
                movesPossible.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(row, col);
            }

            // up down
            row = myPosition.getRow() + 1;
            col = myPosition.getColumn() - 1;
            newPosition = new ChessPosition(row, col);

            while (valid(newPosition)) {

                row++;
                col--;
                movesPossible.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(row, col);
            }
            // down up
            row = myPosition.getRow() - 1;
            col = myPosition.getColumn() + 1;
            newPosition = new ChessPosition(row, col);

            while (valid(newPosition)) {
                row--;
                col++;
                movesPossible.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(row, col);
            }
            // down down
            row = myPosition.getRow() - 1;
            col = myPosition.getColumn() - 1;
            newPosition = new ChessPosition(row, col);

            while (valid(newPosition)) {
                row--;
                col--;
                movesPossible.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(row, col);
            }

            return movesPossible;
        }
        return movesPossible;
    }
}
