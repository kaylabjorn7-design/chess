package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import dto.CreateGameRequest;
import dto.CreateGameResult;
import dto.JoinGameRequest;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void createGameSuccess() throws DataAccessException, BadRequestException, UnauthorizedException {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        AuthData testAuth = new AuthData("133d332d-693e-4379-8644-2e59b07399ec", "Alice");
        authDAO.createAuth(testAuth);

        GamesService gamesService = new GamesService(gameDAO, authDAO);

        CreateGameRequest createGameRequest = new CreateGameRequest("ChessGame");

        CreateGameResult createGameResult = gamesService.createGame(testAuth.authToken(), createGameRequest);
        GameData storedGame = gameDAO.getGame(createGameResult.gameID());

        assertNotNull(storedGame);
        assertEquals(createGameResult.gameID(), storedGame.gameID());
        assertEquals("ChessGame", storedGame.gameName());
        assertNull(storedGame.whiteUsername());
        assertNull(storedGame.blackUsername());
    }

    @Test
    void joinGameWhiteSuccess()
            throws DataAccessException, BadRequestException, UnauthorizedException, AlreadyTakenException {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        AuthData testAuth = new AuthData("133d332d-693e-4379-8644-2e59b07399ec", "Alice");
        authDAO.createAuth(testAuth);

        ChessGame game = new ChessGame();
        GameData testGame = new GameData(0, null, null, "ChessGame", game);
        int gameID = gameDAO.createGame(testGame);

        GamesService gamesService = new GamesService(gameDAO, authDAO);

        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", gameID);

        gamesService.joinGame(testAuth.authToken(), joinGameRequest);

        GameData testGameData = gameDAO.getGame(gameID);

        assertEquals("Alice", testGameData.whiteUsername());
        assertNull(testGameData.blackUsername());
    }

    @Test
    void joinGameBlackSuccess()
            throws DataAccessException, BadRequestException, UnauthorizedException, AlreadyTakenException {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        AuthData testAuth = new AuthData("133d332d-693e-4379-8644-2e59b07399ec", "Alice");
        authDAO.createAuth(testAuth);

        ChessGame game = new ChessGame();
        GameData testGame = new GameData(0, null, null, "ChessGame", game);
        int gameID = gameDAO.createGame(testGame);

        GamesService gamesService = new GamesService(gameDAO, authDAO);

        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", gameID);

        gamesService.joinGame(testAuth.authToken(), joinGameRequest);

        GameData testGameData = gameDAO.getGame(gameID);

        assertEquals("Alice", testGameData.blackUsername());
        assertNull(testGameData.whiteUsername());
    }

    @Test
    void joinGameColorTaken() {

    }
}
