package lucky.seven.kanbanbuzz.entity;

public enum UserRole {
    ROLE_USER("USER"),
    ROLE_MANAGER("MANAGER"),
    ;

    private final String role;

    UserRole(String role) {
        this.role = role;
    }
}
