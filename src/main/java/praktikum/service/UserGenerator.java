package praktikum.service;

import net.datafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;
import praktikum.model.CreateUser;

public class UserGenerator {

    private static final Faker faker = new Faker();

    public CreateUser getRandomUser() {
        return CreateUser.builder()
                .email(String.format("%s@yandex.ru", faker.name().username()))
                .password(RandomStringUtils.randomAlphabetic(10))
                .name(faker.name().fullName())
                .build();
    }
}