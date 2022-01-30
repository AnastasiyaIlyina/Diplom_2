package practicum;

import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import practicum.utils.UserService;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserRegistrationTest {
    private UserService userService;
    private ArrayList<String> randomUserLoginPass;

    @Before
    public void beforeTests() {
        userService = new UserService();
        randomUserLoginPass = userService.getRandomCredentialsUser();
    }

    @Test
    public void userCanBeRegistered() {
        Response response = userService.registerNewUser(randomUserLoginPass.get(0), randomUserLoginPass.get(1), randomUserLoginPass.get(2));

        int statusCode = response.statusCode();
        assertThat("Status code is incorrect", statusCode, equalTo( 200));

        var accessToken= response.then().extract().body();
        assertThat("AccessToken is not created", accessToken, notNullValue());
    }

    @Test
    public void duplicateUserCannotBeRegistered() {
        Response response = userService.registerNewUser(randomUserLoginPass.get(0), randomUserLoginPass.get(1), randomUserLoginPass.get(2));

        int statusCodeFirstUser = response.statusCode();
        assertThat("Status code is incorrect", statusCodeFirstUser, equalTo( 200));

        var accessToken= response.then().extract().body();
        assertThat("AccessToken is not created", accessToken, notNullValue());

        response = userService.registerNewUser(randomUserLoginPass.get(0), randomUserLoginPass.get(1), randomUserLoginPass.get(2));

        int statusCodeDuplicateUser = response.statusCode();
        assertThat("Error status code is incorrect", statusCodeDuplicateUser, equalTo( 403));

        String errorMessage = response.path("message");
        assertThat("Error message is incorrect", errorMessage, equalTo( "User already exists"));
    }

    @Test
    public void userWithoutEmailCannotBeRegistered() {
        Response response = userService.registerNewUser(null, randomUserLoginPass.get(1), randomUserLoginPass.get(2));

        int errorStatusCode = response.statusCode();
        assertThat("Error status code is incorrect", errorStatusCode, equalTo( 403));

        String errorMessage = response.path("message");
        assertThat("Error message is incorrect", errorMessage, equalTo( "Email, password and name are required fields"));
    }

    @Test
    public void userWithoutPasswordCannotBeRegistered() {
        Response response = userService.registerNewUser(randomUserLoginPass.get(0), null, randomUserLoginPass.get(2));

        int errorStatusCode = response.statusCode();
        assertThat("Error status code is incorrect", errorStatusCode, equalTo( 403));

        String errorMessage = response.path("message");
        assertThat("Error message is incorrect", errorMessage, equalTo( "Email, password and name are required fields"));
    }

    @Test
    public void userWithoutNameCannotBeRegistered() {
        Response response = userService.registerNewUser(randomUserLoginPass.get(0), randomUserLoginPass.get(1), null);

        int errorStatusCode = response.statusCode();
        assertThat("Error status code is incorrect", errorStatusCode, equalTo( 403));

        String errorMessage = response.path("message");
        assertThat("Error message is incorrect", errorMessage, equalTo( "Email, password and name are required fields"));
    }
}
