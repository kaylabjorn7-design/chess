package server;

import chess.*;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;

public class ServerMain {
    public static void main(String[] args) throws DataAccessException {
        var port = 8080;
        Server server = new Server();
        DatabaseManager.createDatabase();
        server.run(port);

        System.out.println("♕ 240 Chess Server running on port " + port);
    }
}