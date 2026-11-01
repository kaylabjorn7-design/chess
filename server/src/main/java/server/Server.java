package server;

import dataaccess.sql.SqlAuthDAO;
import dataaccess.sql.SqlGameDAO;
import dataaccess.sql.SqlUserDAO;
import io.javalin.json.JavalinGson;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import io.javalin.*;
import service.ClearService;
import service.GamesService;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> {
            config.staticFiles.add("web");
            config.jsonMapper(new JavalinGson());
        });

        SqlUserDAO sqlUserDAO = new SqlUserDAO();
        SqlAuthDAO sqlAuthDAO = new SqlAuthDAO();
        SqlGameDAO sqlGameDAO = new SqlGameDAO();

        // Register your endpoints and exception handlers here.

        //Clear
        ClearService clearService = new ClearService(sqlUserDAO, sqlAuthDAO, sqlGameDAO);
        ClearHandler clearHandler = new ClearHandler(clearService);
        javalin.delete("/db", clearHandler::handleClear);

        //register, login, logout
        UserService userService = new UserService(sqlUserDAO, sqlAuthDAO);
        UserHandler userHandler = new UserHandler(userService);
        javalin.post("/user", userHandler::handleRegister);
        javalin.post("/session", userHandler::handleLogin);
        javalin.delete("/session", userHandler::handleLogout);

        //list games, create game, join game
        GamesService gamesService = new GamesService(sqlGameDAO, sqlAuthDAO);
        GamesHandler gamesHandler = new GamesHandler(gamesService);
        javalin.get("/game", gamesHandler::handleListGames);
        javalin.post("/game", gamesHandler::handleCreateGame);
        javalin.put("/game", gamesHandler::handleJoinGame);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
