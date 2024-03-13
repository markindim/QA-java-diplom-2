package praktikum.client;

import static io.restassured.RestAssured.given;
import static praktikum.constants.Endpoints.*;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import praktikum.model.Ingredient;


public class IngredientClient extends RestClient {
    public ValidatableResponse getAllIngredient() {
        return given()
                .filter(new AllureRestAssured()).spec(requestSpecification()).get(ALL_INGREDIENT).then();
    }

    public Ingredient getDefaultIngredient() {
        return given()
                .filter(new AllureRestAssured()).contentType(ContentType.JSON).baseUri(BASE_URI).get(ALL_INGREDIENT).body().as(Ingredient.class);
    }
}