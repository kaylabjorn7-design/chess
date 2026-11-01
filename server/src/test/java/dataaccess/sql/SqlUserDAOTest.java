package dataaccess.sql;

import dataaccess.DataAccessException;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

public class SqlUserDAOTest {

    SqlUserDAO sqlUserDAO = new SqlUserDAO();

    @BeforeEach
    public void setUp() throws DataAccessException {
        sqlUserDAO.clear();
    }

    @Test
    public void getUserSuccess() throws DataAccessException {
        UserData userData = new UserData("Alice", "password123", "alice@byu.edu");
        sqlUserDAO.createUser(userData);
        UserData testData = sqlUserDAO.getUser("Alice");

        assertNotNull(testData);
        assertEquals("Alice", testData.username());
        assertTrue(BCrypt.checkpw("password123", testData.password()));
        assertEquals("alice@byu.edu", testData.email());
    }

    @Test
    public void getUserFailure() throws DataAccessException {
        assertNull(sqlUserDAO.getUser("Alice"));
    }

    @Test
    public void createUserFailure() throws DataAccessException {
        UserData userData = new UserData("Alice", "password123", "alice@byu.edu");
        sqlUserDAO.createUser(userData);

        assertThrows(DataAccessException.class, () -> sqlUserDAO.createUser(userData));
    }

    @Test
    public void clearSuccess() throws DataAccessException {
        sqlUserDAO.createUser(new UserData("Alice", "password123", "alice@byu.edu"));
        sqlUserDAO.createUser(new UserData("Olivia", "password456", "olivia@byu.edu"));

        assertNotNull(sqlUserDAO.getUser("Alice"));
        assertNotNull(sqlUserDAO.getUser("Olivia"));

        sqlUserDAO.clear();

        assertNull(sqlUserDAO.getUser("Alice"));
        assertNull(sqlUserDAO.getUser("Olivia"));
    }
}
