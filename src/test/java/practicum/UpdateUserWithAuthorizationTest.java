package practicum;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import practicum.utils.UserService;

import java.util.ArrayList;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class UpdateUserWithAuthorizationTest {
    private UserService userService;
    private String newRandomEmail;
    private String accessToken;
    private String refreshToken;

    @Before
    public void beforeTests() {
        userService = new UserService();
        ArrayList<String> randomUserLoginPass = userService.getRandomCredentialsUser();
        newRandomEmail = RandomStringUtils.randomAlphabetic(7).toLowerCase(Locale.ROOT) + "@new.com";

        Response response = userService.registerNewUser(randomUserLoginPass.get(0), randomUserLoginPass.get(1), randomUserLoginPass.get(2));

        int registrationStatusCode = response.statusCode();
        assertThat("User registration status code is incorrect", registrationStatusCode, equalTo( 200));

        response = userService.login(randomUserLoginPass.get(0), randomUserLoginPass.get(1));

        int loginStatusCode = response.statusCode();
        assertThat("User login status code is incorrect", loginStatusCode, equalTo( 200));
        accessToken = response.path("accessToken");
        refreshToken = response.path("refreshToken");
    }

    @After
    public void  afterTests() {
        Response response = userService.logout(refreshToken);

        int logOutStatusCode = response.statusCode();
        assertThat("User log out status code is incorrect", logOutStatusCode, equalTo( 200));
    }

    @Test
    public void emailCanBeUpdated() {
        Response response = userService.updateUserEmail(accessToken, newRandomEmail);

        int updateStatusCode = response.statusCode();
        assertThat("Update user status code is incorrect", updateStatusCode, equalTo( 200));

        String updatedEmail = response.path("user.email");
        Assert.assertEquals("User email is not updated", newRandomEmail, updatedEmail);
    }

    @Test
    public void nameCanBeUpdated() {
        String newName = "new";
        Response response = userService.updateUserName(accessToken, newName);
        int updateStatusCode = response.statusCode();
        assertThat("Update user status code is incorrect", updateStatusCode, equalTo( 200));

        String updatedName = response.path("user.name");
        Assert.assertEquals("User name is not updated", newName, updatedName);
    }
}
