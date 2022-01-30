package practicum.utils;

import com.google.gson.Gson;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import practicum.dto.*;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class UserService extends BaseService {
    private final Gson gson = new Gson();

    private static final String REGISTER_USER_URI = "api/auth/register";
    private static final String LOGIN_URI = "/api/auth/login";
    private static final String UPDATE_USER_URI = "api/auth/user";
    private static final String EXIT_URI = "/api/auth/logout";

    public Response updateUserEmail(String accessToken, String newEmail) {
        UpdateEmailRequest updateEmailRequest = new UpdateEmailRequest(newEmail);

        return given()
                .spec(getAuthorizedSpec(accessToken))
                .body(gson.toJson(updateEmailRequest))
                .when()
                .patch(UPDATE_USER_URI);
    }

    public Response updateUserName(String accessToken, String newName) {
        UpdateNameRequest updateNameRequest = new UpdateNameRequest(newName);

        return given()
                .spec(getAuthorizedSpec(accessToken))
                .body(gson.toJson(updateNameRequest))
                .when()
                .patch(UPDATE_USER_URI);
    }

    public Response logout(String refreshToken) {
        LogoutRequest logoutRequest = new LogoutRequest(refreshToken);

        return given()
                .spec(getAuthorizedSpec(refreshToken))
                .body(gson.toJson(logoutRequest))
                .when()
                .post(EXIT_URI);
    }

    public ArrayList<String> getRandomCredentialsUser() {
        String userEmail = RandomStringUtils.randomAlphabetic(7) + "@test.com";
        String userPassword = RandomStringUtils.randomAlphabetic(10);
        String userName = RandomStringUtils.randomAlphabetic(10);

        ArrayList<String> loginPass = new ArrayList<>();
        loginPass.add(userEmail);
        loginPass.add(userPassword);
        loginPass.add(userName);

        return loginPass;
    }

    public Response registerNewUser(String email, String password, String name) {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(email, password, name);

        return given()
                .spec(getBaseSpec())
                .body(gson.toJson(userRegistrationRequest))
                .when()
                .post(REGISTER_USER_URI);
    }

    public Response login(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);

        return given()
                .spec(getBaseSpec())
                .body(gson.toJson(loginRequest))
                .when()
                .post(LOGIN_URI);
    }
}
