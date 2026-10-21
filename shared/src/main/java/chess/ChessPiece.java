package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

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

    public boolean inBounds(ChessPosition move) {
        return (move.getRow() >= 1 && move.getRow() <= 8 && move.getColumn() >= 1 && move.getColumn() <=8);
    }

    public boolean enemy(ChessBoard board, ChessPosition myPosition, ChessPosition move) {
        return board.getPiece(myPosition).getTeamColor() != board.getPiece(move).getTeamColor();
    }

    public Collection<ChessMove> slide(ChessBoard board, ChessPosition myPosition, int rowChange, int colChange) {
        List<ChessMove> slideMoves = new ArrayList<>();

        int row = myPosition.getRow() + rowChange;
        int col = myPosition.getColumn() + colChange;

        ChessPosition move = new ChessPosition(row, col);

        while (inBounds(move)) {
            if (board.getPiece(move) == null) {
                slideMoves.add(new ChessMove(myPosition, move, null));
                row = row + rowChange;
                col = col + colChange;
                move = new ChessPosition(row, col);
            }
            else {
                if (enemy(board, myPosition, move)) {
                    slideMoves.add(new ChessMove(myPosition, move, null));
                    return slideMoves;
                }
                else {
                    return slideMoves;
                }
            }
        }

        return slideMoves;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> movesPossible = new ArrayList<>();

        if (board.getPiece(myPosition).getPieceType() == PieceType.BISHOP) {
            movesPossible.addAll(slide(board, myPosition,1, 1));
            movesPossible.addAll(slide(board, myPosition,1, -1));
            movesPossible.addAll(slide(board, myPosition,-1, 1));
            movesPossible.addAll(slide(board, myPosition,-1, -1));
        }

        if (board.getPiece(myPosition).getPieceType() == PieceType.ROOK) {
            movesPossible.addAll(slide(board, myPosition,1, 0));
            movesPossible.addAll(slide(board, myPosition,-1, 0));
            movesPossible.addAll(slide(board, myPosition,0, 1));
            movesPossible.addAll(slide(board, myPosition,0, -1));
        }

        if (board.getPiece(myPosition).getPieceType() == PieceType.QUEEN) {
            movesPossible.addAll(slide(board, myPosition,1, 1));
            movesPossible.addAll(slide(board, myPosition,1, -1));
            movesPossible.addAll(slide(board, myPosition,-1, 1));
            movesPossible.addAll(slide(board, myPosition,-1, -1));
            movesPossible.addAll(slide(board, myPosition,1, 0));
            movesPossible.addAll(slide(board, myPosition,-1, 0));
            movesPossible.addAll(slide(board, myPosition,0, 1));
            movesPossible.addAll(slide(board, myPosition,0, -1));
        }

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        if (board.getPiece(myPosition).getPieceType() == PieceType.KING) {
            List<ChessPosition> kingMoves = List.of(
                    new ChessPosition(row + 1, col + 1),
                    new ChessPosition(row + 1, col - 1),
                    new ChessPosition(row + 1, col),
                    new ChessPosition(row - 1, col + 1),
                    new ChessPosition(row - 1, col - 1),
                    new ChessPosition(row - 1, col),
                    new ChessPosition(row, col + 1),
                    new ChessPosition(row, col - 1)
            );

            for (ChessPosition move : kingMoves) {
                if (inBounds(move) && board.getPiece(move) == null) {
                    movesPossible.add(new ChessMove(myPosition, move, null));
                }
                else {
                    if (inBounds(move) && enemy(board, myPosition, move)) {
                        movesPossible.add(new ChessMove(myPosition, move, null));
                    }
                }
            }
        }

        if (board.getPiece(myPosition).getPieceType() == PieceType.KNIGHT) {
            List<ChessPosition> knightMoves = List.of(
                    new ChessPosition(row + 2, col + 1),
                    new ChessPosition(row + 2, col - 1),
                    new ChessPosition(row + 1, col + 2),
                    new ChessPosition(row + 1, col - 2),
                    new ChessPosition(row - 2, col + 1),
                    new ChessPosition(row - 2, col - 1),
                    new ChessPosition(row - 1, col + 2),
                    new ChessPosition(row - 1, col - 2)
            );

            for (ChessPosition move : knightMoves) {
                if (inBounds(move) && board.getPiece(move) == null) {
                    movesPossible.add(new ChessMove(myPosition, move, null));
                }
                else {
                    if (inBounds(move) && enemy(board, myPosition, move)) {
                        movesPossible.add(new ChessMove(myPosition, move, null));
                    }
                }
            }
        }

        if (board.getPiece(myPosition).getPieceType() == PieceType.PAWN) {
            //White
            //forward 1
            if (getTeamColor() == WHITE) {
                ChessPosition move = new ChessPosition(row + 1, col);
                if (inBounds(move) && board.getPiece(move) == null) {
                    if (row == 7) {
                        movesPossible.addAll(promote(myPosition, move));;
                    }
                    else {
                        movesPossible.add(new ChessMove(myPosition, move, null));
                    }
                    //forward 2
                    if(row == 2) {
                        move = new ChessPosition(row + 2, col);
                        if (inBounds(move) && board.getPiece(move) == null) {
                            movesPossible.add(new ChessMove(myPosition, move, null));
                        }
                    }
                }
                //capture left
                move = new ChessPosition(row + 1, col - 1);
                if (inBounds(move) && board.getPiece(move) != null) {
                    if (enemy(board, myPosition, move)) {
                        if (row == 7) {
                            movesPossible.addAll(promote(myPosition, move));;
                        }
                        else {
                            movesPossible.add(new ChessMove(myPosition, move, null));
                        }
                    }
                }
                //capture right
                move = new ChessPosition(row + 1, col + 1);
                if (inBounds(move) && board.getPiece(move) != null) {
                    if (enemy(board, myPosition, move)) {
                        if (row == 7) {
                            movesPossible.addAll(promote(myPosition, move));
                        }
                        else {
                            movesPossible.add(new ChessMove(myPosition, move, null));
                        }
                    }
                }
            }
            //Black
            if (getTeamColor() == BLACK) {
                //forward 1
                ChessPosition move = new ChessPosition(row - 1, col);
                if (inBounds(move) && board.getPiece(move) == null) {
                    if (row == 2) {
                        movesPossible.addAll(promote(myPosition, move));;
                    }
                    else {
                        movesPossible.add(new ChessMove(myPosition, move, null));
                    }
                    //forward 2
                    if(row == 7) {
                        move = new ChessPosition(row - 2, col);
                        if (inBounds(move) && board.getPiece(move) == null) {
                            movesPossible.add(new ChessMove(myPosition, move, null));
                        }
                    }
                }
                //capture left
                move = new ChessPosition(row - 1, col + 1);
                if (inBounds(move) && board.getPiece(move) != null) {
                    if (enemy(board, myPosition, move)) {
                        if (row == 2) {
                            movesPossible.addAll(promote(myPosition, move));;
                        }
                        else {
                            movesPossible.add(new ChessMove(myPosition, move, null));
                        }
                    }
                }
                //capture right
                move = new ChessPosition(row - 1, col - 1);
                if (inBounds(move) && board.getPiece(move) != null) {
                    if (enemy(board, myPosition, move)) {
                        if (row == 2) {
                            movesPossible.addAll(promote(myPosition, move));
                        }
                        else {
                            movesPossible.add(new ChessMove(myPosition, move, null));
                        }
                    }
                }
            }
        }

        return movesPossible;
    }

    private Collection<ChessMove> promote(ChessPosition myPosition, ChessPosition move) {
        List<ChessMove> promotions = new ArrayList<>();
        promotions.add(new ChessMove(myPosition, move, PieceType.KNIGHT));
        promotions.add(new ChessMove(myPosition, move, PieceType.BISHOP));
        promotions.add(new ChessMove(myPosition, move, PieceType.ROOK));
        promotions.add(new ChessMove(myPosition, move, PieceType.QUEEN));

        return promotions;
    }
}