package client;

import com.google.gson.Gson;
import dto.LoginRequest;
import dto.LoginResult;
import dto.RegisterRequest;
import dto.RegisterResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade (int portNumber) {
        serverUrl = "http://localhost:" + portNumber;
    }

    public RegisterResult register(RegisterRequest request) throws IOException {
        HttpRequest httpRequest = buildRequest("POST", "/user", request);
        HttpResponse<String> response = sendRequest(httpRequest);
        return handleResponse(response, RegisterResult.class);
    }

    public LoginResult login(LoginRequest request) throws IOException {
        HttpRequest httpRequest = buildRequest("POST", "/session", request);
        HttpResponse<String> response = sendRequest(httpRequest);
        return handleResponse(response, LoginResult.class);
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        }
        else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws IOException {
        try (HttpClient client = HttpClient.newHttpClient()) {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> resultClass) throws IOException {
        int statusCode = response.statusCode();
        if (statusCode < 200 || statusCode >= 300) {
            throw new IOException("Server request failed");
        }
        return new Gson().fromJson(response.body(), resultClass);
    }
}
