package practicum;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import practicum.utils.*;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserOrdersListTest {
    private UserRegistrar userRegistrar;
    private ArrayList<String> randomUserLoginPass;
    private UserAuthenticator userAuthenticator;
    private OrderCreator orderCreator;
    private UserOrdersList userOrdersList;

    @Before
    public void beforeTests() {
        userRegistrar = new UserRegistrar();
        randomUserLoginPass = userRegistrar.getRandomCredentialsUser();
        userAuthenticator = new UserAuthenticator();
        orderCreator = new OrderCreator();
        userOrdersList = new UserOrdersList();

        Response response = userRegistrar.registerNewUser(randomUserLoginPass.get(0), randomUserLoginPass.get(1), randomUserLoginPass.get(2));

        int registrationStatusCode = response.statusCode();
        assertThat("User registration status code is incorrect", registrationStatusCode, equalTo( 200));
    }

    @Test
    public void userCanGetOrdersListWithAuthorization() {
        Response response = userAuthenticator.loginUser(randomUserLoginPass.get(0), randomUserLoginPass.get(1));

        int loginStatusCode = response.statusCode();
        assertThat("User login status code is incorrect", loginStatusCode, equalTo( 200));
        String accessToken = response.path("accessToken");

        response =  orderCreator.createOrder(accessToken, new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6e", "61c0c5a71d1f82001bdaaa72"});

        int createFirstOrderStatusCode = response.statusCode();
        assertThat("Create first order status code is incorrect", createFirstOrderStatusCode, equalTo( 200));

        response =  orderCreator.createOrder(accessToken, new String[]{"61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa7a", "61c0c5a71d1f82001bdaaa74"});

        int createSecondOrderStatusCode = response.statusCode();
        assertThat("Create first order status code is incorrect", createSecondOrderStatusCode, equalTo( 200));

        response = userOrdersList.getOrdersList(accessToken);

        int getOrderListStatusCode = response.statusCode();
        assertThat("Create order status code is incorrect", getOrderListStatusCode, equalTo( 200));

        int totalOrders = response.path("total");
        assertThat("Total orders count is incorrect", totalOrders, equalTo( 2));
    }

    @Test
    public void userCanNotGetOrdersListWithoutAuthorization() {
        Response response = userOrdersList.getOrdersList(null);

        int getOrderListStatusCode = response.statusCode();
        assertThat("Create order status code is incorrect", getOrderListStatusCode, equalTo( 401));

        String errorMessage = response.path("message");
        assertThat("Error message is incorrect", errorMessage, equalTo( "You should be authorised"));
    }
}
