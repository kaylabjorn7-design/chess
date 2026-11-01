package dataaccess.sql;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static javax.swing.UIManager.getInt;

public class SqlGameDAO {

    public int createGame(GameData data) throws DataAccessException {
        var sql = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                Gson gson = new Gson();
                String json = gson.toJson(data.game());

                preparedStatement.setString(1, data.whiteUsername());
                preparedStatement.setString(2, data.blackUsername());
                preparedStatement.setString(3, data.gameName());
                preparedStatement.setString(4, json);
                preparedStatement.executeUpdate();

                ResultSet resultSet = preparedStatement.getGeneratedKeys();

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to create game", ex);
        }
        throw new DataAccessException("no gameID returned");
    }

    public void clear() throws DataAccessException {
        var sql = "DELETE FROM games";
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
