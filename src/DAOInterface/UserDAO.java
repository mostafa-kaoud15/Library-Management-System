package DAOInterface;

import model.User;

import java.util.Optional;

public interface UserDAO {
    boolean addUser(User user);
    Optional<User> getUserByUsername(String username);
    Boolean login(String username, String password);
}
