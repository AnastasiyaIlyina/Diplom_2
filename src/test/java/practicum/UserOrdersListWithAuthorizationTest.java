package practicum;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import practicum.utils.*;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserOrdersListWithAuthorizationTest {
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

    // Тест не проходит. Авторизованному пользователю выдается полный список заказов, а не индивидуальный.
    @Test
    public void userCanGetOrdersList() {
        Response response =  orderService.createOrder(accessToken, new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6e", "61c0c5a71d1f82001bdaaa72"});

        int createFirstOrderStatusCode = response.statusCode();
        assertThat("Create first order status code is incorrect", createFirstOrderStatusCode, equalTo( 200));

        response = orderService.createOrder(accessToken, new String[]{"61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa7a", "61c0c5a71d1f82001bdaaa74"});

        int createSecondOrderStatusCode = response.statusCode();
        assertThat("Create first order status code is incorrect", createSecondOrderStatusCode, equalTo( 200));

        response = orderService.getOrdersList(accessToken);

        int getOrderListStatusCode = response.statusCode();
        assertThat("Create order status code is incorrect", getOrderListStatusCode, equalTo( 200));

        int totalOrders = response.path("total");
        assertThat("Total orders count is incorrect", totalOrders, equalTo( 2));
    }
}
