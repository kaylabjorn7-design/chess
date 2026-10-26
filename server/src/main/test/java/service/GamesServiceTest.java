package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GamesServiceTest {
    @Test
    void listGamesSuccess() throws DataAccessException, UnauthorizedException {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        AuthData testAuth = new AuthData("133d332d-693e-4379-8644-2e59b07399ec", "Alice");
        authDAO.createAuth(testAuth);

        ChessGame game = new ChessGame();
        GameData testGame = new GameData(0, "alice", "Paul", "ChessGame", game);
        int gameID = gameDAO.createGame(testGame);

        GamesService gamesService = new GamesService(gameDAO, authDAO);

        Collection<GameData> games = gamesService.listGames(testAuth.authToken());

        assertEquals(1, games.size());
        assertEquals(gameID, games.iterator().next().gameID());
        assertEquals("ChessGame", games.iterator().next().gameName());
    }

    @Test
    void listGamesInvalidToken() throws DataAccessException {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        GamesService gamesService = new GamesService(gameDAO, authDAO);

        AuthData testAuth = new AuthData("133d332d-693e-4379-8644-2e59b07399ec", "Alice");
        authDAO.createAuth(testAuth);

        assertThrows(UnauthorizedException.class, ()-> gamesService.listGames("233d332d-693e-4379-8644-2e59b07399ec"));
    }
}
