package client;

import dto.RegisterRequest;
import dto.RegisterResult;
import org.junit.jupiter.api.*;
import server.Server;
import service.AlreadyTakenException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertThrows;


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
}
