package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    Collection<GameData> listGames() throws DataAccessException;

    int createGame(GameData data) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void updateGame(GameData data) throws DataAccessException;

    void clear() throws DataAccessException;
}
