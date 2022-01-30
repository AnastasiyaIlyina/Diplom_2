package practicum;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import practicum.utils.OrderService;
import practicum.utils.UserService;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderWithAuthorizationTest {

    private UserService userService;
    private OrderService orderService;
    private String accessToken;
    private String refreshToken;

    @Before
    public void beforeTests() {
        userService = new UserService();
        ArrayList<String> randomUserLoginPass = userService.getRandomCredentialsUser();
        orderService = new OrderService();

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
    public void afterTests() {
        Response response = userService.logout(refreshToken);

        int logOutStatusCode = response.statusCode();
        assertThat("User log out status code is incorrect", logOutStatusCode, equalTo( 200));
    }


    @Test
    public void orderCanBeCreated() {
        Response response =  orderService.createOrder(accessToken, new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6e", "61c0c5a71d1f82001bdaaa72"});

        int createOrderStatusCode = response.statusCode();
        assertThat("Create order status code is incorrect", createOrderStatusCode, equalTo( 200));

        String createdBurgerName = response.path("name");
        assertThat("Burger name is incorrect", createdBurgerName, equalTo("Флюоресцентный spicy люминесцентный бургер"));
    }

    @Test
    public void orderCannotBeCreatedWithoutIngredients() {
        Response response =  orderService.createOrder(accessToken, new String[]{});

        int errorStatusCode = response.statusCode();
        assertThat("Error status code is incorrect", errorStatusCode, equalTo( 400));

        String errorMessage = response.path("message");
        Assert.assertEquals("Error message is incorrect", "Ingredient ids must be provided", errorMessage);
    }

    @Test
    public void orderCannotBeCreatedWithWrongIngredients() {
        Response response =  orderService.createOrder(accessToken, new String[]{"10", "20", "30"});

        int errorStatusCode = response.statusCode();
        assertThat("Error status code is incorrect", errorStatusCode, equalTo( 500));
    }
}
