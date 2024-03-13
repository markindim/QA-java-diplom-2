package praktikum.model;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.client.OrderClient;
import praktikum.client.UserClient;
import praktikum.service.IngredientGenerator;
import praktikum.service.UserGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class GetOrderUserTest {
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
    @Step("Получение заказов для авторизированного пользователя")
    @Description("Позитивная проверка получение заказов авторизированного пользователя")
    public void getOrderWithLogin() {
        orderClient.createOrderWithLogin(ingredientGenerator.getCorrectIngredients(), accessToken);
        ValidatableResponse response = orderClient.getOrderForUserWithLogin(accessToken);
        response.assertThat().statusCode(SC_OK).body("success", equalTo(true));
    }

    @Test
    @Step("Получение заказов для не авторизированного пользователя")
    @Description("Негативная проверка получение заказов не авторизированного пользователя")
    public void getOrderWithoutLogin() {
        ValidatableResponse response = orderClient.getOrderForUserWithoutLogin();
        response.assertThat().statusCode(SC_UNAUTHORIZED).body("message", equalTo("You should be authorised"));
    }
}