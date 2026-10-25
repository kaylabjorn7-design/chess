package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private final HashMap<Integer, GameData> gameDataMap = new HashMap<>();
    private int gameCount = 1;

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return gameDataMap.values();
    }

    @Override
    public int createGame(GameData data) throws DataAccessException {
        int gameID = gameCount;
        gameCount++;
        GameData newData = new GameData(gameID, data.whiteUsername(), data.blackUsername(), data.gameName(), data.game());
        gameDataMap.put(gameID, newData);
        return gameID;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return gameDataMap.get(gameID);
    }

    @Override
    public void updateGame(GameData data) throws DataAccessException {
        if (!gameDataMap.containsKey(data.gameID())) {
            throw new DataAccessException("Game does not exist");
        }
        gameDataMap.put(data.gameID(), data);
    }

    @Override
    public void clear() throws DataAccessException{
        gameDataMap.clear();
        gameCount = 1;
    }
}
