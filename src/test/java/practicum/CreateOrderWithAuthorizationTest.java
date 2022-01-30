package practicum;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import practicum.utils.OrderCreator;
import practicum.utils.UserAuthenticator;
import practicum.utils.UserService;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {

    private UserService userService;
    private ArrayList<String> randomUserLoginPass;
    private UserAuthenticator userAuthenticator;
    private OrderCreator orderCreator;

    @Before
    public void beforeTests() {
        userService = new UserService();
        randomUserLoginPass = userService.getRandomCredentialsUser();
        userAuthenticator = new UserAuthenticator();
        orderCreator = new OrderCreator();

        Response response = userService.registerNewUser(randomUserLoginPass.get(0), randomUserLoginPass.get(1), randomUserLoginPass.get(2));

        int registrationStatusCode = response.statusCode();
        assertThat("User registration status code is incorrect", registrationStatusCode, equalTo( 200));
    }

    @Test
    public void orderCanBeCreated() {
        Response response = userAuthenticator.loginUser(randomUserLoginPass.get(0), randomUserLoginPass.get(1));

        int loginStatusCode = response.statusCode();
        assertThat("User login status code is incorrect", loginStatusCode, equalTo( 200));
        String accessToken = response.path("accessToken");

        response =  orderCreator.createOrder(accessToken, new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6e", "61c0c5a71d1f82001bdaaa72"});

        int createOrderStatusCode = response.statusCode();
        assertThat("Create order status code is incorrect", createOrderStatusCode, equalTo( 200));

        String createdBurgerName = response.path("name");
        Assert.assertEquals("Burger name is incorrect", "Флюоресцентный spicy люминесцентный бургер", createdBurgerName);
    }

    //Тест не проходит. По документации сделать заказ может только авторизованный пользователь
    @Test
    public void orderCannotBeCreatedWithoutAuthorization() {
        Response response =  orderCreator.createOrder(null, new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6e", "61c0c5a71d1f82001bdaaa72"});

        int createOrderStatusCode = response.statusCode();
        assertThat("Create order status code is incorrect", createOrderStatusCode, equalTo( 301));

        String redirectUrl = response.header("Location");
        Assert.assertTrue("Redirect URL is incorrect", redirectUrl.endsWith("/login"));
    }

    @Test
    public void orderCannotBeCreatedWithoutIngredients() {
        Response response = userAuthenticator.loginUser(randomUserLoginPass.get(0), randomUserLoginPass.get(1));

        int loginStatusCode = response.statusCode();
        assertThat("User login status code is incorrect", loginStatusCode, equalTo( 200));
        String accessToken = response.path("accessToken");

        response =  orderCreator.createOrder(accessToken, new String[]{});

        int errorStatusCode = response.statusCode();
        assertThat("Error status code is incorrect", errorStatusCode, equalTo( 400));

        String errorMessage = response.path("message");
        Assert.assertEquals("Error message is incorrect", "Ingredient ids must be provided", errorMessage);
    }

    @Test
    public void orderCannotBeCreatedWithWrongIngredients() {
        Response response = userAuthenticator.loginUser(randomUserLoginPass.get(0), randomUserLoginPass.get(1));

        int loginStatusCode = response.statusCode();
        assertThat("User login status code is incorrect", loginStatusCode, equalTo( 200));
        String accessToken = response.path("accessToken");

        response =  orderCreator.createOrder(accessToken, new String[]{"10", "20", "30"});

        int errorStatusCode = response.statusCode();
        assertThat("Error status code is incorrect", errorStatusCode, equalTo( 500));
    }
}
