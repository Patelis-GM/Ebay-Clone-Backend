package silkroad.entities;

public enum Roles {

    USER("USER"),
    ADMIN("ADMIN");

    private final String role;

    Roles(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return this.role;
    }
}
