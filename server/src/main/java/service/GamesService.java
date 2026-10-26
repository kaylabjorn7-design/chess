package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dto.CreateGameRequest;
import dto.CreateGameResult;
import dto.JoinGameRequest;
import model.AuthData;
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

    public void joinGame(String authToken, JoinGameRequest joinGameRequest)
            throws DataAccessException, BadRequestException, UnauthorizedException, AlreadyTakenException {

        AuthData authData = authDAO.findAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        if (joinGameRequest == null ||
                joinGameRequest.playerColor() == null ||
                joinGameRequest.playerColor().isBlank() ||
                !joinGameRequest.playerColor().equals("BLACK") && !joinGameRequest.playerColor().equals("WHITE")) {
            throw new BadRequestException("Error: bad request");
        }

        GameData gameRequestedData = gameDAO.getGame(joinGameRequest.gameID());
        if (gameRequestedData == null) {
            throw new BadRequestException("Error: bad request");
        }

        String whiteUsername = gameRequestedData.whiteUsername();
        String blackUsername = gameRequestedData.blackUsername();

        if (joinGameRequest.playerColor().equals("WHITE")) {
            if (gameRequestedData.whiteUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }
            whiteUsername = authData.username();
        }
        if (joinGameRequest.playerColor().equals("BLACK")) {
            if (gameRequestedData.blackUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }
            blackUsername = authData.username();
        }

        GameData newGameData = new GameData(gameRequestedData.gameID(),
                whiteUsername,
                blackUsername,
                gameRequestedData.gameName(),
                gameRequestedData.game());

        gameDAO.updateGame(newGameData);
    }
}
