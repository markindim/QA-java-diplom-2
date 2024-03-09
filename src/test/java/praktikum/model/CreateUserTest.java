package praktikum.model;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.client.UserClient;
import praktikum.service.UserGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;


public class CreateUserTest {
    private static UserClient userClient;
    private static CreateUser user;
    private static String accessToken;
    private static final UserGenerator userGenerator = new UserGenerator();


    @Before
    @Step("Подготовка тестовых данных - создание данных пользователя")
    public void setUp() {
        userClient = new UserClient();
        user = userGenerator.getRandomUser();
    }

    @After
    @Step("Очистка тестовых данных")
    public void cleanUp() {
        if (accessToken != null) {
            System.out.println("Удаляется пользователь " + user.getName());
            userClient.deleteUser(user, accessToken);
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Позитивная проверка создания пользователя с валидными данными")
    public void createUser() {
        ValidatableResponse response = userClient.createUser(user);
        response.assertThat().statusCode(SC_OK).body("success", equalTo(true));
        accessToken = response.extract().body().path("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя, который уже существует")
    @Description("Негативная проверка создания существующего пользователя")
    public void creatingAnExistingUser() {
        ValidatableResponse createUserResponse = userClient.createUser(user);
        accessToken = createUserResponse.extract().body().path("accessToken");

        ValidatableResponse createExistingUserResponse = userClient.createUser(user);
        createExistingUserResponse.assertThat().statusCode(SC_FORBIDDEN).body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание пользователя без поля Email")
    @Description("Негативная проверка создания пользователя без поля email")
    public void createUserWithoutEmail() {
        user.setEmail(null);
        ValidatableResponse response = userClient.createUser(user);
        response.assertThat().statusCode(SC_FORBIDDEN).body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание пользователя без поля Password")
    @Description("Негативная проверка создания пользователя без поля password")
    public void createUserWithoutPassword() {
        user.setPassword(null);
        ValidatableResponse response = userClient.createUser(user);
        response.assertThat().statusCode(SC_FORBIDDEN).body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание пользователя без поля Name")
    @Description("Негативная проверка создания пользователя без поля name")
    public void createUserWithoutName() {
        user.setName(null);
        ValidatableResponse response = userClient.createUser(user);
        response.assertThat().statusCode(SC_FORBIDDEN).body("success", equalTo(false));
    }
}