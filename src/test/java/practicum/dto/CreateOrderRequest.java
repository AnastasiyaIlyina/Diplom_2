package practicum.dto;

public class CreateOrderRequest {
    private final String[] ingredients;

    public CreateOrderRequest(String[] ingredients) {
        this.ingredients = ingredients;
    }
}
