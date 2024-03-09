package praktikum.client;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.ValidatableResponse;
import praktikum.model.CreateUser;

import static io.restassured.RestAssured.given;
import static praktikum.constants.Endpoints.*;

public class UserClient extends RestClient {
    @Step("Создание пользователя")
    public ValidatableResponse createUser(CreateUser user) {
        return given()
                .filter(new AllureRestAssured()).spec(requestSpecification()).body(user).post(CREATE_USER).then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(CreateUser user, String accessToken) {
        return given()
                .filter(new AllureRestAssured()).spec(requestSpecification()).header("Authorization", accessToken).body(user).delete(DELETE_USER).then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(CreateUser user) {
        return given()
                .filter(new AllureRestAssured()).spec(requestSpecification()).body(user).post(LOGIN_USER).then();
    }

    @Step("Обновление данных пользователя с авторизацией")
    public ValidatableResponse updateUserWithLogin(CreateUser user, String accessToken) {
        return given()
                .filter(new AllureRestAssured()).spec(requestSpecification()).header("Authorization", accessToken).body(user).patch(DELETE_USER).then();
    }

    @Step("Обновление данных пользователя без авторизации")
    public ValidatableResponse updateUserWithOutLogin(CreateUser user) {
        return given()
                .filter(new AllureRestAssured()).spec(requestSpecification()).body(user).patch(DELETE_USER).then();
    }
}