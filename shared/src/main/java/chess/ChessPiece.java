package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

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
    boolean valid(ChessPosition position, ChessBoard board) {
        if (position.getRow() > 8 || position.getRow() < 1) {
            return false;
        }
        if (position.getColumn() > 8 || position.getColumn() < 1) {
            return false;
        }
        if (board.getPiece(position) != null) {
            if (getTeamColor() == board.getPiece(position).getTeamColor()) {
                return false;
            }
        }
        return true;
    }

    boolean inBounds(ChessPosition move) {
        if ((move.getRow() <= 8 && move.getRow() >= 1) && (move.getColumn() <= 8 && move.getColumn() >= 1)) {
            return true;
        }
        return false;
    }

    List<ChessMove> slide(ChessBoard board, ChessPosition myPosition, int rowChange, int colChange) {
        List<ChessMove> movesPossible = new ArrayList<>();
        int row = myPosition.getRow() + rowChange;
        int col = myPosition.getColumn() + colChange;


        ChessPosition move = new ChessPosition(row, col);

        while (inBounds(move) && board.getPiece(move) == null) {
            movesPossible.add(new ChessMove(myPosition, move, null));
            row = row + rowChange;
            col = col + colChange;
            move = new ChessPosition(row, col);
        }

        if (inBounds(move)) {
            if (board.getPiece(move) != null) {
                if (getTeamColor() != board.getPiece(move).getTeamColor()) {
                    movesPossible.add(new ChessMove(myPosition, move, null));
                }
            }
        }

        return movesPossible;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);

        List<ChessMove> movesPossible = new ArrayList<>();

        if (piece.getPieceType() == PieceType.BISHOP) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            ChessPosition newPosition = new ChessPosition(row, col);

            //up up

            movesPossible.addAll(slide(board, myPosition, 1, 1));

//            row = myPosition.getRow() + 1;
//            col = myPosition.getColumn() + 1;
//            newPosition = new ChessPosition(row, col);
//
//            while (valid(newPosition, board)) {
//                if (board.getPiece(newPosition) != null) {
//                    if (getTeamColor() != board.getPiece(newPosition).getTeamColor()) {
//                        movesPossible.add(new ChessMove(myPosition, newPosition, null));
//                    }
//                    break;
//                }
//                row++;
//                col++;
//                movesPossible.add(new ChessMove(myPosition, newPosition, null));
//                newPosition = new ChessPosition(row, col);
//            }

            // up down
            row = myPosition.getRow() + 1;
            col = myPosition.getColumn() - 1;
            newPosition = new ChessPosition(row, col);

            while (valid(newPosition, board)) {
                if (board.getPiece(newPosition) != null) {
                    if (getTeamColor() != board.getPiece(newPosition).getTeamColor()) {
                        movesPossible.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }
                row++;
                col--;
                movesPossible.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(row, col);
            }
            // down up
            row = myPosition.getRow() - 1;
            col = myPosition.getColumn() + 1;
            newPosition = new ChessPosition(row, col);

            while (valid(newPosition, board)) {
                if (board.getPiece(newPosition) != null) {
                    if (getTeamColor() != board.getPiece(newPosition).getTeamColor()) {
                        movesPossible.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }
                row--;
                col++;
                movesPossible.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(row, col);
            }
            // down down
            row = myPosition.getRow() - 1;
            col = myPosition.getColumn() - 1;
            newPosition = new ChessPosition(row, col);

            while (valid(newPosition, board)) {
                if (board.getPiece(newPosition) != null) {
                    if (getTeamColor() != board.getPiece(newPosition).getTeamColor()) {
                        movesPossible.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }
                row--;
                col--;
                movesPossible.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(row, col);
            }

            return movesPossible;
        }

        if (piece.getPieceType() == PieceType.ROOK) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            ChessPosition newPosition = new ChessPosition(row, col);

            //up
            row = myPosition.getRow() + 1;
            col = myPosition.getColumn();
            newPosition = new ChessPosition(row, col);

            while (valid(newPosition, board)) {
                if (board.getPiece(newPosition) != null) {
                    if (getTeamColor() != board.getPiece(newPosition).getTeamColor()) {
                        movesPossible.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }
                row++;
                movesPossible.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(row, col);
            }

            // down
            row = myPosition.getRow() - 1;
            col = myPosition.getColumn();
            newPosition = new ChessPosition(row, col);

            while (valid(newPosition, board)) {
                if (board.getPiece(newPosition) != null) {
                    if (getTeamColor() != board.getPiece(newPosition).getTeamColor()) {
                        movesPossible.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }
                row--;
                movesPossible.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(row, col);
            }
            // left
            row = myPosition.getRow();
            col = myPosition.getColumn() - 1;
            newPosition = new ChessPosition(row, col);

            while (valid(newPosition, board)) {
                if (board.getPiece(newPosition) != null) {
                    if (getTeamColor() != board.getPiece(newPosition).getTeamColor()) {
                        movesPossible.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }
                col--;
                movesPossible.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(row, col);
            }
            // right
            row = myPosition.getRow();
            col = myPosition.getColumn() + 1;
            newPosition = new ChessPosition(row, col);

            while (valid(newPosition, board)) {
                if (board.getPiece(newPosition) != null) {
                    if (getTeamColor() != board.getPiece(newPosition).getTeamColor()) {
                        movesPossible.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }
                col++;
                movesPossible.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(row, col);
            }

            return movesPossible;
        }

        if (piece.getPieceType() == PieceType.QUEEN) {
            return movesPossible;
        }

        if (piece.getPieceType() == PieceType.KNIGHT) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();

            List<ChessPosition> knightMoves = List.of(
                    new ChessPosition(row + 2, col + 1),
                    new ChessPosition(row + 2, col - 1),
                    new ChessPosition(row + 1, col + 2),
                    new ChessPosition(row + 1, col - 2),
                    new ChessPosition(row - 1, col + 2),
                    new ChessPosition(row - 1, col - 2),
                    new ChessPosition(row - 2, col + 1),
                    new ChessPosition(row - 2, col - 1)
            );

            //if out of bounds don't add
            //if not empty: if it doesn't match add else don't add
            //else (if empty or doesn't match me) add

            for (ChessPosition move : knightMoves) {
                if (move.getRow() > 8 || move.getRow() < 1 || move.getColumn() > 8 || move.getColumn() < 1) {
                    continue;
                }
                else if (board.getPiece(move) != null) {
                    if (getTeamColor() != board.getPiece(move).getTeamColor()) {
                        movesPossible.add(new ChessMove(myPosition, move, null));
                    }
                }
                else {
                    movesPossible.add(new ChessMove(myPosition, move, null));
                }
            }
            return movesPossible;
        }
        return movesPossible;
    }
}
