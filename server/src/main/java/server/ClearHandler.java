package server;

import dataaccess.DataAccessException;
import service.ClearService;

import io.javalin.http.Context;

import java.util.Map;

public class ClearHandler {
    private final ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public void handleClear(Context ctx) {
        try {
            clearService.clear();
            ctx.status(200);
            ctx.result("{}");
        }
        catch (DataAccessException e) {
            ctx.status(500);
            ctx.json(Map.of("message","Error: database failure"));
        }
    }
}
