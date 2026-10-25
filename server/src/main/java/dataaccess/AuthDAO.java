package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData findAuth(String token) throws DataAccessException;

    void createAuth(AuthData data) throws DataAccessException;

    void deleteAuth(String token) throws DataAccessException;

    void clear() throws DataAccessException;
}
