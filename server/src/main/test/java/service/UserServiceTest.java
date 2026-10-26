package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dto.LoginRequest;
import dto.LoginResult;
import dto.RegisterRequest;
import dto.RegisterResult;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Test
    void registerSuccess() throws BadRequestException, DataAccessException, AlreadyTakenException {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        UserService userService = new UserService(userDAO, authDAO);

        RegisterRequest registerRequest = new RegisterRequest("Alice", "password123", "alice@byu.edu");

        RegisterResult registerResult = userService.register(registerRequest);

        assertNotNull(userDAO.getUser("Alice"));
        assertEquals(registerResult.username(), userDAO.getUser(registerResult.username()).username());
        assertEquals("password123", userDAO.getUser(registerResult.username()).password());
        assertEquals("alice@byu.edu", userDAO.getUser(registerResult.username()).email());
        assertEquals(registerResult.authToken(), authDAO.findAuth(registerResult.authToken()).authToken());
        assertEquals("Alice", authDAO.findAuth(registerResult.authToken()).username());
    }

    @Test
    void registerDuplicateUserName() throws DataAccessException {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        UserService userService = new UserService(userDAO, authDAO);

        UserData testUser = new UserData("Alice", "password123", "alice@byu.edu");
        userDAO.createUser(testUser);

        RegisterRequest registerRequest = new RegisterRequest("Alice", "password123", "alice@byu.edu");

        assertThrows(AlreadyTakenException.class, ()-> userService.register(registerRequest));
    }

    @Test
    void registerBadRequest() {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        UserService userService = new UserService(userDAO, authDAO);

        RegisterRequest registerRequest = new RegisterRequest("", "", "");

        assertThrows(BadRequestException.class, ()-> userService.register(registerRequest));
    }

    @Test
    void loginSuccess() throws DataAccessException, BadRequestException {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        UserService userService = new UserService(userDAO, authDAO);

        UserData testUser = new UserData("Alice", "password123", "alice@byu.edu");
        userDAO.createUser(testUser);

        LoginRequest loginRequest = new LoginRequest("Alice", "password123");

        LoginResult loginResult = userService.login(loginRequest);

        assertEquals("Alice", loginResult.username());
        assertNotNull(authDAO.findAuth(loginResult.authToken()));
        assertEquals(loginResult.authToken(), authDAO.findAuth(loginResult.authToken()).authToken());
        assertEquals("Alice", authDAO.findAuth(loginResult.authToken()).username());
    }

    @Test
    void incorrectPassword() throws DataAccessException {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        UserService userService = new UserService(userDAO, authDAO);

        UserData testUser = new UserData("Alice", "password123", "alice@byu.edu");
        userDAO.createUser(testUser);

        LoginRequest loginRequest = new LoginRequest("Alice", "password124");

        assertThrows(UnauthorizedException.class, ()-> userService.login(loginRequest));
    }

    //bad request

    @Test
    void logoutSuccess() throws DataAccessException, UnauthorizedException {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        UserService userService = new UserService(userDAO, authDAO);

        AuthData testAuth = new AuthData("133d332d-693e-4379-8644-2e59b07399ec", "Alice");
        authDAO.createAuth(testAuth);

        userService.logout(testAuth.authToken());

        assertNull(authDAO.findAuth(testAuth.authToken()));
    }

    @Test
    void logoutInvalidToken() throws DataAccessException {
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        UserService userService = new UserService(userDAO, authDAO);

        AuthData testAuth = new AuthData("133d332d-693e-4379-8644-2e59b07399ec", "Alice");
        authDAO.createAuth(testAuth);

        assertThrows(UnauthorizedException.class, ()-> userService.logout(null));
    }
}
