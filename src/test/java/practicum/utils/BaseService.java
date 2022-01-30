package practicum;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseService {

    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    protected RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .build();
    }

    protected RequestSpecification getAuthorizedSpec(String accessToken) {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setContentType(ContentType.JSON);

        if (accessToken != null) {
            builder.addHeader("Authorization", accessToken);
        }

        return builder
                .setBaseUri(BASE_URL)
                .build();
    }
}
