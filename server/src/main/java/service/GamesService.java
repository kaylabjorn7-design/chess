package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dto.CreateGameRequest;
import dto.CreateGameResult;
import model.GameData;

import java.util.Collection;

public class GamesService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GamesService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public Collection<GameData> listGames(String authToken)
            throws DataAccessException, UnauthorizedException {
        if (authDAO.findAuth(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return gameDAO.listGames();
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest createGameRequest)
            throws DataAccessException, UnauthorizedException, BadRequestException {
        if (authDAO.findAuth(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        if (createGameRequest == null ||
                createGameRequest.gameName() == null ||
                createGameRequest.gameName().isBlank()) {
            throw new BadRequestException("Error: bad request");
        }
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(0, null, null, createGameRequest.gameName(), game);
        return new CreateGameResult(gameDAO.createGame(gameData));
    }
}
