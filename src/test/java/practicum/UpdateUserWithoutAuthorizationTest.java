package practicum;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class UpdateUserTest {
    private UserRegistrar userRegistrar;
    private ArrayList<String> randomUserLoginPass;
    private UserAuthenticator userAuthenticator;
    private UserService userService;
    public String newRandomEmail;
    public String newName = "new";
    public String newUserEmail = "Same1@email.com";
    public String newUserPass = "159753";
    public String newUserName = "Same1Name";
    Response response = userRegistrar.registerNewUser(newUserEmail, newUserPass, newUserName);

    @Before
    public void beforeTests() {
        userRegistrar = new UserRegistrar();
        randomUserLoginPass = userRegistrar.getRandomCredentialsUser();
        userAuthenticator = new UserAuthenticator();
        userService = new UserService();
        newRandomEmail = RandomStringUtils.randomAlphabetic(7).toLowerCase(Locale.ROOT) + "@new.com";

        Response response = userRegistrar.registerNewUser(randomUserLoginPass.get(0), randomUserLoginPass.get(1), randomUserLoginPass.get(2));
        int registrationStatusCode = response.statusCode();
        assertThat("User registration status code is incorrect", registrationStatusCode, equalTo( 200));

        String newUserEmail = "Same@email.com";
        String newUserPass = "159753";
        String newUserName = "SameName";
        Response response = userRegistrar.registerNewUser(newUserEmail, newUserPass, newUserName);
    }

    @Test
    public void updateEmailWithAuthorization() {
        Response response = userAuthenticator.loginUser(randomUserLoginPass.get(0), randomUserLoginPass.get(1));

        int loginStatusCode = response.statusCode();
        assertThat("User login status code is incorrect", loginStatusCode, equalTo( 200));
        String accessToken = response.path("accessToken");

        response = userService.updateUserEmail(accessToken, newRandomEmail);
        int updateStatusCode = response.statusCode();
        assertThat("Update user status code is incorrect", updateStatusCode, equalTo( 200));

        String updatedEmail = response.path("user.email");
        Assert.assertEquals("User email is not updated", newRandomEmail, updatedEmail);
    }

    @Test
    public void updateNameWithAuthorization() {
        Response response = userAuthenticator.loginUser(randomUserLoginPass.get(0), randomUserLoginPass.get(1));

        int loginStatusCode = response.statusCode();
        assertThat("User login status code is incorrect", loginStatusCode, equalTo( 200));
        String accessToken = response.path("accessToken");

        response = userService.updateUserName(accessToken, newName);
        int updateStatusCode = response.statusCode();
        assertThat("Update user status code is incorrect", updateStatusCode, equalTo( 200));

        String updatedName = response.path("user.name");
        Assert.assertEquals("User name is not updated", newName, updatedName);
    }

    @Test
    public void updateWithExistEmailWithAuthorization() {
        Response response = userAuthenticator.loginUser(randomUserLoginPass.get(0), randomUserLoginPass.get(1));

        int loginStatusCode = response.statusCode();
        assertThat("User login status code is incorrect", loginStatusCode, equalTo( 200));
        String accessToken = response.path("accessToken");

        response = userService.updateUserEmail(accessToken, newUserEmail);

        int firstUpdateStatusCode = response.statusCode();
        assertThat(" First update user  email status code is incorrect", firstUpdateStatusCode, equalTo( 403));

        String errorMessage = response.path("message");
        assertThat("Error message is incorrect", errorMessage, equalTo( "User with such email already exists"));
    }



    @Test
    public void updateEmailWithoutAuthorization() {
        Response response = userService.updateUserEmail(null, newRandomEmail);
        int updateStatusCode = response.statusCode();
        assertThat("Update user status code is incorrect", updateStatusCode, equalTo( 401));

        String errorMessage = response.path("message");
        assertThat("Error message is incorrect", errorMessage, equalTo( "You should be authorised"));
    }

    @Test
    public void updateNameWithoutAuthorization() {
        Response response = userService.updateUserName(null, newName);
        int updateStatusCode = response.statusCode();
        assertThat("Update user status code is incorrect", updateStatusCode, equalTo( 401));

        String errorMessage = response.path("message");
        assertThat("Error message is incorrect", errorMessage, equalTo( "You should be authorised"));
    }
}
