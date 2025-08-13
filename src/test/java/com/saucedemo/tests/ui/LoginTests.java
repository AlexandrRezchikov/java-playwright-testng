package com.saucedemo.tests.ui;

import com.saucedemo.pages.ProductsPage;
import com.saucedemo.tests.BaseTest;
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

    @Test
    public void failTest() {
        AtomicReference<ProductsPage> productsPage = new AtomicReference<>();

        step("Авторизация", () -> productsPage
                .set(loginPage.loginAs("fakeUsername", "fakePassword")));

        step("Падение теста", () -> assertThat(productsPage.get().getTitle()).isVisible());
    }
}
