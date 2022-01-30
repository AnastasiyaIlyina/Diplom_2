package practicum.dto;

public class UserRegistrationRequest {
    private final String email;
    private final String password;
    private final String name;

    public UserRegistrationRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    };
}
