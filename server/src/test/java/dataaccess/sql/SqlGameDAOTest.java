package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SqlGameDAOTest {
    SqlGameDAO sqlGameDAO = new SqlGameDAO();

    @BeforeEach
    public void setUp() throws DataAccessException {
        sqlGameDAO.clear();
    }

    @Test
    public void getGameSuccess() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(0, "Alice", "Paul", "ChessGame", game);
        int gameID = sqlGameDAO.createGame(gameData);
        GameData testData = sqlGameDAO.getGame(gameID);

        assertNotNull(testData);
        assertEquals(gameID, testData.gameID());
        assertEquals("Alice", testData.whiteUsername());
        assertEquals("Paul", testData.blackUsername());
        assertEquals("ChessGame", testData.gameName());
        assertEquals(game, testData.game());
    }
}
