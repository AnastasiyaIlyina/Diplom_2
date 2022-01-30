package practicum;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import practicum.utils.UserService;

import java.util.ArrayList;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class UpdateUserWithoutAuthorizationTest {
    private UserService userService;
    public String newRandomEmail;
    public String newName = "new";

    @Before
    public void beforeTests() {
        userService = new UserService();
        ArrayList<String> randomUserLoginPass = userService.getRandomCredentialsUser();
        newRandomEmail = RandomStringUtils.randomAlphabetic(7).toLowerCase(Locale.ROOT) + "@new.com";

        Response response = userService.registerNewUser(randomUserLoginPass.get(0), randomUserLoginPass.get(1), randomUserLoginPass.get(2));

        int registrationStatusCode = response.statusCode();
        assertThat("User registration status code is incorrect", registrationStatusCode, equalTo( 200));
    }

    @Test
    public void emailCannotBeUpdated() {
        Response response = userService.updateUserEmail(null, newRandomEmail);

        int errorStatusCode = response.statusCode();
        assertThat("Error status code is incorrect", errorStatusCode, equalTo( 401));

        String errorMessage = response.path("message");
        assertThat("Error message is incorrect", errorMessage, equalTo( "You should be authorised"));
    }

    @Test
    public void nameCannotBeUpdated() {
        Response response = userService.updateUserName(null, newName);

        int errorStatusCode = response.statusCode();
        assertThat("Error status code is incorrect", errorStatusCode, equalTo( 401));

        String errorMessage = response.path("message");
        assertThat("Error message is incorrect", errorMessage, equalTo( "You should be authorised"));
    }
}
