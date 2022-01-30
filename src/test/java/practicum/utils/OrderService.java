package practicum.utils;

import com.google.gson.Gson;
import io.restassured.response.Response;
import practicum.dto.CreateOrderRequest;

import static io.restassured.RestAssured.given;

public class OrderCreator extends BaseService {
    private static final String ORDER_URI ="api/orders";
    private final Gson gson = new Gson();

    public Response createOrder(String accessToken, String[] ingredients) {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(ingredients);
        String createOrderRequestBody =  gson.toJson(createOrderRequest);

        return given()
                .spec(getAuthorizedSpec(accessToken))
                .body(createOrderRequestBody)
                .when()
                .post(ORDER_URI);
    }
}
