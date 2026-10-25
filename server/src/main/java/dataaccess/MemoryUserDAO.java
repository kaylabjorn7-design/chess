package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private final HashMap<String, UserData> userDataMap = new HashMap<>();

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return userDataMap.get(username);
    }

    @Override
    public void createUser(UserData data) throws DataAccessException {
        userDataMap.put(data.username(), data);
    }

    @Override
    public void clear() throws DataAccessException {
        userDataMap.clear();
    }
}
