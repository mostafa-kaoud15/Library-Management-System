package DAOimplement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import DAOInterface.UserDAO;
import database.Database;
import model.User;

public class UserDaoImpl implements UserDAO{
    private static final Logger logger = Logger.getLogger(UserDaoImpl.class.getName());
    private static final String FIND_BY_NAME = "select * from users where username = ?";
    private static final String INSERT_INTO_USER = "insert into users(username, password, user_role) values (?, ?, ?)";
    private static final String FIND_PASSWORD = "select password from users where username = ?";
    @Override
    public boolean addUser(User user) {
        try (Connection connection = Database.getConnection()) {
            connection.setAutoCommit(false);
            if (!isUserExist(user.getUsername(), connection)) {
                 insertUserIntoDatabase(user, connection);
                 connection.commit();
                 logger.info("the user insert successfully : "  + user.getUsername());
                 return true;
            }
            logger.info("User already exists");
            return false;
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "there are problem with getting connection");
            return false;
        }
    }

    @Override
    public Optional<User> getUserByUsername(String userName) {
        try(Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME);) {

                preparedStatement.setString(1, userName);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {

                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String role = resultSet.getString("user_role");

                    return Optional.of(new User(id, username, password, role));
                }
                logger.info("user not found");
                return Optional.empty();

        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "error exist while getting connection");
            return Optional.empty();
        }

    }

    @Override
    public Boolean login(String username, String password) {
        try (Connection connection = Database.getConnection()) {
            if (isUserExist(username, connection) && Objects.equals(password, getPassword(username, connection))) {
                logger.info("user logged in successfully");
                return true;
            }
            logger.warning("user name or password incorrect");
            return false;

        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "error exist while login");
            return false;
        }
    }


    private boolean isUserExist(String username, Connection connection) {
        try (PreparedStatement findUser = connection.prepareStatement(FIND_BY_NAME);) {
            findUser.setString(1, username);
            ResultSet rs = findUser.executeQuery();
            return rs.next();
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "error while find is the user in database : " + username);
        }
        return false;
    }

    private void insertUserIntoDatabase(User user, Connection connection) {
        try (PreparedStatement insert = connection.prepareStatement(INSERT_INTO_USER)){
            insert.setString(1, user.getUsername());
            insert.setString(2, user.getPassword());
            insert.setString(3, user.getUserRole());

            insert.executeUpdate();


        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "error while inserting the user into database : " + user.getUsername());
            try {
                connection.rollback();
            }
            catch (SQLException e1) {
                logger.log(Level.SEVERE, "error while rolling back transaction");
            }
        }

    }

    private static String getPassword(String username, Connection connection) {
        try (PreparedStatement findPasswordQuery = connection.prepareStatement(FIND_PASSWORD)) {
            findPasswordQuery.setString(1, username);
            ResultSet resultSet = findPasswordQuery.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            }
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "error while finding password", e);

        }
        return "";
    }

    
}
