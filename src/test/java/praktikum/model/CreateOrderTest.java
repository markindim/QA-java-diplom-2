package praktikum.model;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.client.OrderClient;
import praktikum.client.UserClient;
import praktikum.service.IngredientGenerator;
import praktikum.service.UserGenerator;

import java.util.Map;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {
    private static UserClient userClient;
    private static OrderClient orderClient;
    private static CreateUser user;
    private static IngredientGenerator ingredientGenerator;
    private static String accessToken;
    private static final UserGenerator userGenerator = new UserGenerator();

    @Before
    @Step("Подготовка тестовых данных - создание пользователя и получение accessToken")
    public void setUp() {
        userClient = new UserClient();
        user = userGenerator.getRandomUser();
        orderClient = new OrderClient();
        ingredientGenerator = new IngredientGenerator();
        userClient.createUser(user);
        ValidatableResponse response = userClient.loginUser(user);
        accessToken = response.extract().body().path("accessToken");
    }

    @After
    @Step("Очистка тестовых данных")
    public void cleanUp() {
        if (accessToken != null) {
            userClient.deleteUser(user, accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа авторизированного пользователя с ингредиентами")
    @Description("Позитивная проверка создания заказа пользователя с авторизацией и ингредиентами")
    public void createOrderWithLogin() {
        ValidatableResponse response = orderClient.createOrderWithLogin(ingredientGenerator.getCorrectIngredients(), accessToken);
        response.assertThat().statusCode(SC_OK).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации пользователя с ингредиентами")
    @Description("Позитивная проверка создания заказа пользователя без авторизации и ингредиентами")
    public void createOrderWithoutLogin() {
        Map<String, String[]> ingredient = ingredientGenerator.getCorrectIngredients();
        ValidatableResponse response = orderClient.createOrderWithoutLogin(ingredient);
        response.assertThat().statusCode(SC_OK).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и без ингредиентов")
    @Description("Негативная проверка создания заказа пользователя с авторизацией и  без ингредиентов")
    public void createOrderWithoutIngredient() {
        Map<String, String[]> ingredient = ingredientGenerator.getEmptyIngredients();
        ValidatableResponse response = orderClient.createOrderWithLogin(ingredient, accessToken);
        response.assertThat().statusCode(SC_BAD_REQUEST).body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и неверным хэшем")
    @Description("Негативная проверка создания заказа пользователя с авторизацией и неверным хэшем")
    public void createOrderWithInvalidHash() {
        Map<String, String[]> ingredient = ingredientGenerator.getIncorrectIngredients();
        ValidatableResponse response = orderClient.createOrderWithLogin(ingredient, accessToken);
        response.assertThat().statusCode(SC_BAD_REQUEST).body("message", equalTo("One or more ids provided are incorrect"));
    }
}