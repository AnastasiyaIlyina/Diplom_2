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


public class UpdateUserWithExistDataTest {
    private UserService userService;
    public String existRandomUserEmail;
    public String existRandomUserName;
    private String accessToken;
    private String refreshToken;

    @Before
    public void beforeTests() {
        userService = new UserService();
        ArrayList<String> randomUserLoginPass = userService.getRandomCredentialsUser();

        // Регистрируем пользователя, которого будем изменять
        Response response = userService.registerNewUser(randomUserLoginPass.get(0), randomUserLoginPass.get(1), randomUserLoginPass.get(2));

        int registrationStatusCode = response.statusCode();
        assertThat("User registration status code is incorrect", registrationStatusCode, equalTo( 200));

        // Регистрируем пользователя, который будет иметь уже существующие данные
        existRandomUserEmail = RandomStringUtils.randomAlphabetic(7).toLowerCase(Locale.ROOT) + "@exist.com";
        String existRandomUserPass = RandomStringUtils.randomAlphabetic(7).toLowerCase(Locale.ROOT);
        existRandomUserName = RandomStringUtils.randomAlphabetic(7).toLowerCase(Locale.ROOT);

        response = userService.registerNewUser(existRandomUserEmail, existRandomUserPass, existRandomUserName);

        int registrationExistStatusCode = response.statusCode();
        assertThat("Exist user registration status code is incorrect", registrationExistStatusCode, equalTo( 200));

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
        assertThat("User logout status code is incorrect", logOutStatusCode, equalTo( 200));
    }

    @Test
    public void emailCannotBeUpdatedWithExistEmail() {
        Response response = userService.updateUserEmail(accessToken, existRandomUserEmail);

        int errorStatusCode = response.statusCode();
        assertThat(" Error status code is incorrect", errorStatusCode, equalTo( 403));

        String errorMessage = response.path("message");
        assertThat("Error message is incorrect", errorMessage, equalTo( "User with such email already exists"));
    }

    @Test
    public void nameCanBeUpdatedWithExistName() {
        Response response = userService.updateUserName(accessToken, existRandomUserName);

        int statusCode = response.statusCode();
        assertThat("Update user status code is incorrect", statusCode, equalTo(200));

        String updatedName = response.path("user.name");
        Assert.assertEquals("User name is not updated", existRandomUserName, updatedName);
    }
}
