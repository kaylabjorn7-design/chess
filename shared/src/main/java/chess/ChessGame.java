package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard currentBoard;
    TeamColor currentTurn;

    public ChessGame() {
        currentBoard = new ChessBoard();
        currentBoard.resetBoard();
        setTeamTurn(TeamColor.WHITE);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(currentBoard, chessGame.currentBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(currentBoard);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        if (team == TeamColor.WHITE) {
            currentTurn = TeamColor.WHITE;
        }
        else {
            currentTurn = TeamColor.BLACK;
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessBoard realBoard = getBoard();

        if (realBoard.getPiece(startPosition) == null) {
            return null;
        }

        Collection<ChessMove> validMoves = realBoard.getPiece(startPosition).pieceMoves(realBoard, startPosition);
        Collection<ChessMove> removeMoves = new ArrayList<>();

        for (ChessMove move : validMoves) {
            currentBoard = realBoard;
            ChessBoard testBoard = new ChessBoard();

            //copy real board to testBoard
            for (int r = 1; r <= 8; r++) {
                for (int c = 1; c <= 8; c++) {
                    ChessPosition position = new ChessPosition(r, c);
                    ChessPiece piece = realBoard.getPiece(position);
                    testBoard.addPiece(position, piece);
                }
            }
            //make hypothetical move on testBoard
            currentBoard = testBoard;
            ChessPiece myPiece = testBoard.getPiece(move.getStartPosition());
            testBoard.addPiece(move.getEndPosition(), myPiece);
            testBoard.addPiece(move.getStartPosition(), null);

            //if king is in check remove move from validMoves
            if(isInCheck(myPiece.getTeamColor())) {
                removeMoves.add(move);

            }
        }
        currentBoard = realBoard;

        for (ChessMove move : removeMoves) {
            validMoves.remove(move);
        }

        return validMoves;
    }

    boolean validMove(ChessMove move) {
        ChessPosition movePosition = move.getEndPosition();
        if (validMoves(movePosition) == null) {
            return false;
        }
        for (ChessMove _move : validMoves(movePosition)) {
            if (_move == move) {
                return true;
            }
        }

        return false;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece myPiece = getBoard().getPiece(move.getStartPosition());
        if (validMove(move)) {
            getBoard().addPiece(move.getEndPosition(), myPiece);
            getBoard().addPiece(move.getStartPosition(), null);
        }
        else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        //for piece on board that is !teamColor
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition position = new ChessPosition(r, c);
                ChessPiece piece = getBoard().getPiece(position);
                if (piece != null) {
                    if (piece.getTeamColor() != teamColor) {
                        //for move in possible moves
                        Collection<ChessMove> possibleMoves = piece.pieceMoves(getBoard(), position);
                        for (ChessMove move : possibleMoves) {
                            // if ChessPiece at endPosition is king
                            if (getBoard().getPiece(move.getEndPosition()) != null) {
                                if (getBoard().getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING) {
                                    //team is in check
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            for (int r = 1; r <= 8; r++) {
                for (int c = 1; c <= 8; c++) {
                    ChessPosition position = new ChessPosition(r, c);
                    ChessPiece piece = getBoard().getPiece(position);
                    if (piece != null) {
                        if (piece.getTeamColor() == teamColor) {
                            if (!validMoves(position).isEmpty()) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        currentBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currentBoard;
    }
}
