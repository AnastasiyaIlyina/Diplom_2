package practicum;

import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import practicum.utils.OrderService;
import practicum.utils.UserService;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserOrdersListWithoutAuthorizationTest {
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

    @Test
    public void userCanNotGetOrdersList() {
        Response response = orderService.getOrdersList(null);

        int getOrderListStatusCode = response.statusCode();
        assertThat("Create order status code is incorrect", getOrderListStatusCode, equalTo( 401));

        String errorMessage = response.path("message");
        assertThat("Error message is incorrect", errorMessage, equalTo( "You should be authorised"));
    }
}
