package com.saucedemo.tests.ui;

import com.saucedemo.pages.ProductsPage;
import com.saucedemo.tests.BaseTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicReference;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;

public class LoginTests extends BaseTest {

    @Test
    public void correctLoginCredentialsTest() {
        AtomicReference<ProductsPage> productsPage = new AtomicReference<>();

        step("Авторизация", () -> productsPage.set(loginPage.login()));

        step("Проверка успешной авторизации", () -> assertThat(productsPage.get().getTitle()).hasText("Products"));
    }

    @Test
    public void lockedOutUserTest() {
        step("Авторизация (не правильная)", () -> loginPage.loginAs("wrong", "fake"));

        step("Проверка вывода ошибки", () -> assertThat(loginPage.getErrorMessage())
                .hasText("Epic sadface: Username and password do not match any user in this service"));
    }

    @Test(enabled = false)
    public void failTest() {
        AtomicReference<ProductsPage> productsPage = new AtomicReference<>();

        step("Авторизация", () -> productsPage
                .set(loginPage.loginAs("fakeUsername", "fakePassword")));

        step("Падение теста", () -> assertThat(productsPage.get().getTitle()).isVisible());
    }

    @DataProvider(name = "login data")
    public Object[][] loginData() {
        return new Object[][]{
                {"", "password", "Username"},
                {"username", "", "Password"},
                {"", "", "Username"}
        };
    }

    @Test(dataProvider = "login data")
    public void authorizationWithEmptyUserDataTest(String username, String password, String errorMessage) {
        step("Авторизация с пустыми данными", () -> loginPage.loginAs(username, password));

        step("Проверка текста сообщения", () -> assertThat(loginPage.getErrorMessage())
                .hasText("Epic sadface: %s is required".formatted(errorMessage)));
    }

    @Test
    public void authorizationWithLockedUserTest() {
        step("Авторизация под заблокированным узером", () ->
                loginPage.loginAs("locked_out_user", "secret_sauce"));

        step("Проверка текста сообщения", () -> assertThat(loginPage.getErrorMessage())
                .hasText("Epic sadface: Sorry, this user has been locked out."));
    }
}
