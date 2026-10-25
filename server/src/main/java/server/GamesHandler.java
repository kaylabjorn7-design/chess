package server;

import dataaccess.DataAccessException;
import dto.GameSummary;
import dto.ListGamesResult;
import io.javalin.http.Context;
import model.GameData;
import service.GamesService;
import service.UnauthorizedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class GamesHandler {
    private final GamesService gamesService;

    public GamesHandler(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    public void handleListGames(Context ctx) {
        try {
            String authToken = ctx.header("authorization");

            Collection<GameData> games = gamesService.listGames(authToken);

            ArrayList<GameSummary> summaries = new ArrayList<>();

            for (GameData game : games) {
                GameSummary gameSummary = new GameSummary(game.gameID(),
                        game.whiteUsername(),
                        game.blackUsername(),
                        game.gameName());
                summaries.add(gameSummary);

            }

            ListGamesResult listGamesResult = new ListGamesResult(summaries);

            ctx.status(200);
            ctx.json(listGamesResult);
        }
        catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.json(Map.of("message","Error: unauthorized"));
        }
        catch (DataAccessException e) {
            ctx.status(500);
            ctx.json(Map.of("message","Error: database failure"));
        }
    }
}

