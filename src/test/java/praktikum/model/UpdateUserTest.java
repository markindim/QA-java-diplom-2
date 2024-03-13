package praktikum.model;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import net.datafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.client.UserClient;
import praktikum.service.UserGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class UpdateUserTest {
    private static UserClient userClient;
    private static CreateUser user;
    private static String accessToken;
    private static final Faker faker = new Faker();
    private static final UserGenerator userGenerator = new UserGenerator();

    @Before
    @Step("Подготовка тестовых данных - создание пользователя и получение accessToken")
    public void setUp() {
        userClient = new UserClient();
        user = userGenerator.getRandomUser();
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
    @DisplayName("Обновление данных пользователя c авторизацией. Изменение поля Email")
    @Description("Позитивная проверка изменения поля email пользователя")
    public void updateUserWithLoginChangeEmail() {
        user.setEmail(faker.internet().emailAddress());
        ValidatableResponse response = userClient.updateUserWithLogin(user, accessToken);
        response.assertThat().statusCode(SC_OK).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Обновление данных пользователя c авторизацией. Изменение поля Password")
    @Description("Позитивная проверка изменения поля password пользователя")
    public void updateUserWithLoginChangePassword() {
        user.setPassword(String.format(RandomStringUtils.randomAlphabetic(10)));
        ValidatableResponse response = userClient.updateUserWithLogin(user, accessToken);
        response.assertThat().statusCode(SC_OK).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Обновление данных пользователя c авторизацией. Изменение поля Name")
    @Description("Позитивная проверка изменения name пользователя")
    public void updateUserWithLoginChangeName() {
        user.setName(faker.name().fullName());
        ValidatableResponse response = userClient.updateUserWithLogin(user, accessToken);
        response.assertThat().statusCode(SC_OK).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Обновление данных пользователя без авторизации. Изменение поля Email")
    @Description("Негативная проверка изменения поля email пользователя без авторизации пользователя")
    public void updateUserWithOutLoginChangeEmail() {
        user.setEmail(String.format("%s@yandex.ru", faker.name().username()));
        ValidatableResponse response = userClient.updateUserWithOutLogin(user);
        response.assertThat().statusCode(SC_UNAUTHORIZED).body("success", equalTo(false));
    }

    @Test
    @DisplayName("Обновление данных пользователя без авторизации. Изменение поля Password")
    @Description("Негативная проверка изменения поля password пользователя без авторизации пользователя")
    public void updateUserWithOutLoginChangePassword() {
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        ValidatableResponse response = userClient.updateUserWithOutLogin(user);
        response.assertThat().statusCode(SC_UNAUTHORIZED).body("success", equalTo(false));
    }

    @Test
    @DisplayName("Обновление данных пользователя без авторизации. Изменение поля Name")
    @Description("Негативная проверка изменения поля name пользователя без авторизации пользователя")
    public void updateUserWithOutLoginChangeName() {
        user.setName(faker.name().fullName());
        ValidatableResponse response = userClient.updateUserWithOutLogin(user);
        response.assertThat().statusCode(SC_UNAUTHORIZED).body("success", equalTo(false));
    }
}