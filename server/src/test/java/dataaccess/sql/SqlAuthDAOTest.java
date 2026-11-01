package dataaccess.sql;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlAuthDAOTest {
    SqlAuthDAO sqlAuthDAO = new SqlAuthDAO();

    @BeforeEach
    public void setUp() throws DataAccessException {
        sqlAuthDAO.clear();
    }

    @Test
    public void findAuthSuccess() throws DataAccessException {
        AuthData authData = new AuthData("133d332d-693e-4379-8644-2e59b07399ec", "Alice");
        sqlAuthDAO.createAuth(authData);
        AuthData testData = sqlAuthDAO.findAuth("133d332d-693e-4379-8644-2e59b07399ec");

        assertNotNull(testData);
        assertEquals("133d332d-693e-4379-8644-2e59b07399ec", testData.authToken());
        assertEquals("Alice", testData.username());
    }

    @Test
    public void findAuthFailure() throws DataAccessException {
        assertNull(sqlAuthDAO.findAuth("133d332d-693e-4379-8644-2e59b07399ec"));
    }

    @Test
    public void createAuthFailure() throws DataAccessException {
        AuthData authData = new AuthData("133d332d-693e-4379-8644-2e59b07399ec", "Alice");
        sqlAuthDAO.createAuth(authData);

        assertThrows(DataAccessException.class, () -> sqlAuthDAO.createAuth(authData));
    }

    @Test
    public void deleteAuthSuccess() throws DataAccessException {
        sqlAuthDAO.createAuth(new AuthData("133d332d-693e-4379-8644-2e59b07399ec", "Alice"));
        sqlAuthDAO.createAuth(new AuthData("133d332d-693e-4379-8644-2e59b07399ed", "Olivia"));

        assertNotNull(sqlAuthDAO.findAuth("133d332d-693e-4379-8644-2e59b07399ec"));
        assertNotNull(sqlAuthDAO.findAuth("133d332d-693e-4379-8644-2e59b07399ed"));

        sqlAuthDAO.deleteAuth("133d332d-693e-4379-8644-2e59b07399ec");

        assertNull(sqlAuthDAO.findAuth("133d332d-693e-4379-8644-2e59b07399ec"));
        assertNotNull(sqlAuthDAO.findAuth("133d332d-693e-4379-8644-2e59b07399ed"));
    }

    @Test
    public void clearSuccess() throws DataAccessException {
        sqlAuthDAO.createAuth(new AuthData("133d332d-693e-4379-8644-2e59b07399ec", "Alice"));
        sqlAuthDAO.createAuth(new AuthData("133d332d-693e-4379-8644-2e59b07399ed", "Olivia"));

        assertNotNull(sqlAuthDAO.findAuth("133d332d-693e-4379-8644-2e59b07399ec"));
        assertNotNull(sqlAuthDAO.findAuth("133d332d-693e-4379-8644-2e59b07399ed"));

        sqlAuthDAO.clear();

        assertNull(sqlAuthDAO.findAuth("133d332d-693e-4379-8644-2e59b07399ec"));
        assertNull(sqlAuthDAO.findAuth("133d332d-693e-4379-8644-2e59b07399ed"));
    }
}
