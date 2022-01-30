package practicum;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserService extends BaseService {

    private static final String UPDATE_USER_URI = "api/auth/user";

    public Response updateUserEmail(String accessToken, String newEmail) {
        String preprocessedLogin = newEmail == null ? "null" : "\"" + newEmail + "\"";
        String loginRequestBody = "{\"email\":" + preprocessedLogin + "}";

        return given()
                .spec(getAuthorizedSpec(accessToken))
                .body(loginRequestBody)
                .when()
                .patch(UPDATE_USER_URI);
    }

    public Response updateUserName(String accessToken, String newName) {
        String preprocessedName = newName == null ? "null" : "\"" + newName + "\"";
        String loginRequestBody = "{\"name\":" + preprocessedName + "}";

        return given()
                .spec(getAuthorizedSpec(accessToken))
                .body(loginRequestBody)
                .when()
                .patch(UPDATE_USER_URI);
    }
}
