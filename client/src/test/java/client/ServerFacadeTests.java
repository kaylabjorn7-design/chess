package client;

import dto.*;
import org.junit.jupiter.api.*;
import server.Server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static int port;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clearDataBase() throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/db"))
                .DELETE()
                .build();
        try (HttpClient client = HttpClient.newHttpClient()) {
            var response = client.send(request, HttpResponse.BodyHandlers.discarding());

            Assertions.assertEquals(200, response.statusCode());
        }
    }

    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerSuccess() throws IOException {
        RegisterRequest request = new RegisterRequest("Jane", "password123", "jane@byu.edu");
        RegisterResult result = facade.register(request);

        Assertions.assertEquals("Jane", result.username());
        Assertions.assertNotNull(result.authToken());
        Assertions.assertTrue(result.authToken().length() > 10);
    }

    //same username
    @Test
    public void registerFail() throws IOException {
        RegisterRequest request = new RegisterRequest("Jane", "password123", "jane@byu.edu");
        facade.register(request);

        assertThrows(IOException.class, ()-> facade.register(request));
    }

    @Test
    public void loginSuccess() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("Jane", "password123", "jane@byu.edu");
        facade.register(registerRequest);

        LoginRequest request = new LoginRequest("Jane", "password123");
        LoginResult result = facade.login(request);

        Assertions.assertEquals("Jane", result.username());
        Assertions.assertNotNull(result.authToken());
        Assertions.assertTrue(result.authToken().length() > 10);
    }

    @Test
    public void loginFail() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("Jane", "password123", "jane@byu.edu");
        facade.register(registerRequest);

        LoginRequest badRequest = new LoginRequest("Jane", "wrongPassword");

        assertThrows(IOException.class, ()-> facade.login(badRequest));
    }

    @Test
    public void logoutSuccess() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("Jane", "password123", "jane@byu.edu");
        String authToken = facade.register(registerRequest).authToken();

        Assertions.assertDoesNotThrow(() -> facade.logout(authToken));
    }

    //invalid token
    @Test
    public void logoutFail() {
        Assertions.assertThrows(IOException.class, ()-> facade.logout("InvalidAuthToken"));
    }

    @Test
    public void createGameSuccess() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("Jane", "password123", "jane@byu.edu");
        String authToken = facade.register(registerRequest).authToken();

        CreateGameRequest request = new CreateGameRequest("myGame");
        CreateGameResult result = facade.createGame(request, authToken);

        Assertions.assertTrue(result.gameID() > 0);

        ListGamesResult listGamesResult = facade.listGames(authToken);
        Collection<GameSummary> games = listGamesResult.games();

        Assertions.assertTrue(games.stream().anyMatch(
                game -> game.gameID() == result.gameID() &&
                game.gameName().equals("myGame")));
    }

    @Test
    public void createGameFail() {
        CreateGameRequest request = new CreateGameRequest("myGame");

        Assertions.assertThrows(IOException.class, ()-> facade.createGame(request, "InvalidAuthToken"));
    }

    @Test
    public void listGamesSuccess() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("Jane", "password123", "jane@byu.edu");
        String authToken = facade.register(registerRequest).authToken();

        facade.createGame(new CreateGameRequest("myGame"), authToken);
        facade.createGame(new CreateGameRequest("myGame1"), authToken);

        ListGamesResult result = facade.listGames(authToken);
        Collection<GameSummary> games = result.games();

        Assertions.assertEquals(2, games.size());
        assertTrue(games.stream().anyMatch(game -> game.gameName().equals("myGame")));
        assertTrue(games.stream().anyMatch(game -> game.gameName().equals("myGame1")));
    }

    //invalid authToken
    @Test
    public void listGamesFail() {
        Assertions.assertThrows(IOException.class, ()-> facade.listGames("InvalidAuthToken"));
    }

    @Test
    public void joinGameSuccess() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("Jane", "password123", "jane@byu.edu");
        String authToken = facade.register(registerRequest).authToken();

        int gameID = facade.createGame(new CreateGameRequest("myGame"), authToken).gameID();

        JoinGameRequest request = new JoinGameRequest("WHITE", gameID);
        facade.joinGame(request, authToken);

        ListGamesResult listGamesResult = facade.listGames(authToken);
        Collection<GameSummary> games = listGamesResult.games();

        Assertions.assertTrue(games.stream().anyMatch(game ->
                game.gameID() == gameID &&
                        "Jane".equals(game.whiteUsername())
        ));
    }

    //join with a filled color
    @Test
    public void joinGameFail() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest("Jane", "password123", "jane@byu.edu");
        String janeToken = facade.register(registerRequest).authToken();

        RegisterRequest registerRequest1 = new RegisterRequest("Paul", "password123", "paul@byu.edu");
        String paulToken = facade.register(registerRequest1).authToken();

        int gameID = facade.createGame(new CreateGameRequest("myGame"), janeToken).gameID();

        JoinGameRequest request = new JoinGameRequest("WHITE", gameID);
        facade.joinGame(request, janeToken);

        Assertions.assertThrows(IOException.class, ()-> facade.joinGame(request, paulToken));
    }
}
