package practicum.dto;

public class CreateUserRegistrarRequest {
    private final String email;
    private final String password;
    private final String name;

    public CreateUserRegistrarRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name =name;
    };
}
