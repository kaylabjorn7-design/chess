package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClearServiceTest {

    @Test
    void success() throws DataAccessException {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        UserData testUser = new UserData("Alice", "password123", "alice@byu.edu");
        userDAO.createUser(testUser);

        AuthData testAuth = new AuthData("133d332d-693e-4379-8644-2e59b07399ec", "Alice");
        authDAO.createAuth(testAuth);

        ChessGame game = new ChessGame();
        GameData testGame = new GameData(0, "alice", "Paul", "ChessGame", game);
        gameDAO.createGame(testGame);

        ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);

        clearService.clear();

        assertNull(userDAO.getUser("Alice"));
        assertNull(authDAO.findAuth("133d332d-693e-4379-8644-2e59b07399ec"));
        assertTrue(gameDAO.listGames().isEmpty());
    }
}
