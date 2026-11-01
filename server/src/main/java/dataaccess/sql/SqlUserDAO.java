package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class SqlUserDAO implements UserDAO {
    @Override
    public UserData getUser(String username) throws DataAccessException {
        var sql = "SELECT * FROM users WHERE username = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");
                    return new UserData(username, password, email);
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to get user", ex);
        }
        return null;
    }

    @Override
    public void createUser(UserData data) throws DataAccessException {
        var sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            String hashedPassword = BCrypt.hashpw(data.password(), BCrypt.gensalt());
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, data.username());
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, data.email());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to create user", ex);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var sql = "DELETE FROM users";
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
