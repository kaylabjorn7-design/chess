package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] squares = new ChessPiece[8][8];

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */

    public void createbackLine(int row) {
        ChessPiece rook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPosition position1 = new ChessPosition(row, 1);
        this.addPiece(position1, rook);
        ChessPiece rook1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPosition position2 = new ChessPosition(row, 8);
        this.addPiece(position2, rook1);
        ChessPiece knight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPosition position3 = new ChessPosition(row, 2);
        this.addPiece(position3, knight);
        ChessPiece knight1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPosition position4 = new ChessPosition(row, 7);
        this.addPiece(position4, knight1);
        ChessPiece bishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPosition position5 = new ChessPosition(row, 3);
        this.addPiece(position5, bishop);
        ChessPiece bishop1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPosition position6 = new ChessPosition(row, 6);
        this.addPiece(position6, bishop1);
        ChessPiece queen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPosition position7 = new ChessPosition(row, 4);
        this.addPiece(position7, queen);
        ChessPiece king = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPosition position8 = new ChessPosition(row, 5);
        this.addPiece(position8, king);
    }

    public void resetBoard() {
        //clear board
        squares = new ChessPiece[8][8];

        createbackLine(1);

        int col = 1;
        while (col <= 8) {
            ChessPiece pawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            ChessPosition position = new ChessPosition(2, col);
            this.addPiece(position, pawn);
            col++;
        }
        //createbackLine(color, row)
        //back line on row 1 and 8
        //pawns on row 2 and 7

        int row = 8;

        ChessPiece brook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPosition position9 = new ChessPosition(row, 1);
        this.addPiece(position9, brook);
        ChessPiece brook1 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPosition position10 = new ChessPosition(row, 8);
        this.addPiece(position10, brook1);
        ChessPiece bknight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPosition position11 = new ChessPosition(row, 2);
        this.addPiece(position11, bknight);
        ChessPiece bknight1 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPosition position12 = new ChessPosition(row, 7);
        this.addPiece(position12, bknight1);
        ChessPiece bbishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPosition position13 = new ChessPosition(row, 3);
        this.addPiece(position13, bbishop);
        ChessPiece bbishop1 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPosition position14 = new ChessPosition(row, 6);
        this.addPiece(position14, bbishop1);
        ChessPiece bqueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        ChessPosition position15 = new ChessPosition(row, 4);
        this.addPiece(position15, bqueen);
        ChessPiece bking = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        ChessPosition position16 = new ChessPosition(row, 5);
        this.addPiece(position16, bking);

        int bcol = 1;
        while (bcol <= 8) {
            ChessPiece pawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            ChessPosition position = new ChessPosition(7, bcol);
            this.addPiece(position, pawn);
            bcol++;
        }
    }
}
