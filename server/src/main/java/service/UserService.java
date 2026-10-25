package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dto.RegisterRequest;
import dto.RegisterResult;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;


    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest registerRequest)
            throws BadRequestException, DataAccessException, AlreadyTakenException {
        if (registerRequest == null ||
                registerRequest.username() == null ||
                registerRequest.username().isBlank() ||
                registerRequest.password() == null ||
                registerRequest.password().isBlank() ||
                registerRequest.email() == null ||
                registerRequest.email().isBlank()) {
            throw new BadRequestException("Error: bad request");
        }
        if (userDAO.getUser(registerRequest.username()) != null) {
            throw new AlreadyTakenException("Error: already taken");
        }
        UserData userData = new UserData(
                registerRequest.username(),
                registerRequest.password(),
                registerRequest.email());
        userDAO.createUser(userData);

        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, registerRequest.username());
        authDAO.createAuth(authData);

        return new RegisterResult(registerRequest.username(), authToken);
    }
}
