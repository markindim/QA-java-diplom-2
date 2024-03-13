package praktikum.client;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.ValidatableResponse;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static praktikum.constants.Endpoints.ORDERS_USER;

public class OrderClient extends RestClient {

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrderWithLogin(Map<String, String[]> ingredients, String accessToken) {
        return given()
                .filter(new AllureRestAssured()).spec(requestSpecification()).header("Authorization", accessToken).body(ingredients).post(ORDERS_USER).then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutLogin(Map<String, String[]> ingredients) {
        return given()
                .filter(new AllureRestAssured()).spec(requestSpecification()).body(ingredients).post(ORDERS_USER).then();
    }

    @Step("Получение заказа конкретного пользователя С авторизацией")
    public ValidatableResponse getOrderForUserWithLogin(String accessToken) {
        return given()
                .filter(new AllureRestAssured()).spec(requestSpecification()).header("Authorization", accessToken).get(ORDERS_USER).then();
    }

    @Step("Получение заказа конкретного пользователя без авторизации")
    public ValidatableResponse getOrderForUserWithoutLogin() {
        return given()
                .filter(new AllureRestAssured()).spec(requestSpecification()).get(ORDERS_USER).then();
    }
}