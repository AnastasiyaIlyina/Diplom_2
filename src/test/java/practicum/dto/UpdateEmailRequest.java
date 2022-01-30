package practicum.dto;

public class LogoutRequest {
    private final String token;

    public LogoutRequest(String token) {
        this.token = token;
    };
}
