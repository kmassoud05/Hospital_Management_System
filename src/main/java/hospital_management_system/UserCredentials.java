package hospital_management_system;

public class UserCredentials {
    private final int userId;
    private final String username;
    private final String fullName;
    private final String role;
    
    public UserCredentials() {
        this.userId = 0;
        this.username = "default_username";
        this.fullName = "default_full_name";
        this.role = "default_role";
    }
    
    public UserCredentials(String username) {
        this.userId = 0;
        this.username = username;
        this.fullName = "default_full_name";
        this.role = "default_role";
    }
    
    public UserCredentials(int userId, String username, String fullName, String role) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }
    
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
}
