package enums;

public enum UserRole {
    admin,
    user;

    public static UserRole fromString(String role) {
        try {
            return UserRole.valueOf(role.toLowerCase());
        }
        catch (IllegalArgumentException | NullPointerException ee) {
            System.out.println("invalid user role: " + role);
        }
        return null;
    }
}
