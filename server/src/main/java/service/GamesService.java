package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
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
}
