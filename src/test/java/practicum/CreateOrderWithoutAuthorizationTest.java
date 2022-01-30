package practicum;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import practicum.utils.OrderService;
import practicum.utils.UserService;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderWithoutAuthorizationTest {

    private OrderService orderService;

    @Before
    public void beforeTests() {
        UserService userService = new UserService();
        ArrayList<String> randomUserLoginPass = userService.getRandomCredentialsUser();
        orderService = new OrderService();

        Response response = userService.registerNewUser(randomUserLoginPass.get(0), randomUserLoginPass.get(1), randomUserLoginPass.get(2));

        int registrationStatusCode = response.statusCode();
        assertThat("User registration status code is incorrect", registrationStatusCode, equalTo( 200));
    }

    // Тест не проходит. По документации нельзя создать заказ без авторизации
    @Test
    public void orderCannotBeCreatedWithoutAuthorization() {
        Response response =  orderService.createOrder(null, new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6e", "61c0c5a71d1f82001bdaaa72"});
        String str = response.asString();
        int createOrderStatusCode = response.statusCode();
        Assert.assertNotEquals("Create order status code is incorrect", 200, createOrderStatusCode);
    }
}
