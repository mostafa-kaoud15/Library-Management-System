
import DAOimplement.UserDaoImpl;


public class Main {
    public static void main(String[] args) {
        UserDaoImpl userDao = new UserDaoImpl();
        userDao.login("mostafa kaoud", "2000");
    }
}