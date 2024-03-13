package praktikum.model;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.client.UserClient;
import praktikum.service.UserGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    private UserClient userClient;
    private CreateUser user;
    private String accessToken;
    private final UserGenerator userGenerator = new UserGenerator();

    @Before
    @Step("Подготовка тестовых данных - создание пользователя и получение accessToken")
    public void setUp() {
        userClient = new UserClient();
        user = userGenerator.getRandomUser();
        ValidatableResponse response = userClient.createUser(user);
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
    @DisplayName("Авторизация существующего пользователя")
    @Description("Позитивная проверка авторизации пользователя")
    public void loginUser() {
        ValidatableResponse response = userClient.loginUser(user);
        response.assertThat().statusCode(SC_OK).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Авторизация с неверным полем Email пользователя")
    @Description("Негативная проверка авторизации пользователя с неверным полем email")
    public void loginUserWithIncorrectEmail() {
        user.setEmail(String.format("%s@yandex.ru", RandomStringUtils.randomAlphabetic(8)));
        ValidatableResponse response = userClient.loginUser(user);
        response.assertThat().statusCode(SC_UNAUTHORIZED).body("success", equalTo(false));
    }

    @Test
    @DisplayName("Авторизация с неверным полем Password пользователя")
    @Description("Негативная проверка авторизации пользователя с неверным полем password")
    public void loginUserWithIncorrectPassword() {
        user.setPassword(String.format(RandomStringUtils.randomAlphabetic(10)));
        ValidatableResponse response = userClient.loginUser(user);
        response.assertThat().statusCode(SC_UNAUTHORIZED).body("success", equalTo(false));
    }
}