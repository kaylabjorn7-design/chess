package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dto.RegisterRequest;
import dto.RegisterResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
