package practicum.utils;

import com.google.gson.Gson;
import io.restassured.response.Response;
import practicum.dto.CreateOrderRequest;

import static io.restassured.RestAssured.given;

public class OrderService extends BaseService {
    private static final String ORDER_URI ="api/orders";
    private final Gson gson = new Gson();

    public Response createOrder(String accessToken, String[] ingredients) {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(ingredients);

        return given()
                .spec(getAuthorizedSpec(accessToken))
                .body(gson.toJson(createOrderRequest, CreateOrderRequest.class))
                .when()
                .post(ORDER_URI);
    }

    public Response getOrdersList(String accessToken) {
        return given()
                .spec(getAuthorizedSpec(accessToken))
                .when()
                .get(ORDER_URI);
    }
}
