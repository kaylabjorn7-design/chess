package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class ChessBoardRenderer {
    private ChessBoard board;
    private ChessGame.TeamColor perspective;

    private static final String LIGHT_SQUARE = "\u001B[48;2;133;141;152m";
    private static final String DARK_SQUARE  = "\u001B[48;2;93;101;113m";

    private static final String WHITE_PIECE = "\u001B[38;2;231;235;240m";
    private static final String BLACK_PIECE = "\u001B[38;2;23;27;33m";

    int rowStart;
    int rowStep;
    int colStart;
    int colStep;

    public void drawBoard(ChessBoard board, ChessGame.TeamColor perspective) {
        if (perspective == ChessGame.TeamColor.WHITE) {
            rowStart = 8;
            rowStep = -1;
            colStart = 1;
            colStep = 1;
        }
        else if (perspective == ChessGame.TeamColor.BLACK) {
            rowStart = 1;
            rowStep = 1;
            colStart = 8;
            colStep = -1;
        }

        for (int i = 0; i < 8; i++) {
            int row = rowStart + i * rowStep;
            System.out.print(row + " ");
            for (int j = 0; j < 8; j++) {
                int col = colStart + j * colStep;
                if ((row + col) % 2 == 1) {
                    System.out.print(LIGHT_SQUARE);
                }
                else {
                    System.out.print(DARK_SQUARE);
                }
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece != null) {
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        System.out.print(WHITE_PIECE);
                    }
                    else {
                        System.out.print(BLACK_PIECE);
                    }
                }

                System.out.print(getPieceSymbol(piece));
            }
            System.out.print("\u001B[0m");
            System.out.println();
        }
        System.out.print("  ");
        for (int j = 0; j < 8; j++) {
            int col = colStart + j * colStep;
            char letter = (char) ('a' + col - 1);
            System.out.print(" " + letter + "\u2003");
        }
        System.out.println();
    }

    private String getPieceSymbol(ChessPiece piece) {
        if (piece == null) {
            return " \u2003 ";
        }
        return switch (piece.getPieceType()) {
            case ChessPiece.PieceType.KING -> " ♚ ";
            case ChessPiece.PieceType.QUEEN -> " ♛ ";
            case ChessPiece.PieceType.ROOK -> " ♜ ";
            case ChessPiece.PieceType.BISHOP -> " ♝ ";
            case ChessPiece.PieceType.KNIGHT -> " ♞ ";
            case ChessPiece.PieceType.PAWN -> 	" ♟ ";
        };
    }
}
