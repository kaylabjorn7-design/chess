package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlAuthDAO implements AuthDAO {

    public AuthData findAuth(String token) throws DataAccessException {
        var sql = "SELECT * FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, token);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String authToken = resultSet.getString("authToken");
                    String username = resultSet.getString("username");
                    return new AuthData(authToken, username);
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to get authToken", ex);
        }
        return null;
    }

    public void createAuth(AuthData data) throws DataAccessException {
        var sql = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, data.authToken());
                preparedStatement.setString(2, data.username());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to create authToken", ex);
        }
    }

    public void deleteAuth(String token) throws DataAccessException {
        var sql = "DELETE FROM auth WHERE authToken = ? ";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, token);
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to delete authToken", ex);
        }
    }

    public void clear() throws DataAccessException {
        var sql = "DELETE FROM auth";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to clear", ex);
        }
    }
}
