package server;

import dataaccess.DataAccessException;
import dto.RegisterRequest;
import dto.RegisterResult;
import io.javalin.http.Context;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.UserService;

import java.util.Map;

public class UserHandler {
    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public void handleRegister(Context ctx) {
        try {
            RegisterRequest registerRequest = ctx.bodyAsClass(RegisterRequest.class);
            RegisterResult registerResult = userService.register(registerRequest);


            ctx.status(200);
            ctx.json(registerResult);
        }
        catch (BadRequestException e) {
            ctx.status(400);
            ctx.json(Map.of("message","Error: bad request"));
        }
        catch (AlreadyTakenException e) {
            ctx.status(403);
            ctx.json(Map.of("message","Error: already taken"));
        }
        catch (DataAccessException e) {
            ctx.status(500);
            ctx.json(Map.of("message","Error: database failure"));
        }
    }
}
