package client;

import com.google.gson.Gson;
import dto.RegisterRequest;
import dto.RegisterResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade (int portNumber) {
        serverUrl = "http://localhost:" + portNumber;
    }

    public RegisterResult register(RegisterRequest request) throws IOException {
        URL url = new URL(serverUrl + "/user");
        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();
        try {
            connection.setRequestMethod("POST");

            Gson gson = new Gson();
            String json = gson.toJson(request);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            try (OutputStream output = connection.getOutputStream()) {
                output.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int statusCode = connection.getResponseCode();
            if (statusCode != 200) {
                throw new IOException("Registration failed");
            }

            try (InputStream input = connection.getInputStream();
                 InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
                return gson.fromJson(reader, RegisterResult.class);
            }

        } finally {
            connection.disconnect();
        }
    }
}
