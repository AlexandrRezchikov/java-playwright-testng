package com.saucedemo.tests.ui;

import com.saucedemo.models.ShipInfo;
import com.saucedemo.pages.CartPage;
import com.saucedemo.tests.BaseTest;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicReference;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.saucedemo.config.ConfigurationManager.config;
import static io.qameta.allure.Allure.step;

public class CartTests extends BaseTest {

    @Test(testName = "Добавление товаров в корзину")
    public void transferToCartTest() {
        AtomicReference<CartPage> cartPage = new AtomicReference<>();

        step("Добавления товара в корзину", () ->
                cartPage.set(loginPage.login().addItemToCart("Sauce Labs Bike Light").clickOnCart()));
        step("Проверка перехода в корзину", () ->
                assertThat(page).hasURL(config().baseUrl() + "/cart.html"));
        step("Проверка товара в корзине", () ->
                assertThat(cartPage.get().getItems()).hasText("Sauce Labs Bike Light"));
    }

    @Test(testName = "Очистка корзины")
    public void cleanCartTest() {
        AtomicReference<CartPage> cartPage = new AtomicReference<>();

        step("Логин и добавление товаров", () -> cartPage.set(loginPage
                .login()
                .addItemToCart("Sauce Labs Backpack")
                .addItemToCart("Sauce Labs Bike Light")
                .addItemToCart("Sauce Labs Bolt T-Shirt")
                .clickOnCart()));
        step("Проверка количества товаров", () -> assertThat(cartPage.get().getItems()).hasCount(3));
        step("Очистка корзины", () -> cartPage.get().cleanCart());
        step("Проверка пустой корзины", () -> assertThat(cartPage.get().getItems()).isHidden());
    }

    @Test(testName = "Продолжение покупок")
    public void continueShoppingTest() {
        loginPage.login().clickOnCart().clickOnContinueShopping();
        assertThat(page).hasURL(config().baseUrl() + "/inventory.html");
    }

    @Test(testName = "Оформление заказа с пустыми данными")
    public void makingOrderWithEmptyDataTest() {
        AtomicReference<CartPage> cartPage = new AtomicReference<>();

        step("Логин и добавление товара в корзину", () -> {
            cartPage.set(loginPage.login().addItemToCart("Sauce Labs Fleece Jacket").clickOnCart());
        });
        step("Оформление заказа с пустыми данными", () -> {
            ShipInfo shipInfo = ShipInfo.builder()
                    .firstName("")
                    .lastName("")
                    .zip("")
                    .build();
            cartPage.get().clickOnCheckout().fillInfo(shipInfo).clickOnContinue();
        });
        step("Проверка текста ошибки", () ->
                assertThat(cartPage.get().getErrorMessage()).hasText("Error: First Name is required"));
    }
}
