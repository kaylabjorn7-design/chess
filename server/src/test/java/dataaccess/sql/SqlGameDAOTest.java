package dataaccess.sql;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import javax.xml.crypto.Data;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void getGameFailure() throws DataAccessException {
        assertNull(sqlGameDAO.getGame(1));
    }

    @Test
    public void createGameSuccess() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(0, "Alice", "Paul", "ChessGame", game);
        int gameID = sqlGameDAO.createGame(gameData);

        assertTrue(gameID > 0);
        assertNotNull(sqlGameDAO.getGame(gameID));
    }

    @Test
    public void listGamesSuccess() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(0, "Alice", "Paul", "ChessGame", game);
        int gameID = sqlGameDAO.createGame(gameData);
        GameData testData = sqlGameDAO.getGame(gameID);

        ChessGame game1 = new ChessGame();
        GameData gameData1 = new GameData(0, "Olivia", "Ben", "ChessGame1", game1);
        int gameID1 = sqlGameDAO.createGame(gameData1);
        GameData testData1 = sqlGameDAO.getGame(gameID1);

        Collection<GameData> games = sqlGameDAO.listGames();

        assertEquals(2, games.size());
        assertTrue(games.contains(testData));
        assertTrue(games.contains(testData1));
    }

    @Test
    public void listGamesEmpty() throws DataAccessException {
        assertTrue(sqlGameDAO.listGames().isEmpty());
    }

    @Test
    public void updateGameSuccess() throws DataAccessException, InvalidMoveException {
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(0, null, null, "ChessGame", game);
        int gameID = sqlGameDAO.createGame(gameData);

        sqlGameDAO.updateGame(new GameData(gameID, "Alice", "Paul", "ChessGame", game));
        GameData testData = sqlGameDAO.getGame(gameID);

        assertEquals("Alice", testData.whiteUsername());
        assertEquals("Paul", testData.blackUsername());

        game.makeMove(new ChessMove(
                new ChessPosition(2, 5),
                new ChessPosition(4, 5),
                null
                ));

        sqlGameDAO.updateGame(new GameData(gameID, "Alice", "Paul", "ChessGame", game));
        GameData updatedTestData = sqlGameDAO.getGame(gameID);

        assertEquals(game, updatedTestData.game());
    }

    @Test
    public void updateGameFailure() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(1, "Alice", "Paul", "ChessGame", game);
        assertThrows(DataAccessException.class, () -> sqlGameDAO.updateGame(gameData));
    }

    @Test
    public void clearSuccess() throws DataAccessException {
        ChessGame game = new ChessGame();
        int GameID = sqlGameDAO.createGame(new GameData(0, "Alice", "Paul", "ChessGame", game));
        ChessGame game1 = new ChessGame();
        int GameID1 = sqlGameDAO.createGame(new GameData(0, "Olivia", "Ben", "ChessGame1", game1));

        assertNotNull(sqlGameDAO.getGame(GameID));
        assertNotNull(sqlGameDAO.getGame(GameID1));

        sqlGameDAO.clear();

        assertNull(sqlGameDAO.getGame(GameID));
        assertNull(sqlGameDAO.getGame(GameID1));
    }
}
