package server;

import com.google.gson.Gson;
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

        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

        // Register your endpoints and exception handlers here.

        //Clear
        ClearService clearService = new ClearService(memoryUserDAO, memoryAuthDAO, memoryGameDAO);
        ClearHandler clearHandler = new ClearHandler(clearService);
        javalin.delete("/db", clearHandler::handleClear);

        //register, login, logout
        UserService userService = new UserService(memoryUserDAO, memoryAuthDAO);
        UserHandler userHandler = new UserHandler(userService);
        javalin.post("/user", userHandler::handleRegister);
        javalin.post("/session", userHandler::handleLogin);
        javalin.delete("/session", userHandler::handleLogout);

        //List games
        GamesService gamesService = new GamesService(memoryGameDAO, memoryAuthDAO);
        GamesHandler gamesHandler = new GamesHandler(gamesService);
        javalin.get("/game", gamesHandler::handleListGames);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
