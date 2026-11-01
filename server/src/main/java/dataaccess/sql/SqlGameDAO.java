package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import static javax.swing.UIManager.getInt;

public class SqlGameDAO implements GameDAO {

    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> games = new ArrayList<>();

        var sql = "SELECT * FROM games";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                Gson gson = new Gson();

                ResultSet resultSet = preparedStatement.executeQuery();


                while (resultSet.next()) {
                    int gameID = resultSet.getInt("gameID");
                    String whiteUsername = resultSet.getString("whiteUsername");
                    String blackUsername = resultSet.getString("blackUsername");
                    String gameName = resultSet.getString("gameName");

                    games.add(new GameData(
                            gameID, whiteUsername, blackUsername, gameName,
                            gson.fromJson(resultSet.getString("game"), ChessGame.class)));
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to list games", ex);
        }

        return games;
    }

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

    public GameData getGame(int gameID) throws DataAccessException {
        var sql = "SELECT * FROM games WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, gameID);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    Gson gson = new Gson();

                    String whiteUsername = resultSet.getString("whiteUsername");
                    String blackUsername = resultSet.getString("blackUsername");
                    String gameName = resultSet.getString("gameName");

                    return new GameData(
                            gameID, whiteUsername, blackUsername, gameName,
                            gson.fromJson(resultSet.getString("game"), ChessGame.class));
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to get game", ex);
        }
        return null;
    }

    public void updateGame(GameData data) throws DataAccessException {
        var sql = "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {

                Gson gson = new Gson();
                String json = gson.toJson(data.game());

                preparedStatement.setString(1, data.whiteUsername());
                preparedStatement.setString(2, data.blackUsername());
                preparedStatement.setString(3, data.gameName());
                preparedStatement.setString(4, json);
                preparedStatement.setInt(5, data.gameID());
                int updateInt = preparedStatement.executeUpdate();
                if (updateInt == 0) {
                    throw new DataAccessException("Game does not exist");
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to update", ex);
        }
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
