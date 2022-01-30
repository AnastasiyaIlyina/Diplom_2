package practicum;

import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import practicum.utils.UserService;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserAuthorizationTest {
    private ArrayList<String> randomUserLoginPass;
    private UserService userService;
    public String wrongEmail = "notRight@email.ru";
    public String wrongPassword = "1notRight";

    @Before
    public void beforeTests() {
        userService = new UserService();
        randomUserLoginPass = userService.getRandomCredentialsUser();
        Response response = userService.registerNewUser(randomUserLoginPass.get(0), randomUserLoginPass.get(1), randomUserLoginPass.get(2));
        int statusCodeFirstUser = response.statusCode();
        assertThat("Status code is incorrect", statusCodeFirstUser, equalTo( 200));
    }

    @Test
    public void userCanLogIn() {
        Response response = userService.login(randomUserLoginPass.get(0), randomUserLoginPass.get(1));

        int statusCode = response.statusCode();
        assertThat("Status code is incorrect", statusCode, equalTo( 200));

        var accessToken= response.then().extract().body();
        assertThat("AccessToken is not created", accessToken, notNullValue());
    }

    @Test
    public void userCannotLogInWithWrongEmail() {
        Response response = userService.login(wrongEmail, randomUserLoginPass.get(1));

        int errorStatusCode = response.statusCode();
        assertThat("Error status code is incorrect", errorStatusCode, equalTo( 401));

        String errorMessage = response.path("message");
        assertThat("Error message is incorrect", errorMessage, equalTo( "email or password are incorrect"));
    }

    @Test
    public void userCannotLogInWithWrongPassword() {
        Response response = userService.login(randomUserLoginPass.get(0), wrongPassword);

        int errorStatusCode = response.statusCode();
        assertThat("Error status code is incorrect", errorStatusCode, equalTo( 401));

        String errorMessage = response.path("message");
        assertThat("Error message is incorrect", errorMessage, equalTo( "email or password are incorrect"));
    }
}
