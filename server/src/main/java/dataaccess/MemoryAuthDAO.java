package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> authDataMap = new HashMap<>();

    @Override
    public AuthData findAuth(String token) throws DataAccessException {
        return authDataMap.get(token);
    }

    @Override
    public void createAuth(AuthData data) throws DataAccessException {
        authDataMap.put(data.authToken(), data);
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {
        authDataMap.remove(token);
    }

    @Override
    public void clear() throws DataAccessException {
        authDataMap.clear();
    }
}
